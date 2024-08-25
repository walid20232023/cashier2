package org.openmrs.module.dispensing.importer;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.api.ConceptService;
import org.openmrs.module.emrapi.concept.EmrConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

@Component
public class DrugImporter {

    @Autowired
    private EmrConceptService emrConceptService;

    @Autowired
    private ConceptService conceptService;

    private Map<String, String> dosageFormShortcuts = new HashMap<String, String>();

    public DrugImporter() {
        dosageFormShortcuts.put("Tablet", "SNOMED CT:385055001");
        dosageFormShortcuts.put("Capsule", "SNOMED CT:428641000");
    }

    private CellProcessor[] getCellProcessors() {
        return new CellProcessor[] {
                new Optional(new Trim()),               // uuid
                new Optional(new Trim()),   // OpenBoxes code
                new Trim(),                 // Name
                new Optional(new Trim()),   // Concept Code
                new Optional(new Trim())    // Dosage Form
        };
    }

    /**
     * For unit tests -- normally this is autowired
     * @param conceptService
     */
    public void setConceptService(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    public void setEmrConceptService(EmrConceptService emrConceptService) {
        this.emrConceptService = emrConceptService;
    }

    public ImportNotes verifySpreadsheet(Reader csvFileReader) throws IOException{
        List<DrugImporterRow> drugList = readSpreadsheet(csvFileReader);
        return verifySpreadsheetHelper(drugList);
    }

    private ImportNotes verifySpreadsheetHelper(List<DrugImporterRow> drugList) throws IOException {
        ImportNotes notes = new ImportNotes();

        Set<String> openBoxesCodes = new HashSet<String>();
        Set<String> productNames = new HashSet<String>();

        for (DrugImporterRow row : drugList) {

            // note that we currently don't store the inventory code anywhere!
            if (row.getInventoryCode() != null) {     // inventory code is optional
                if (openBoxesCodes.contains(row.getInventoryCode())) {
                    notes.addError("Duplicate Inventory Code: " + row.getInventoryCode());
                }
                openBoxesCodes.add(row.getInventoryCode());
            }

            if (productNames.contains(row.getProductName())) {
                notes.addError("Duplicate Product Name: " + row.getProductName());
            }

            Drug existingDrug = conceptService.getDrug(row.getProductName());
            if (existingDrug != null && row.getUuid() != null && !existingDrug.getUuid().equals(row.getUuid())) {
                notes.addError("Product already exists in database with different Uuid: " + row.getProductName()
                        + " (" + existingDrug.getUuid() + ")");
            }

            productNames.add(row.getProductName());

            if (row.getConcept() != null) {
                Concept concept = emrConceptService.getConcept(row.getConcept());
                if (concept == null) {
                    notes.addError("Specified concept not found: " + row.getConcept());
                } else {
                    notes.addNote(row.getConcept() + " -> " + concept.getId());
                }
            } else {
                String productName = row.getProductName();
                String genericName = productName.split(",")[0].trim();
                List<Concept> possibleConcepts = conceptService.getConceptsByName(genericName);
                if (possibleConcepts.size() == 0) {
                    notes.addError("At " + productName + ", cannot find concept named " + genericName);
                } else if (possibleConcepts.size() > 1) {
                    notes.addError("At " + productName + ", found multiple candidate concepts named " + genericName + ": " + possibleConcepts);
                } else {
                    notes.addNote(productName + " -> (auto-mapped) " + possibleConcepts.get(0).getNames().iterator().next().getName());
                }
            }

            if (StringUtils.isNotEmpty(row.getDosageForm())) {
                Concept concept = getConcept(row.getDosageForm(), dosageFormShortcuts);
                if (concept == null) {
                    notes.addError("dosage form concept not found: " + row.getDosageForm());
                }
                else {
                    notes.addNote(row.getDosageForm() + " -> " + concept.getId());
                }
            }
        }

        return notes;
    }

    public ImportNotes importSpreadsheet(Reader csvFileReader) throws IOException {

        List<DrugImporterRow> drugList = readSpreadsheet(csvFileReader);

        ImportNotes notes = verifySpreadsheetHelper(drugList);

        if (notes.hasErrors()) {
            return notes;
        }
        else {

            for (DrugImporterRow row : drugList) {

                Drug drug = null;

                // first, see if there is an existing drug with this uuid
                if (row.getUuid() != null) {
                    drug = conceptService.getDrugByUuid(row.getUuid());
                }

                // if not, see if there is an existing drug with this name
                if (drug == null) {
                    drug = conceptService.getDrug(row.getProductName());
                }

                // if not, we are creating a new drug
                if (drug == null) {
                    drug = new Drug();
                }

                // now create/update

                // set uuid
                if (row.getUuid() != null) {
                    drug.setUuid(row.getUuid());
                }

                // set name
                drug.setName(row.getProductName());

                // set concept
                Concept concept = null;
                if (row.getConcept() != null) {
                    concept = emrConceptService.getConcept(row.getConcept());
                    if (concept == null) {
                        // we should never get here, because validation should have been run
                        throw new RuntimeException("Specified concept not found: " + row.getConcept());
                    }
                } else {
                    String productName = row.getProductName();
                    String genericName = productName.split(",")[0].trim();
                    List<Concept> possibleConcepts = conceptService.getConceptsByName(genericName);
                    if (possibleConcepts.size() == 0) {
                        // we should never get here, because validation should have been run
                        throw new RuntimeException("At " + productName + ", cannot find concept named " + genericName);
                    } else if (possibleConcepts.size() > 1) {
                        // we should never get here, because validation should have been run
                        throw new RuntimeException("At " + productName + ", found multiple candidate concepts named " + genericName + ": " + possibleConcepts);
                    }
                    else {
                        concept = possibleConcepts.get(0);
                    }
                }
                drug.setConcept(concept);

                // set dosage form
                if (StringUtils.isNotEmpty(row.getDosageForm())) {
                    Concept dosageForm = getConcept(row.getDosageForm(), dosageFormShortcuts);
                    if (dosageForm == null) {
                        // we should never get here, because validation should have been run
                        throw new RuntimeException("Specified concept not found: " + row.getDosageForm());
                    }
                    drug.setDosageForm(dosageForm);
                }

                // note that we currently don't store the inventory code anywhere!

                conceptService.saveDrug(drug);
            }

            return notes;
        }

    }

    private Concept getConcept(String code, Map<String, String> shortcuts) {
        if (shortcuts != null && shortcuts.get(code) != null) {
            String shortcut = shortcuts.get(code);
            Concept concept = emrConceptService.getConcept(shortcut);
            if (concept != null) {
                return concept;
            }
        }
        return emrConceptService.getConcept(code);
    }

    private List<DrugImporterRow> readSpreadsheet(Reader csvFileReader) throws IOException  {

        List<DrugImporterRow> drugList = new ArrayList<DrugImporterRow>();

        CsvBeanReader csv = null;
        try {
            csv = new CsvBeanReader(csvFileReader, CsvPreference.EXCEL_PREFERENCE);
            csv.getHeader(true);
            CellProcessor[] cellProcessors = getCellProcessors();

            while (true) {
                DrugImporterRow row = csv.read(DrugImporterRow.class, DrugImporterRow.FIELD_COLUMNS, cellProcessors);
                if (row == null) {
                    break;
                }

                drugList.add(row);
            }

        } finally {
            if (csv != null) {
                csv.close();
            }
        }

        return drugList;
    }

}
