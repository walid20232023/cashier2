package org.openmrs.module.dispensing.api.importer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.Drug;
import org.openmrs.api.ConceptService;
import org.openmrs.module.dispensing.importer.DrugImporter;
import org.openmrs.module.dispensing.importer.ImportNotes;
import org.openmrs.module.emrapi.concept.EmrConceptService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DrugImporterTest {

    private DrugImporter drugImporter;

    private EmrConceptService emrConceptService;

    private ConceptService conceptService;

    private Concept amoxicillinConcept;

    private Concept amitriptylineHydrochlorideConcept;

    private Concept amlodipineBeslyateConcept;

    @Before
    public void setup() {

        drugImporter = new DrugImporter();
        emrConceptService = mock(EmrConceptService.class);
        conceptService = mock(ConceptService.class);

        drugImporter.setEmrConceptService(emrConceptService);
        drugImporter.setConceptService(conceptService);

        amoxicillinConcept = new Concept();
        ConceptName amoxicillinConceptName = new ConceptName();
        amoxicillinConceptName.setName("Amoxicillin");
        amoxicillinConcept.addName(amoxicillinConceptName);
        when(emrConceptService.getConcept("b85d8dc0-329d-11e3-aa6e-0800200c9a66")).thenReturn(amoxicillinConcept);

        amitriptylineHydrochlorideConcept = new Concept();
        ConceptName amitriptylineHydrochlorideConceptName = new ConceptName();
        amitriptylineHydrochlorideConceptName.setName("Amitriptyline hydrochloride");
        amitriptylineHydrochlorideConcept.addName(amitriptylineHydrochlorideConceptName);
        when(conceptService.getConceptsByName("Amitriptyline hydrochloride"))
                .thenReturn(Collections.singletonList(amitriptylineHydrochlorideConcept));

        amlodipineBeslyateConcept = new Concept();
        ConceptName amlodipineBeslyateConceptName = new ConceptName();
        amlodipineBeslyateConceptName.setName("Amlodipine besylate");
        amlodipineBeslyateConcept.addName(amlodipineBeslyateConceptName);
        when(conceptService.getConceptsByName("Amlodipine besylate"))
                .thenReturn(Collections.singletonList(amlodipineBeslyateConcept));

    }


    @Test
    public void validDrugListShouldPassValidation() throws IOException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("drug-list-test.csv");
        InputStreamReader reader = new InputStreamReader(inputStream);

        ImportNotes notes = drugImporter.verifySpreadsheet(reader);
        assertFalse(notes.hasErrors());

        // TODO: should have proper notes

    }

    @Test
    public void drugListShouldFailValidationIfNoMatchingConceptByName() throws IOException {

        // override an existing when from the setup
        when(conceptService.getConceptsByName("Amitriptyline hydrochloride")).thenReturn(new ArrayList<Concept>());

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("drug-list-test.csv");
        InputStreamReader reader = new InputStreamReader(inputStream);

        ImportNotes notes = drugImporter.verifySpreadsheet(reader);
        assertTrue(notes.hasErrors());
        assertThat(notes.getErrors().size(), is(1));
        assertThat(notes.getErrors().get(0), containsString("cannot find concept named"));

    }

    @Test
    public void drugListShouldFailValidationIfMultipleMatchesForConceptName() throws IOException {

        // override an existing when from the setup
        when(conceptService.getConceptsByName("Amitriptyline hydrochloride")).thenReturn(Arrays.asList(new Concept(), new Concept()));

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("drug-list-test.csv");
        InputStreamReader reader = new InputStreamReader(inputStream);

        ImportNotes notes = drugImporter.verifySpreadsheet(reader);
        assertTrue(notes.hasErrors());
        assertThat(notes.getErrors().size(), is(1));
        assertThat(notes.getErrors().get(0), containsString("found multiple candidate concepts named"));

    }


    @Test
    public void drugListShouldFailValidationIfNoMatchingConceptWithUuid() throws IOException {

        // override an existing when from the setup
        when(emrConceptService.getConcept("b85d8dc0-329d-11e3-aa6e-0800200c9a66")).thenReturn(null);

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("drug-list-test.csv");
        InputStreamReader reader = new InputStreamReader(inputStream);

        ImportNotes notes = drugImporter.verifySpreadsheet(reader);
        assertTrue(notes.hasErrors());
        assertThat(notes.getErrors().size(), is(2));
        assertThat(notes.getErrors().get(0), containsString("Specified concept not found"));

    }

    @Test
    public void drugListShouldFailValidationIfExistingDrugWithSameName() throws IOException {

        Drug existingDrug = new Drug();
        when(conceptService.getDrug("Amitriptyline hydrochloride, 25 mg, coated tablet")).thenReturn(existingDrug);

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("drug-list-test.csv");
        InputStreamReader reader = new InputStreamReader(inputStream);

        ImportNotes notes = drugImporter.verifySpreadsheet(reader);
        assertTrue(notes.hasErrors());
        assertThat(notes.getErrors().size(), is(1));
        assertThat(notes.getErrors().get(0), containsString("Product already exists in database with different Uuid"));

    }

    @Test
    public void drugListShouldPassValidationIfExistingDrugWithSameNameButDrugInSpreadsheetHasNoUuid() throws IOException {

        Drug existingDrug = new Drug();
        when(conceptService.getDrug("Amoxicillin, 500 mg + potassium clavulanic acid, 125 mg, tablet")).thenReturn(existingDrug);

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("drug-list-test.csv");
        InputStreamReader reader = new InputStreamReader(inputStream);

        ImportNotes notes = drugImporter.verifySpreadsheet(reader);
        assertFalse(notes.hasErrors());

    }


    @Test
    public void drugListShouldFailValidationIfDuplicateOpenBoxesCode() throws IOException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("drug-list-test-duplicate-inventory-code.csv");
        InputStreamReader reader = new InputStreamReader(inputStream);

        ImportNotes notes = drugImporter.verifySpreadsheet(reader);
        assertTrue(notes.hasErrors());
        assertThat(notes.getErrors().size(), is(1));
        assertThat(notes.getErrors().get(0), containsString("Duplicate Inventory Code"));

    }

    @Test
    public void drugListShouldFailValidationIfDuplicateProductName() throws IOException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("drug-list-test-duplicate-product-name.csv");
        InputStreamReader reader = new InputStreamReader(inputStream);

        ImportNotes notes = drugImporter.verifySpreadsheet(reader);
        assertTrue(notes.hasErrors());
        assertThat(notes.getErrors().size(), is(1));
        assertThat(notes.getErrors().get(0), containsString("Duplicate Product Name"));

    }

    @Test
    public void shouldImportDrugList() throws IOException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("drug-list-test.csv");
        InputStreamReader reader = new InputStreamReader(inputStream);

        ImportNotes notes = drugImporter.importSpreadsheet(reader);
        assertTrue(notes.getErrors().isEmpty());

        Drug amitriptylineHydrochloride = new Drug();
        amitriptylineHydrochloride.setName("Amitriptyline hydrochloride, 25 mg, coated tablet");
        amitriptylineHydrochloride.setUuid("63e31e90-329d-11e3-aa6e-0800200c9a66");
        amitriptylineHydrochloride.setConcept(amitriptylineHydrochlorideConcept);

        Drug amlodipineBesylate = new Drug();
        amlodipineBesylate.setName("Amlodipine besylate, 5 mg, tablet");
        amlodipineBesylate.setUuid("6ca5db30-329d-11e3-aa6e-0800200c9a66");
        amlodipineBesylate.setConcept(amlodipineBeslyateConcept);

        Drug amoxicillin1 = new Drug();
        amoxicillin1.setName("Amoxicillin, 125 mg/ 5 mL, powder for suspension, 100 mL bottle");
        amoxicillin1.setConcept(amoxicillinConcept);

        Drug amoxicillin2 = new Drug();
        amoxicillin2.setName("Amoxicillin, 500 mg + potassium clavulanic acid, 125 mg, tablet");
        amoxicillin2.setConcept(amoxicillinConcept);

        verify(conceptService).saveDrug(argThat(new IsExpectedDrugWithUuid(amitriptylineHydrochloride)));
        verify(conceptService).saveDrug(argThat(new IsExpectedDrugWithUuid(amlodipineBesylate)));
        verify(conceptService).saveDrug(argThat(new IsExpectedDrug(amoxicillin1)));
        verify(conceptService).saveDrug(argThat(new IsExpectedDrug(amoxicillin2)));

    }

    @Test
    public void shouldUpdateExistingDrugName() throws IOException {

        Drug existingDrug = new Drug();
        Concept wrongConcept = new Concept();
        existingDrug.setName("some random name");
        existingDrug.setUuid("6ca5db30-329d-11e3-aa6e-0800200c9a66");
        existingDrug.setConcept(wrongConcept);
        when(conceptService.getDrugByUuid("6ca5db30-329d-11e3-aa6e-0800200c9a66")).thenReturn(existingDrug);

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("drug-list-test.csv");
        InputStreamReader reader = new InputStreamReader(inputStream);

        ImportNotes notes = drugImporter.importSpreadsheet(reader);
        assertTrue(notes.getErrors().isEmpty());

        // verify that the name and concept of the existing drug have been updated
        assertThat(existingDrug.getName(), is("Amlodipine besylate, 5 mg, tablet"));
        assertThat(existingDrug.getConcept(), is(amlodipineBeslyateConcept));
        verify(conceptService).saveDrug(argThat(new IsExpectedDrugWithUuid(existingDrug)));

    }

    @Test
    public void shouldUpdateExistingDrugReferencedByName() throws IOException {

        Drug existingDrug = new Drug();
        Concept wrongConcept = new Concept();
        existingDrug.setName("Amlodipine besylate, 5 mg, tablet");
        existingDrug.setUuid("6ca5db30-329d-11e3-aa6e-0800200c9a66");
        existingDrug.setConcept(wrongConcept);
        when(conceptService.getDrug("Amlodipine besylate, 5 mg, tablet")).thenReturn(existingDrug);

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("drug-list-test.csv");
        InputStreamReader reader = new InputStreamReader(inputStream);

        ImportNotes notes = drugImporter.importSpreadsheet(reader);
        assertTrue(notes.getErrors().isEmpty());

        // verify that the concept of the existing drug have been updated
        assertThat(existingDrug.getConcept(), is(amlodipineBeslyateConcept));
        verify(conceptService).saveDrug(argThat(new IsExpectedDrugWithUuid(existingDrug)));

    }


    private class IsExpectedDrug extends ArgumentMatcher<Drug> {

        Drug expectedDrug;

        public IsExpectedDrug(Drug drug) {
            this.expectedDrug = drug;
        }

        @Override
        public boolean matches(Object o) {
            Drug actualDrug = (Drug) o;

            return actualDrug.getName().equals(expectedDrug.getName()) &&
                    actualDrug.getConcept().equals(expectedDrug.getConcept()) &&
                    actualDrug.getUuid() != null;

        }
    }

    private class IsExpectedDrugWithUuid extends ArgumentMatcher<Drug> {

        Drug expectedDrug;

        public IsExpectedDrugWithUuid(Drug drug) {
            this.expectedDrug = drug;
        }

        @Override
        public boolean matches(Object o) {
            Drug actualDrug = (Drug) o;

            return actualDrug.getName().equals(expectedDrug.getName()) &&
                    actualDrug.getConcept().equals(expectedDrug.getConcept()) &&
                    actualDrug.getUuid().equals(expectedDrug.getUuid());
        }
    }


}
