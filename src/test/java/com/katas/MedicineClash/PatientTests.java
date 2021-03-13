package com.katas.MedicineClash;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.AdditionalAnswers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientTests {
    private final LocalDate today = LocalDate.now();
    private final LocalDate yesterday = today.minusDays(1);
    private final LocalDate lastWeek = today.minusDays(7);
    private final String NonClashingMed = "bar";
    private String ClashingMed1= "foo";
    private String ClashingMed2= "baz";

    private Patient createClashingPatient() {
        return createClashingPatient(lastWeek);
    }

    private Patient createClashingPatient(LocalDate dispenseDate) {
        var patient = new Patient();
        var rx = new Prescription(dispenseDate, 2);
        var medicine = new Medicine(ClashingMed1);
        medicine.addPrescription(rx);
        patient.addMedicine(medicine);
        var medicine2 = new Medicine(ClashingMed2);
        medicine2.addPrescription(rx);
        patient.addMedicine(medicine2);
        return patient;
    }

    private Collection<String> CreateClashingList() {
        Collection<String> clashingMeds = new ArrayList<>();
        clashingMeds.add(ClashingMed1);
        clashingMeds.add(ClashingMed2);
        return clashingMeds;
    }

    @Test
    public void Clash_WhenPassedEmptyList_ThrowsException() throws InvalidParameterException {
        Patient patient = createClashingPatient();

        Collection<String> clashingMeds = new ArrayList<>();

        assertThatThrownBy(() -> patient.Clash(clashingMeds))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("'clashingMeds' must have at least 2 medicines.");
    }

    @Test
    public void Clash_WhenPassedOneMed_ThrowsException() throws InvalidParameterException {
        Patient patient = createClashingPatient();

        Collection<String> clashingMeds = new ArrayList<>();
        clashingMeds.add(NonClashingMed);

        assertThatThrownBy(() -> patient.Clash(clashingMeds))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("'clashingMeds' must have at least 2 medicines.");
    }

    @Test
    public void Clash_WhenPatientHasFewerThenTwoMeds_ReturnsEmpty()  {
        var patient = new Patient();
        var medicine1 = new Medicine(ClashingMed1);
        patient.addMedicine(medicine1);

        Collection<String> clashingMeds = CreateClashingList();

        assertThat(patient.Clash(clashingMeds)).isEqualTo(new ArrayList<LocalDate>());
    }

    @Test
    public void Clash_WhenClashIsOlderThanDefaultLookback_ReturnsEmpty()  {
        var beyondLastQuarter = today.minusDays(97);
        Patient patient = createClashingPatient(beyondLastQuarter);

        Collection<String> clashingMeds = CreateClashingList();

        assertThat(patient.Clash(clashingMeds)).isEqualTo(new ArrayList<LocalDate>());
    }

    @Test
    public void Clash_WhenClashWithinDefaultLookback_ReturnsListOfClashingDates()  {
        var lastWeek = today.minusDays(7);
        Patient patient = createClashingPatient(lastWeek);

        Collection<String> clashingMeds = CreateClashingList();

        var expectedDates = new ArrayList<>();
        expectedDates.add(lastWeek);
        expectedDates.add(lastWeek.plusDays(1));

        assertThat(patient.Clash(clashingMeds)).isEqualTo(expectedDates);
    }

    @Test
    public void Clash_WhenMedicinesClashButNoOverlap_ReturnsEmptyList()  {
        var lastWeek = today.minusDays(7);
        var twoWeeksAgo = today.minusDays(14);

        var patient = new Patient();

        var rxLastWeek = new Prescription(lastWeek, 2);
        var medicine = new Medicine(ClashingMed1);
        medicine.addPrescription(rxLastWeek);
        patient.addMedicine(medicine);

        var rxTwoWeeksAgo = new Prescription(twoWeeksAgo, 2);
        var medicine2 = new Medicine(ClashingMed2);
        medicine2.addPrescription(rxTwoWeeksAgo);
        patient.addMedicine(medicine2);

        Collection<String> clashingMeds = CreateClashingList();

        var expectedDates = new ArrayList<>();

        assertThat(patient.Clash(clashingMeds)).isEqualTo(expectedDates);
    }

    @Test
    public void Clash_WhenMedicinesClashAndPartiallyOverlap_ReturnsOverlap()  {
        var lastWeek = today.minusDays(7);
        var twoWeeksAgo = today.minusDays(14);

        var patient = new Patient();

        var rxLastWeek = new Prescription(lastWeek, 3);
        var medicine = new Medicine(ClashingMed1);
        medicine.addPrescription(rxLastWeek);
        patient.addMedicine(medicine);

        var rxTwoWeeksAgo = new Prescription(twoWeeksAgo, 8);
        var medicine2 = new Medicine(ClashingMed2);
        medicine2.addPrescription(rxTwoWeeksAgo);
        patient.addMedicine(medicine2);

        Collection<String> clashingMeds = CreateClashingList();

        var expectedDates = new ArrayList<>();
        expectedDates.add(lastWeek);

        assertThat(patient.Clash(clashingMeds)).isEqualTo(expectedDates);
    }


}