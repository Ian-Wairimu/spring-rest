package com.wairimuian.springrest.configuration;

import com.wairimuian.springrest.entity.Employee;
import com.wairimuian.springrest.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadItems {
    private static final Logger log = LoggerFactory.getLogger(LoadItems.class);
    @Bean
    public CommandLineRunner addEmployee(EmployeeRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Employee(
                    "ian moon",
                    "software engineer"
            )));
            log.info("Preloading " + repository.save(new Employee(
                    "Kelvin Kimani",
                    "Devops engineer"
            )));
        };
    }
}
