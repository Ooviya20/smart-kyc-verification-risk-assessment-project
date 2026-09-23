package com.smartkyc.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.*;

import com.smartkyc.entity.PepScreening;
import com.smartkyc.service.PepScreeningService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/pep")
public class PepScreeningController {

    private final PepScreeningService pepScreeningService;

    public PepScreeningController(
            PepScreeningService pepScreeningService) {

        this.pepScreeningService = pepScreeningService;
    }

    @PostMapping("/{customerId}")
    public PepScreening screenCustomer(
            @PathVariable Long customerId) {

        return pepScreeningService.screenCustomer(customerId);
    }

    @GetMapping
    public List<PepScreening> getAllScreenings() {

        return pepScreeningService.getAllScreenings();
    }
}