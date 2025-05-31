package org.drk.portfolio.dto;

import lombok.Data;
import java.util.Map;

@Data
public class FundDetailDTO {
    private String fundCode;
    private String fundName;
    private Map<String, Double> returns;
}