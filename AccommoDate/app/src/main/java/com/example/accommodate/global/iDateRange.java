package com.example.accommodate.global;

import java.time.LocalDate;

public interface iDateRange
{
    LocalDate getStartDate();

    LocalDate getEndDate();

    boolean isAcceptedDateRange(DateRange range);

    int compareTo(DateRange range);
}