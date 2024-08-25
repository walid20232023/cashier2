package org.openmrs.module.dispensing;

public class MedicationDose {

    Double dose;
    String units;

    public MedicationDose() {
    }

    public MedicationDose(Double dose, String units) {
        this.dose = dose;
        this.units = units;
    }

    public Double getDose() {
        return dose;
    }

    public void setDose(Double dose) {
        this.dose = dose;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
