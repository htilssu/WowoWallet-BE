package com.wowo.wowo.service;

import com.wowo.wowo.model.Permission;
import com.wowo.wowo.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public void deletePermission(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new RuntimeException("Quyền không tồn tại");
        }
        permissionRepository.deleteById(id);
    }
}