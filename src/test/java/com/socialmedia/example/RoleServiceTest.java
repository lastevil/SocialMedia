package com.socialmedia.example;

import com.socialmedia.example.entities.Role;
import com.socialmedia.example.exception.ResourceNotFoundException;
import com.socialmedia.example.exception.ValidationException;
import com.socialmedia.example.repositorys.RoleRepository;
import com.socialmedia.example.services.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;


@SpringBootTest(classes = RoleService.class)
class RoleServiceTest {
    @Autowired
    private RoleService service;
    @MockBean
    private RoleRepository repository;

    @Test
    void findRoleByName() {
        Optional<Role> role = Optional.of(new Role());
        role.get().setName("USER_ROLE");
        role.get().setId(1L);
        role.get().setCreatedAt(LocalDateTime.now());
        role.get().setUpdatedAt(LocalDateTime.now());

        Optional<Role> emptyRole = Optional.empty();

        Mockito.doReturn(role).when(repository).findByName("USER_ROLE");
        Mockito.doReturn(emptyRole).when(repository).findByName("TEST_ROLE");

        Optional<Role> resultRole = repository.findByName("USER_ROLE");
        Assertions.assertEquals(role.get().getName(), resultRole.get().getName());

        ValidationException thrown1 = Assertions.assertThrows(ValidationException.class,
                () -> service.findRoleByName(null));
        Assertions.assertEquals("Invalid name of role", thrown1.getMessage());

        ResourceNotFoundException thrown2 = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.findRoleByName("TEST_ROLE"));
        Assertions.assertEquals("Role 'TEST_ROLE' not found", thrown2.getMessage());

    }
}
