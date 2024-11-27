package com.wowo.wowo.controller;

import com.wowo.wowo.data.dto.UserDTO;
import com.wowo.wowo.model.Role;
import com.wowo.wowo.model.User;
import com.wowo.wowo.service.RoleService;
import com.wowo.wowo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<UserDTO>> getAdminUsers() {
        List<UserDTO> adminUsers = userService.getUsersByRoleId(1); // Get admin users as UserDTO
        return ResponseEntity.ok(adminUsers);
    }

    @GetMapping("/manager")
    public ResponseEntity<List<UserDTO>> getManagerUsers() {
        List<UserDTO> managerUsers = userService.getUsersByRoleId(2); // Get manager users as UserDTO
        return ResponseEntity.ok(managerUsers);
    }

    @GetMapping("/search")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        Optional<UserDTO> userDTO = userService.getUserByEmail(email);
        return userDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Integer id) {
        Optional<Role> role = roleService.findById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

}