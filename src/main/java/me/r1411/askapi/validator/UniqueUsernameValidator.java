package me.r1411.askapi.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import me.r1411.askapi.model.User;
import me.r1411.askapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsernameConstraint, String> {
    private final UserService userService;

    @Autowired
    public UniqueUsernameValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UniqueUsernameConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> checkUser = userService.findByUsername(username);
        return checkUser.isEmpty();
    }
}
