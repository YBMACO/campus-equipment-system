package edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.service;

public interface PenaltyStrategy {
    long calculatePenalty(long daysLate);
}
