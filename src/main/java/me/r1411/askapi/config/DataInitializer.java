package me.r1411.askapi.config;

import me.r1411.askapi.model.Role;
import me.r1411.askapi.model.User;
import me.r1411.askapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;

    @Autowired
    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        String createDefaultAdminVariable = System.getenv("CREATE_DEFAULT_ADMIN_ACCOUNT");

        if (createDefaultAdminVariable == null)
            return;

        boolean createDefaultAdmin = Boolean.parseBoolean(createDefaultAdminVariable);

        if (!createDefaultAdmin)
            return;

        if (userService.countAll() != 0)
            return;

        String adminUsername = System.getenv("DEFAULT_ADMIN_USERNAME");
        String adminPassword = System.getenv("DEFAULT_ADMIN_PASSWORD");

        User adminUser = new User(adminUsername, adminPassword);
        userService.register(adminUser, Role.ADMIN);
    }
}
