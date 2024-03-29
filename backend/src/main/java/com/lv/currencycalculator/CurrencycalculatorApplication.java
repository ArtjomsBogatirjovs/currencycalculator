package com.lv.currencycalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CurrencycalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencycalculatorApplication.class, args);
    }

}
