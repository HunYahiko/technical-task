package com.gringotts.technicaltask.filter;

import com.gringotts.technicaltask.service.counter.SuccessfulRequestCounterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
class SuccessfulRequestCounterInterceptor implements HandlerInterceptor {

    private final SuccessfulRequestCounterService service;

    /**
     It depends on what exactly completed means.
     If by completed you mean successful, then 2xx is enough.
     If completed request is any request, you can simply remove the if check.
     **/
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        if (HttpStatus.valueOf(response.getStatus()).is2xxSuccessful()) {
            service.increment();
        }
    }
}
