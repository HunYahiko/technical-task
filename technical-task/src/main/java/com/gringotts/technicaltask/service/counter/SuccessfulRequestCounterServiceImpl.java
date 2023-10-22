package com.gringotts.technicaltask.service.counter;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
class SuccessfulRequestCounterServiceImpl implements SuccessfulRequestCounterService {

    final AtomicInteger counter;

    public SuccessfulRequestCounterServiceImpl() {
        counter = new AtomicInteger();
    }

    @Override
    public void increment() {
        counter.incrementAndGet();
    }

    @Override
    public Integer getCurrentCount() {
        return counter.get();
    }
}
