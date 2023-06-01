package com.socialmedia.example.services;

import com.socialmedia.example.converters.UserConverter;
import com.socialmedia.example.dto.UserRegDto;
import com.socialmedia.example.entities.Role;
import com.socialmedia.example.entities.User;
import com.socialmedia.example.exception.AppAuthenticationException;
import com.socialmedia.example.exception.ResourceExistsException;
import com.socialmedia.example.exception.validators.NewUserValidator;
import com.socialmedia.example.repositorys.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserConverter userConverter;

    @Transactional
    public UserDetails loadUserByUsername(String login) {
        String[] masLogin = login.split("@");
        User user;
        if (masLogin.length == 2 && masLogin[1].contains(".")) {
            user = userRepository.findByEmail(login.toLowerCase())
                    .orElseThrow(() -> (new AppAuthenticationException("Incorrect email")));
        } else {
            user = userRepository.findByUsername(login.toLowerCase())
                    .orElseThrow(() -> (new AppAuthenticationException("Incorrect username")));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private List<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return  roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    @Transactional
    public void tryNewUserAdd(UserRegDto newUser) {
        NewUserValidator.validate(newUser);
        if (Boolean.TRUE.equals(userRepository.existsUserByUsername(newUser.getUsername())))
            throw new ResourceExistsException("This user already exists");
        User user = userConverter.fromRegDto(newUser);
        Role roleUser = roleService.findRoleByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);
        user.setRoles(userRoles);
        userRepository.save(user);
    }
}
