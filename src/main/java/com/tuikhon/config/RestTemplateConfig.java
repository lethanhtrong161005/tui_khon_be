package com.tuikhon.config;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Global RestTemplate bean configuration for external HTTP integrations with custom timeouts.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Constructs global RestTemplate bean with 5-second connect timeout and 10-second read timeout.
     *
     * @return Configured RestTemplate instance.
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(10));
        return new RestTemplate(factory);
    }
}
