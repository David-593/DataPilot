package com.datapilot.analytics;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SalesAnalyticsRepository extends Repository<PurchaseRow, Long> {
    @Query(value = """
        SELECT
            COUNT(DISTINCT c.id) AS purchase_count,
            COALESCE(SUM(dc.cantidad * (dc.precio_unitario - dc.descuento_unitario)), 0) AS total_sales,
            COALESCE(SUM(dc.cantidad), 0) AS units_sold
        FROM compras c
        JOIN detalle_compra dc ON dc.compra_id = c.id
        WHERE c.fecha >= :fromDate
          AND c.fecha < :toDate
          AND c.estado = 'COMPLETADA'
        """, nativeQuery = true)
    List<Object[]> summarize(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
}
