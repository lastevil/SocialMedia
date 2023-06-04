package com.socialmedia.example.services;

import com.socialmedia.example.entities.Role;
import com.socialmedia.example.exception.ResourceNotFoundException;
import com.socialmedia.example.exception.ValidationException;
import com.socialmedia.example.repositorys.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Role findRoleByName(String roleName) {
        if (roleName == null) {
            List<String> error = new ArrayList<>();
            error.add("Invalid name of role");
            throw new ValidationException(error);
        }
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Role '%s' not found", roleName)));
    }
}
