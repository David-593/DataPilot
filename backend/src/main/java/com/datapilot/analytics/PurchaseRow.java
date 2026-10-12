package com.datapilot.analytics;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "compras")
public class PurchaseRow {
    @Id
    private Long id;

    protected PurchaseRow() {}
}
