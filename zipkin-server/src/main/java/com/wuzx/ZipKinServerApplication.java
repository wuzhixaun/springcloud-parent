package com.wuzx;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin2.server.internal.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
public class ZipKinServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZipKinServerApplication.class, args);
    }
}
