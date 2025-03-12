package com.ticketsalesapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class TicketSalesApp implements CommandLineRunner {

    private final ConsoleApp consoleApp;

    public TicketSalesApp(ConsoleApp consoleApp) {
        this.consoleApp = consoleApp;
    }

    public static void main(String[] args) {
        SpringApplication.run(TicketSalesApp.class, args);
    }

    @Override
    public void run(String... args) {
        consoleApp.run();
    }
}
