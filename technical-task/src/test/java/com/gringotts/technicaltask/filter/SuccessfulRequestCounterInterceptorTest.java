package com.gringotts.technicaltask.filter;

import com.gringotts.technicaltask.service.counter.SuccessfulRequestCounterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SuccessfulRequestCounterInterceptorTest {

    @Mock
    private SuccessfulRequestCounterService service;

    private SuccessfulRequestCounterInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new SuccessfulRequestCounterInterceptor(service);
    }

    @Test
    void afterCompletion_whenRequestIsNotSuccessful_doesNotIncrementCounter() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final Object handle = mock(Object.class);
        final Exception ex = mock(Exception.class);

        when(response.getStatus()).thenReturn(HttpStatus.BAD_REQUEST.value());

        interceptor.afterCompletion(request, response, handle, ex);

        verify(service, never()).increment();
    }

    @Test
    void afterCompletion_whenRequestIsSuccessful_incrementsCounter() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final Object handle = mock(Object.class);
        final Exception ex = mock(Exception.class);

        when(response.getStatus()).thenReturn(HttpStatus.CREATED.value());

        interceptor.afterCompletion(request, response, handle, ex);

        verify(service).increment();
    }
}