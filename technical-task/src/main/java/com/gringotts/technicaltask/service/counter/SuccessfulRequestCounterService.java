package com.gringotts.technicaltask.service.counter;

public interface SuccessfulRequestCounterService {
    void increment();
    Integer getCurrentCount();
}
