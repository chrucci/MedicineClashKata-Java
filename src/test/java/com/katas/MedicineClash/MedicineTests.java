package com.katas.MedicineClash;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MedicineTests {
    private LocalDate today = LocalDate.now();
    private LocalDate yesterday = today.minusDays(1);
    private Medicine medicine;
    private String defaultName = "foo";

    private Collection<LocalDate> GetRecentActiveDates() {
        Collection<LocalDate> expectedDaysActive = new ArrayList<>();
        expectedDaysActive.add(yesterday);
        expectedDaysActive.add(today);
        return expectedDaysActive;
    }

    @BeforeEach
    public void testSetup(){
        medicine = new Medicine(defaultName);
    }

    @Test
    public void getName_passedToConstructor_ReturnsInProperty(){
        assertThat(medicine.getName()).isEqualTo(defaultName);
    }

    @Test
    public void getActiveDates_WhenCalled_ReturnsListOfDatesMedicineHasBeenActive(){
        var prescription = new Prescription(yesterday, 2);
        medicine.addPrescription(prescription);

        assertThat(medicine.getActiveDays()).isEqualTo(GetRecentActiveDates());
    }

    @Test
    public void getActiveDates_WhenCalledWithPrescriptionExtendingToFuture_ReturnsDatesUpToToday(){
        var prescription = new Prescription(yesterday, 5);
        medicine.addPrescription(prescription);

        assertThat(medicine.getActiveDays()).isEqualTo(GetRecentActiveDates());
    }

    @Test
    public void getActiveDates_WithRecentCompletedPrescriptions_ReturnsDatesUpToToday(){
        var lastWeek = today.minusDays(7);
        var prescription = new Prescription(lastWeek, 2);
        medicine.addPrescription(prescription);

        var expectedDates = new ArrayList<LocalDate>();
        expectedDates.add(lastWeek);
        expectedDates.add(lastWeek.plusDays(1));

        assertThat(medicine.getActiveDays()).isEqualTo(expectedDates);
    }

    @Test
    public void getActiveDates_WithFuturePrescriptions_ReturnsEmptyList(){
        var tomorrow = today.plusDays(1);
        var prescription = new Prescription(tomorrow, 2);
        medicine.addPrescription(prescription);

        var expectedDates = new ArrayList<LocalDate>();

        assertThat(medicine.getActiveDays()).isEqualTo(expectedDates);
    }

    @Test
    public void getActiveDates_WithPrescriptionsBeforeDefaultLookback_ReturnsEmptyList(){
        var lastQuarter = today.minusDays(93);
        var prescription = new Prescription(lastQuarter, 2);
        medicine.addPrescription(prescription);

        var expectedDates = new ArrayList<LocalDate>();

        assertThat(medicine.getActiveDays()).isEqualTo(expectedDates);
    }

    @Test
    public void getActiveDates_WithPrescriptionStartBeforeDefaultLookback_ReturnsPartialList(){
        var lastQuarter = today.minusDays(90);
        var beforeLastQuarter = lastQuarter.minusDays(3);
        var prescription = new Prescription(beforeLastQuarter, 5);
        medicine.addPrescription(prescription);

        var expectedDates = new ArrayList<LocalDate>();
        expectedDates.add(lastQuarter);
        expectedDates.add(lastQuarter.plusDays(1));

        assertThat(medicine.getActiveDays()).isEqualTo(expectedDates);
    }

    @Test
    public void getActiveDates_WithLookbackOverride_ReturnsEmptyList(){
        var lastWeek = today.minusDays(7);
        var prescription = new Prescription(lastWeek, 2);
        medicine.addPrescription(prescription);

        assertThat(medicine.getActiveDays(3)).isEqualTo(new ArrayList<LocalDate>());
    }

    @Test
    public void getActiveDates_WithOverridePrescriptionsBeforeDefaultLookback_ReturnsListt(){
        var lastQuarter = today.minusDays(93);
        var prescription = new Prescription(lastQuarter, 2);
        medicine.addPrescription(prescription);

        var expectedDates = new ArrayList<LocalDate>();
        expectedDates.add(lastQuarter);
        expectedDates.add(lastQuarter.plusDays(1));

        assertThat(medicine.getActiveDays(120)).isEqualTo(expectedDates);
    }



}
