package com.gringotts.technicaltask.service.counter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SuccessfulRequestCounterServiceImplTest {

    private SuccessfulRequestCounterServiceImpl service;
    @BeforeEach
    void setUp() {
        service = new SuccessfulRequestCounterServiceImpl();
    }

    @Test
    void increment_whenInvoked_incrementsCounterValue() {
        service.increment();

        assertThat(service.counter).hasValue(1);
    }

    @Test
    void getCurrentCount_whenInvoked_returnsCorrectValue() {
        service.counter.set(12);

        assertThat(service.getCurrentCount()).isEqualTo(12);
    }
}