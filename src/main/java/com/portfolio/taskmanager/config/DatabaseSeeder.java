package com.portfolio.taskmanager.config;

import com.portfolio.taskmanager.application.service.UserService;
import com.portfolio.taskmanager.infrastructure.persistence.repository.UserSpringRepository;
import com.portfolio.taskmanager.infrastructure.persistence.repository.TaskSpringRepository;
import com.portfolio.taskmanager.infrastructure.persistence.entity.TaskEntity;
import com.portfolio.taskmanager.domain.model.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DatabaseSeeder {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    @Bean
    CommandLineRunner seedData(UserService userService, UserSpringRepository userRepo, TaskSpringRepository taskRepo) {
        return args -> {
            seedTestUser(userService, userRepo);
            seedExampleTasks(taskRepo);
        };
    }

    @Transactional
    void seedTestUser(UserService userService, UserSpringRepository userRepo) {
        final String username = "teste"; 
        final String password = "12345"; 
        if (userRepo.existsByUsername(username)) {
            log.info("Seed: user '{}' already exists - skipping", username);
            return;
        }
        userService.register(username, password, "USER");
        log.info("Seed: created test user '{}' with password '12345' (BCrypt-hashed)", username);
    }

    @Transactional
    void seedExampleTasks(TaskSpringRepository taskRepo) {
        if (taskRepo.count() > 0) {
            log.info("Seed: tasks already exist - skipping");
            return;
        }

        String[] titles = new String[]{
                "Configurar ambiente de desenvolvimento",
                "Criar endpoints de autenticação",
                "Documentar API com Swagger"
        };
        String[] descriptions = new String[]{
                "Instalar JDK 21, Maven, Docker e configurar IDE.",
                "Implementar registro, login e emissão de JWT.",
                "Expor Swagger UI e revisar modelos de request/response."
        };

        for (int i = 0; i < titles.length; i++) {
            TaskEntity t = new TaskEntity();
            t.setId(java.util.UUID.randomUUID().toString());
            t.setTitle(titles[i]);
            t.setDescription(descriptions[i]);
            TaskStatus status = switch (i) {
                case 0 -> TaskStatus.PENDING;
                case 1 -> TaskStatus.IN_PROGRESS;
                case 2 -> TaskStatus.COMPLETED;
                default -> TaskStatus.PENDING;
            };
            t.setStatus(status);
            taskRepo.save(t);
        }
        log.info("Seed: created example tasks (3 items)");
    }
}
