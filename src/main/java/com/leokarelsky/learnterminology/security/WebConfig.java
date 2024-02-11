package com.leokarelsky.learnterminology.security;

import com.leokarelsky.learnterminology.model.Termin;
import com.leokarelsky.learnterminology.model.TerminCollection;
import com.leokarelsky.learnterminology.model.User;
import com.leokarelsky.learnterminology.repository.CollectionRepository;
import com.leokarelsky.learnterminology.repository.RoleRepository;
import com.leokarelsky.learnterminology.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/form-login");
    }

    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepo, RoleRepository roleRepo, CollectionRepository collectionRepository, PasswordEncoder encoder) {
        return args -> {

            TerminCollection collection1 = new TerminCollection();
            collection1.setId(55L);
            collection1.setTitle("Leo's Collection");
            collection1.setCreatedAt(LocalDateTime.now());

            TerminCollection collection2 = new TerminCollection();
            collection2.setId(56);
            collection2.setTitle("Cris's Collection");
            collection2.setCreatedAt(LocalDateTime.now());

            User user = new User();
            user.setFirstName("Leo");
            user.setLastName("Messi");
            user.setEmail("user@gmail.com");
            user.setPassword(encoder.encode("password"));
            user.setRole(roleRepo.getReferenceById(2L));
            user.setMyCollections(List.of(collection1));
            userRepo.save(user);

            User admin = new User();
            admin.setFirstName("Cristiano");
            admin.setLastName("Ronaldo");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(encoder.encode("password"));
            admin.setRole(roleRepo.getReferenceById(1L));
            admin.setMyCollections(List.of(collection2));
            userRepo.save(admin);

            collection1.setOwner(user);
            collectionRepository.save(collection1);
            collection2.setOwner(admin);
            collectionRepository.save(collection2);
        };
    }
}
