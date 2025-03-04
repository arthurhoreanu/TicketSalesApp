package main.java.com.ticketsalesapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TicketSalesApp {

    public static void main(String[] args) {
        SpringApplication.run(TicketSalesApp.class, args);
    }

    @Bean
    public CommandLineRunner run(ApplicationContext context) {
        return args -> {
            ConsoleApp consoleApp = context.getBean(ConsoleApp.class);
            ConsoleApp.run();
        };
    }
}