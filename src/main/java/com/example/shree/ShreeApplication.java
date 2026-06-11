package com.example.shree;

import com.example.shree.entity.UserEntity;
import com.example.shree.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ShreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShreeApplication.class, args);

	}
    @Bean
    CommandLineRunner initUsers(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        return args -> {

            UserEntity user = new UserEntity();
            user.setUsername("shree");
            user.setPassword(passwordEncoder.encode("shree123"));
            user.setRole("ROLE_USER");

            userRepo.save(user);

            System.out.println("User inserted successfully");
        };
    }

}
