package com.yousheng.app1.casclient1;

import net.unicon.cas.client.configuration.EnableCasClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCasClient
public class CasClient1Application {

    public static void main(String[] args) {
        SpringApplication.run(CasClient1Application.class, args);
    }

}
