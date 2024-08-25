package org.openmrs.module.dispensing;

public class MedicationDuration {

    Integer duration;
    String timeUnits;

    public MedicationDuration() {
    }

    public MedicationDuration(Integer duration, String timeUnits) {
        this.duration = duration;
        this.timeUnits = timeUnits;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getTimeUnits() {
        return timeUnits;
    }

    public void setTimeUnits(String timeUnits) {
        this.timeUnits = timeUnits;
    }
}
