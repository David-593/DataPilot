package com.datapilot.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalesAnalyticsService {
    private final SalesAnalyticsRepository repository;

    public SalesAnalyticsService(SalesAnalyticsRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public SalesSummary summarize(LocalDate from, LocalDate toExclusive) {
        if (from == null || toExclusive == null || !from.isBefore(toExclusive)) {
            throw new IllegalArgumentException("'from' debe ser anterior a 'to'.");
        }
        List<Object[]> rows = repository.summarize(from.atStartOfDay(), toExclusive.atStartOfDay());
        Object[] row = rows.get(0);
        long purchases = ((Number) row[0]).longValue();
        BigDecimal total = row[1] == null ? BigDecimal.ZERO : (BigDecimal) row[1];
        long units = ((Number) row[2]).longValue();
        return new SalesSummary(purchases, total, units);
    }
}
