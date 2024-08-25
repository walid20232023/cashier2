package org.openmrs.module.dispensing.api.importer;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Drug;
import org.openmrs.api.ConceptService;
import org.openmrs.module.dispensing.importer.DrugImporter;
import org.openmrs.module.dispensing.importer.ImportNotes;
import org.openmrs.module.emrapi.test.builder.ConceptBuilder;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class DrugImporterComponentTest extends BaseModuleContextSensitiveTest {

    @Autowired
    private DrugImporter drugImporter;

    @Autowired
    private ConceptService conceptService;

    @Before
    public void setUp() throws Exception {
        new ConceptBuilder(conceptService, conceptService.getConceptDatatypeByName("N/A"), conceptService.getConceptClassByName("Misc"))
                .addName("Tablet")
                .addMapping(conceptService.getConceptMapTypeByName("same-as"), conceptService.getConceptSourceByName("SNOMED CT"), "385055001")
                .saveAndGet();
    }

    @Test
    public void importSpreadsheet() throws Exception {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("drug-list-component-test.csv");
        InputStreamReader reader = new InputStreamReader(inputStream);

        ImportNotes notes = drugImporter.importSpreadsheet(reader);
        if (notes.hasErrors()) {
            for (String s : notes.getErrors()) {
                System.out.println(s);
            }

        }
        assertFalse(notes.hasErrors());

        Drug aspirin = conceptService.getDrug("Aspirin, 5 mg, tablet");
        assertNotNull(aspirin);
        assertThat(aspirin.getDosageForm().getName().getName(), is("Tablet"));
        assertNotNull(conceptService.getDrug("Triomune-30, some other"));
        assertNotNull(conceptService.getDrug("NYQUIL, 125 mg/ 5 mL, 100 mL bottle"));

    }


}
