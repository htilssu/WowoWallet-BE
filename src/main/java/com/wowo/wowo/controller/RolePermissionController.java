package com.wowo.wowo.controller;

import com.wowo.wowo.model.RolePermission;
import com.wowo.wowo.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/role-permissions")
public class RolePermissionController {
    @Autowired
    private RolePermissionService rolePermissionService;

    @PostMapping
    public ResponseEntity<RolePermission> createRolePermission(@RequestBody RolePermission rolePermission) {
        return ResponseEntity.ok(rolePermissionService.createRolePermission(rolePermission));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolePermission> getRolePermissionById(@PathVariable Long id) {
        Optional<RolePermission> rolePermission = rolePermissionService.findById(id);
        return rolePermission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRolePermission(@PathVariable Long id) {
        rolePermissionService.deleteRolePermission(id);
        return ResponseEntity.noContent().build();
    }

}