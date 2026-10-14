package com.datapilot;


import com.datapilot.analytics.SalesAnalyticsController;
import com.datapilot.analytics.SalesAnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SalesAnalyticsController.class)
class SalesAnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SalesAnalyticsService salesAnalyticsService;

    @Test
    public void MockRequestBuilders() throws Exception{

      mockMvc.perform(
              get("/api/analytics/sales")
                      .param(
                              "from", "2026-10-01"
                      ).param(
                              "to", "2026-11-01"
                      )
      ).andExpect(status().isOk());
    }

    @Test
    public void MockRequestEqual() throws Exception{

        mockMvc.perform(
                get("/api/analytics/sales")
                        .param(
                                "from", "2026-10-01"
                        ).param(
                                "to", "2026-10-01"
                        )
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void MockRequestTOisBefore() throws Exception{
        mockMvc.perform(
                get("/api/analytics/sales")
                        .param(
                                "from", "2026-11-01"
                        ).param(
                                "to", "2026-10-01"
                        )
        ).andExpect(status().isBadRequest());
    }
}

