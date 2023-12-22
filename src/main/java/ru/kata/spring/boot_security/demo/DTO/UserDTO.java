package ru.kata.spring.boot_security.demo.DTO;

import org.modelmapper.ModelMapper;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {

    private Long id;

    private String username;

    private String password;

    private List<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String passwordToEncoding() {
        return this.password;
    }

    public List<RoleDTO> getRoles() {
        List<RoleDTO> rolesDTO = new ArrayList<>();
        rolesDTO = this.roles.stream().map(role -> new ModelMapper().map(role, RoleDTO.class)).collect(Collectors.toList());
        return rolesDTO;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
