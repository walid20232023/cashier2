package org.openmrs.module.dispensing;

public class MedicationFrequency {
    String frequency;

    public MedicationFrequency() {
    }

    public MedicationFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
