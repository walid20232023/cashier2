package org.openmrs.module.dispensing;

import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.Obs;

import java.util.Date;

/**
 * Convenience representation of a medication that was dispensed to a patient.
 *
 * In the underlying data model this is represented as an Obs group (pointed to by the existingObs field), and the
 * mapping of obs group members to fields in this class are in
 * {@link org.openmrs.module.dispensing.descriptor.DispensingConceptSetDescriptor}
 */
public class DispensedMedication {

    private Date dispensedDateTime;
    private Drug drug;
    private MedicationDose medicationDose;
    private MedicationFrequency medicationFrequency;
    private Integer quantityDispensed;
    private MedicationDuration medicationDuration;
    private String timingOfHospitalPrescription;
    private Location dischargeLocation;
    private String administrationInstructions;

    private Obs existingObs;

    public DispensedMedication() {
    }

    public DispensedMedication(Date dispensedDateTime, Drug drug, Obs existingObs,
                               MedicationDose medicationDose, MedicationDuration medicationDuration,
                               MedicationFrequency medicationFrequency, Integer quantityDispensed,
                               String timingOfHospitalPrescription, Location dischargeLocation) {
        this.dispensedDateTime = dispensedDateTime;
        this.drug = drug;
        this.existingObs = existingObs;
        this.medicationDose = medicationDose;
        this.medicationDuration = medicationDuration;
        this.medicationFrequency = medicationFrequency;
        this.quantityDispensed = quantityDispensed;
        this.timingOfHospitalPrescription = timingOfHospitalPrescription;
        this.dischargeLocation = dischargeLocation;


    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public Obs getExistingObs() {
        return existingObs;
    }

    public void setExistingObs(Obs existingObs) {
        this.existingObs = existingObs;
    }

    public MedicationDose getMedicationDose() {
        return medicationDose;
    }

    public void setMedicationDose(MedicationDose medicationDose) {
        this.medicationDose = medicationDose;
    }

    public MedicationDuration getMedicationDuration() {
        return medicationDuration;
    }

    public void setMedicationDuration(MedicationDuration medicationDuration) {
        this.medicationDuration = medicationDuration;
    }

    public MedicationFrequency getMedicationFrequency() {
        return medicationFrequency;
    }

    public void setMedicationFrequency(MedicationFrequency medicationFrequency) {
        this.medicationFrequency = medicationFrequency;
    }

    public Integer getQuantityDispensed() {
        return quantityDispensed;
    }

    public void setQuantityDispensed(Integer quantityDispensed) {
        this.quantityDispensed = quantityDispensed;
    }

    public Date getDispensedDateTime() {
        return dispensedDateTime;
    }

    public void setDispensedDateTime(Date dispensedDateTime) {
        this.dispensedDateTime = dispensedDateTime;
    }

    public String getTimingOfHospitalPrescription() {
        return timingOfHospitalPrescription;
    }

    public void setTimingOfHospitalPrescription(String timingOfHospitalPrescription) {
        this.timingOfHospitalPrescription = timingOfHospitalPrescription;
    }

    public Location getDischargeLocation() {
        return dischargeLocation;
    }

    public void setDischargeLocation(Location dischargeLocation) {
        this.dischargeLocation = dischargeLocation;
    }

    public String getAdministrationInstructions() {
        return administrationInstructions;
    }

    public void setAdministrationInstructions(String administrationInstructions) {
        this.administrationInstructions = administrationInstructions;
    }

}
