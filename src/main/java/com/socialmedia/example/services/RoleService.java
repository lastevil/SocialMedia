package com.socialmedia.example.services;

import com.socialmedia.example.entities.Role;
import com.socialmedia.example.exception.ResourceNotFoundException;
import com.socialmedia.example.repositorys.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Role findRoleByName(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new ResourceNotFoundException(String.format("Role '%s' not found", roleName));
        }
        return role;
    }
}
