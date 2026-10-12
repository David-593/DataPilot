package com.datapilot.analytics;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "http://localhost:4200")
public class SalesAnalyticsController {
    private final SalesAnalyticsService service;

    public SalesAnalyticsController(SalesAnalyticsService service) {
        this.service = service;
    }

    @GetMapping("/sales")
    public SalesSummary sales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if(!from.isBefore(to)){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "from debe ser anteior a to"
            );
        }
        return service.summarize(from, to);
    }
}
