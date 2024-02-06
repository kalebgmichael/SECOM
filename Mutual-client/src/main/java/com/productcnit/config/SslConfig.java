package com.productcnit.config;


import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.tcp.SslProvider;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

@Slf4j
@Configuration
public class SslConfig
{
    @Value("${suriya.client.ssl.trust-store}")
    private String trustStorePath;

    @Value("${suriya.client.ssl.trust-store-password}")
    private String trustStorePassword;


    @Value("${suriya.client.ssl.key-store}")
    private String keyStorePath;

    @Value("${suriya.client.ssl.key-store-password}")
    private String keyStorePassword;

    public SslContext buildSslContext()
    {
        SslContext sslContext = null;
        try(FileInputStream keyStoreFileInputStream = new FileInputStream(ResourceUtils.getFile(keyStorePath));
            FileInputStream trustStoreFileInputStream = new FileInputStream(ResourceUtils.getFile(trustStorePath));
        ) {

            KeyStore keyStore = KeyStore.getInstance("jks");
            keyStore.load(keyStoreFileInputStream, keyStorePassword.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());


            KeyStore trustStore = KeyStore.getInstance("jks");
            trustStore.load(trustStoreFileInputStream, trustStorePassword.toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(trustStore);

            sslContext = SslContextBuilder.forClient()
                    .keyManager(keyManagerFactory)
                    .trustManager(trustManagerFactory)
                    .build();
        } catch (Exception exception) {
            log.error("Exception while building SSL context for reactor web client: ", exception);
        }

        return sslContext;
    }

    @Bean
    public WebClient webClient() {
        SslProvider sslProvider= SslProvider.builder()
                .sslContext(buildSslContext()).build();
        reactor.netty.http.client.HttpClient httpClient = reactor.netty.http.client.HttpClient.create()
                .secure(sslProvider);
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }
}