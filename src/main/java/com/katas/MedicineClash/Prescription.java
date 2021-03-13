package com.katas.MedicineClash;

import java.time.LocalDate;

public class Prescription {
    private final LocalDate date;
    private final int daysSupply;

    public Prescription(LocalDate date, int daysSupply) {

        this.date = date;
        this.daysSupply = daysSupply;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getDaysSupply() {
        return daysSupply;
    }
}
