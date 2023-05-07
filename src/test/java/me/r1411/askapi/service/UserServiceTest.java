package me.r1411.askapi.service;

import me.r1411.askapi.model.Role;
import me.r1411.askapi.model.User;
import me.r1411.askapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void register_shouldCallRepository() {
        User user = mock(User.class);
        when(userRepository.save(user)).thenReturn(user);

        userService.register(user);

        verify(userRepository).save(user);
    }

    @Test
    void register_shouldSetRoleToUser() {
        User user = mock(User.class);
        when(userRepository.save(user)).thenReturn(user);

        userService.register(user);

        verify(user).setRole(Role.USER);
    }

    @Test
    void register_shouldUsePasswordEncoder() {
        User user = mock(User.class);
        when(user.getPassword()).thenReturn("123456");
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode("123456")).thenReturn("$2a$10$/bw.AmIi3.FG15lS5ZiYHOHatuOXlfsphV4R8v.JUtP7WnZPgdOie");

        userService.register(user);

        verify(user).setPassword("$2a$10$/bw.AmIi3.FG15lS5ZiYHOHatuOXlfsphV4R8v.JUtP7WnZPgdOie");
        verify(passwordEncoder).encode("123456");
    }

    @Test
    void findById_shouldCallRepository() {
        User user = mock(User.class);
        when(userRepository.findById(0)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById(0);

        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get(), user);
        verify(userRepository).findById(0);
    }

    @Test
    void findByUsername_shouldCallRepository() {
        User user = mock(User.class);
        when(userRepository.findByUsername("ivan")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername("ivan");

        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get(), user);
        verify(userRepository).findByUsername("ivan");
    }
}
