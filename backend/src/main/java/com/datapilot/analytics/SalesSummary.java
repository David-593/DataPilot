package com.datapilot.analytics;

import java.math.BigDecimal;

public record SalesSummary(long purchaseCount, BigDecimal totalSales, long unitsSold) {}
