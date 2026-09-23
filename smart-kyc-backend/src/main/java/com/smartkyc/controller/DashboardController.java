package com.smartkyc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartkyc.dto.DashboardResponse;
import com.smartkyc.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = {
	    "http://localhost:5173",
	    "http://localhost:5176"
	})
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardResponse getDashboardData() {
        return dashboardService.getDashboardData();
    }
}
