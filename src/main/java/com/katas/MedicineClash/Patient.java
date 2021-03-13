package com.katas.MedicineClash;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.cglib.core.Local;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;

public class Patient {

    private Collection<Medicine> medicines = new ArrayList<>();

    public void addMedicine(Medicine medicine) {
        medicines.add(medicine);
    }

    public Collection<LocalDate> Clash(Collection<String> clashingMeds) throws InvalidParameterException {
        var defaultReturn = new ArrayList<LocalDate>();
        if (medicines.size() < 2) return defaultReturn;
        if (clashingMeds.size() < 2)  throw new InvalidParameterException("'clashingMeds' must have at least 2 medicines.");

        Hashtable<LocalDate, Integer> clashingDates = new Hashtable<>();

        for (String clashingMed : clashingMeds) {
            var med = getMedByName(clashingMed);
            if (med == null) return defaultReturn;
            var activeDays = med.getActiveDays();
            for (LocalDate activeDay : activeDays) {
                var countTracker = 0;
                if (clashingDates.containsKey(activeDay)){
                    countTracker = clashingDates.get(activeDay);
                }
                countTracker++;
                clashingDates.put(activeDay, countTracker);
            }
        }

        //assembly union of cached dates
        Collection<LocalDate> finalClashingDates = new ArrayList<>();

        clashingDates.forEach((key, value) -> {
            if (value == clashingMeds.size()){
                finalClashingDates.add(key);
            }
        });

        return finalClashingDates;
    }

    private Medicine getMedByName(String medicineName) {
        for (Medicine med : medicines) {
            if (med.getName().equals(medicineName)) {
                return med;
            }
        }
        return null;
    }
}