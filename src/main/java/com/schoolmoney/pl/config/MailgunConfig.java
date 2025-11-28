package com.schoolmoney.pl.config;

import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import feign.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailgunConfig {

    @Value("${mailgun.api.domain}")
    private String DOMAIN;

    @Value("${mailgun.api.private_key}")
    private String API_KEY;

    @Bean
    public MailgunMessagesApi mailgunMessagesApi() {
        return MailgunClient.config(DOMAIN, API_KEY)
                            .logLevel(Logger.Level.NONE)
                            .createApi(MailgunMessagesApi.class);
    }
}
