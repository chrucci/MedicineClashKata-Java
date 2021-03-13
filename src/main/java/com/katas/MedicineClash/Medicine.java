package com.katas.MedicineClash;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class Medicine {

    private String _name;
    private Collection<Prescription> prescriptions = new ArrayList<>();
    private final int DefaultLookback = 90;

    public Medicine(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void addPrescription(Prescription prescription) {
        prescriptions.add(prescription);
    }

    public Collection<LocalDate> getActiveDays() {
        return getActiveDays(DefaultLookback);
    }

    public Collection<LocalDate> getActiveDays(int lookback) {
        var dates = new ArrayList<LocalDate>();
        for (Prescription rx : prescriptions) {
            //if (rx.getDate() > lookback)
            var testDate = rx.getDate();
            var endDate = testDate.plusDays(rx.getDaysSupply());
            LocalDate today = LocalDate.now();
            var lookbackDate = today.minusDays(lookback);
            while (!testDate.isAfter(today) && testDate.isBefore(endDate)){
                if (!testDate.isBefore(lookbackDate)){
                    dates.add(testDate);
                }
                testDate = testDate.plusDays(1);
            }
        }
        return dates;
    }
}
