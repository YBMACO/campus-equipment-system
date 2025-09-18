package edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.service;

import org.springframework.stereotype.Component;

@Component
public class DailyPenaltyStrategy implements PenaltyStrategy {
    private final long penaltyPerDay = 50;

    @Override
    public long calculatePenalty(long daysLate) {
        return daysLate * penaltyPerDay;
    }
}
