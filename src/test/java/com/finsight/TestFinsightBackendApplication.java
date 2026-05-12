package com.finsight;

import org.springframework.boot.SpringApplication;

public class TestFinsightBackendApplication {

    public static void main(String[] args) {
        SpringApplication.from(FinsightBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
