package org.drk.portfolio.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tefas.import")
@Getter
@Setter
public class TefasImportConfig {
    private String pdfPath;
}