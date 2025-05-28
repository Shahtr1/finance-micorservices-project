package com.finance.user.config;

import com.finance.user.entity.Role;
import com.finance.user.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        List<String> defaultRoles = Arrays.asList("SUPPLIER", "BUYER", "ADMIN", "FINANCIER");

        for (String roleName : defaultRoles) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName(roleName);
                        return roleRepository.save(newRole);
                    });
        }

        System.out.println("✔ Default roles ensured in DB.");
    }
}
