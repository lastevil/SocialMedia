package com.socialmedia.example;

import com.socialmedia.example.configs.AppConfig;
import com.socialmedia.example.converters.UserConverter;
import com.socialmedia.example.dto.requests.UserRegDto;
import com.socialmedia.example.dto.responses.UserResponseDto;
import com.socialmedia.example.entities.Role;
import com.socialmedia.example.entities.User;
import com.socialmedia.example.exception.AppAuthenticationException;
import com.socialmedia.example.exception.ResourceExistsException;
import com.socialmedia.example.exception.ResourceNotFoundException;
import com.socialmedia.example.exception.ValidationException;
import com.socialmedia.example.exception.validators.UserValidator;
import com.socialmedia.example.repositorys.UserRepository;
import com.socialmedia.example.services.RoleService;
import com.socialmedia.example.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = {UserService.class, UserConverter.class, UserValidator.class, AppConfig.class})
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserConverter converter;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleService roleService;

    private Optional<User> testUser1;
    private Optional<User> testUser2;

    private UUID id;

    @BeforeEach
    public void initEach() {
        id =UUID.randomUUID();
        Role roleUser = new Role();
        roleUser.setId(1L);
        roleUser.setName("ROLE_USER");
        roleUser.setCreatedAt(LocalDateTime.now());
        roleUser.setUpdatedAt(LocalDateTime.now());
        Mockito.doReturn(roleUser).when(roleService).findRoleByName("ROLE_USER");

        Role roleTestUser = roleService.findRoleByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleTestUser);

        testUser1 = Optional.of(new User());
        testUser1.get().setId(id);
        testUser1.get().setRoles(userRoles);
        testUser1.get().setUsername("testuser");
        testUser1.get().setEmail("test@test.ru");
        testUser1.get().setPassword("Qwe123");

        testUser2 = Optional.empty();
    }

    @Test
    void loadUserByUsernameTest() {
        Mockito.doReturn(testUser1).when(userRepository).findByEmail("test@test.ru");
        Mockito.doReturn(testUser2).when(userRepository).findByEmail("user@mail.ru");
        Mockito.doReturn(testUser1).when(userRepository).findByUsername("testUser".toLowerCase());
        Mockito.doReturn(testUser2).when(userRepository).findByUsername("user".toLowerCase());

        UserDetails resultUser;
        resultUser = userService.loadUserByUsername("test@test.ru");
        Assertions.assertEquals(resultUser.getUsername(), testUser1.get().getUsername());

        UserDetails resultUser1;
        resultUser1 = userService.loadUserByUsername("testUser");
        Assertions.assertEquals(resultUser1.getUsername(), testUser1.get().getUsername());

        AppAuthenticationException thrown1 = Assertions.assertThrows(AppAuthenticationException.class,
                () -> userService.loadUserByUsername("user@mail.ru"));
        Assertions.assertEquals("Incorrect email", thrown1.getMessage());

        AppAuthenticationException thrown2 = Assertions.assertThrows(AppAuthenticationException.class,
                () -> userService.loadUserByUsername("user"));
        Assertions.assertEquals("Incorrect username", thrown2.getMessage());
    }

    @Test
    void tryNewUserAddTest() {
        when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        UserRegDto newTestUser = new UserRegDto("", "test@test.ru", "Qwe123");

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.tryNewUserAdd(newTestUser));
        Assertions.assertEquals("Username must be not empty", exception.getMessage());

        newTestUser.setUsername("testUser");
        newTestUser.setEmail("");
        exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.tryNewUserAdd(newTestUser));
        Assertions.assertEquals("Email must not be empty", exception.getMessage());

        newTestUser.setEmail("asdasd");
        exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.tryNewUserAdd(newTestUser));
        Assertions.assertEquals("Email is not valid", exception.getMessage());

        newTestUser.setEmail("test@test.ru");
        newTestUser.setPassword("");
        exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.tryNewUserAdd(newTestUser));
        Assertions.assertEquals("Password must not be empty", exception.getMessage());

        newTestUser.setUsername("testUser");
        newTestUser.setPassword("Qwe123");
        Mockito.doReturn(true).when(userRepository).existsUserByUsername("testUser");
        ResourceExistsException exception1 = Assertions.assertThrows(ResourceExistsException.class,
                () -> userService.tryNewUserAdd(newTestUser));
        Assertions.assertEquals("This username is busy", exception1.getMessage());

        Mockito.doReturn(false).when(userRepository).existsUserByUsername("testUser");
        Mockito.doReturn(true).when(userRepository).existsByEmail("test@test.ru");
        exception1 = Assertions.assertThrows(ResourceExistsException.class,
                () -> userService.tryNewUserAdd(newTestUser));
        Assertions.assertEquals("This email is busy on another account", exception1.getMessage());
    }

    @Test
    void findUserByUsernameTest() {
        Mockito.doReturn(testUser1).when(userRepository).findByUsername("testUser");
        Mockito.doReturn(testUser2).when(userRepository).findByUsername("test");
        User user = userService.findUserByUsername("testUser");
        Assertions.assertEquals(testUser1.get().getUsername(), user.getUsername());


        ResourceNotFoundException thrown1 = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.findUserByUsername("test"));
        Assertions.assertEquals("User Not Found", thrown1.getMessage());
    }

    @Test
    void findUserByUserIdTest(){
        UUID randId = UUID.randomUUID();
        Mockito.doReturn(testUser1).when(userRepository).findById(id);
        Mockito.doReturn(testUser2).when(userRepository).findById(randId);

        User user = userService.findUserByUserId(id);
        Assertions.assertEquals(user.getUsername(), testUser1.get().getUsername());

        ResourceNotFoundException thrown1 = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.findUserByUserId(randId));
        Assertions.assertEquals("User Not Found", thrown1.getMessage());

        ValidationException thrown2 = Assertions.assertThrows(ValidationException.class,
                () -> userService.findUserByUserId(null));
        Assertions.assertEquals("Wrong user id", thrown2.getMessage());
    }

    @Test
    void getAllUsersTest(){
        Role roleTestUser = roleService.findRoleByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleTestUser);
        User testUser3 = new User();
        testUser3.setId(UUID.randomUUID());
        testUser3.setRoles(userRoles);
        testUser3.setUsername("testuser3");
        testUser3.setEmail("test@test1.ru");
        testUser3.setPassword("Qwe123");

        User testUser4 = testUser1.get();

        List<User> userList = new ArrayList<>();
        userList.add(testUser4);
        userList.add(testUser3);

        Mockito.doReturn(userList).when(userRepository).findAll();

        List<UserResponseDto> responseDtoList = userService.getAllUsers();
        List<UserResponseDto> responseDtoList2 = userList.stream().map(converter::fromEntityToRespDto).collect(Collectors.toList());

        Assertions.assertEquals(2, responseDtoList.size());
        Assertions.assertEquals(responseDtoList.get(0).getClass(), UserResponseDto.class);
        Assertions.assertEquals(responseDtoList,responseDtoList2);

    }
}
