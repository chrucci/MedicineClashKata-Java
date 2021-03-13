package com.katas.MedicineClash;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PrescriptionTests {
    @Test
    public void Constructor_WhenCalled_SetsDateAndDaysSupplied() {
        var expectedDate = LocalDate.now();
        var expectedDaysSupply = 90;
        var prescription = new Prescription(expectedDate, expectedDaysSupply);

        assertThat(prescription.getDate()).isEqualTo(expectedDate);
        assertThat(prescription.getDaysSupply()).isEqualTo(expectedDaysSupply);
    }
}