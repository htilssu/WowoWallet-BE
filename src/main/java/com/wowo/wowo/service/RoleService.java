package com.wowo.wowo.service;

import com.wowo.wowo.model.Role;
import com.wowo.wowo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Optional<Role> findById(Integer id) {
        return roleRepository.findById(id);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public void deleteRole(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Vai trò không tồn tại");
        }
        roleRepository.deleteById(id);
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findAll().stream()
                .filter(role -> role.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}