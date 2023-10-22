package com.gringotts.technicaltask.web;

import com.gringotts.technicaltask.service.counter.SuccessfulRequestCounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/counter")
@RequiredArgsConstructor
class SuccessfulRequestCounterController {

    private final SuccessfulRequestCounterService service;

    @GetMapping
    public ResponseEntity<Integer> getCountOfSuccessfulRequests() {
        return ResponseEntity.ok(service.getCurrentCount());
    }
}
