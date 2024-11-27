package com.wowo.wowo.service;

import com.wowo.wowo.model.RolePermission;
import com.wowo.wowo.repository.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolePermissionService {
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    public RolePermission createRolePermission(RolePermission rolePermission) {
        return rolePermissionRepository.save(rolePermission);
    }

    public Optional<RolePermission> findById(Long id) {
        return rolePermissionRepository.findById(id);
    }

    public List<RolePermission> findAll() {
        return rolePermissionRepository.findAll();
    }

    public void deleteRolePermission(Long id) {
        if (!rolePermissionRepository.existsById(id)) {
            throw new RuntimeException("Vai trò quyền không tồn tại");
        }
        rolePermissionRepository.deleteById(id);
    }
}