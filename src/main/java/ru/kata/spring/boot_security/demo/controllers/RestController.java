package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.DTO.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RestController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public RestController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all-user")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream().map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return modelMapper.map(user, UserDTO.class);
    }

    @GetMapping("/user-by-username/{username}")
    public UserDTO getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return modelMapper.map(user, UserDTO.class);
    }

    @PostMapping("/add-user")
    public ResponseEntity<HttpStatus> addUser(@RequestBody UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        String encodedPassword = new BCryptPasswordEncoder().encode(userDTO.passwordToEncoding());
        user.setPassword(encodedPassword);
        user.setRoles(null);
        userService.saveUser(user);
        userDTO.getRoles().stream()
                .forEach(roleDTO -> userService.addRoleToUser(user.getUsername(), roleDTO.getName()));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/edit-user")
    public ResponseEntity<HttpStatus> editUser(@RequestBody UserDTO userDTO) {
        List<User> listUser = userService.getAllUsers();
        if (listUser.stream()
                .noneMatch(u ->
                        u.getUsername().equals(userDTO.getUsername())
                        &&
                        !u.getId().equals(userDTO.getId())))
        {
            User user = modelMapper.map(userDTO, User.class);
            user.setPassword(userDTO.passwordToEncoding());
            user.setRoles(null);
            userService.updateUser(user);
            userDTO.getRoles().stream()
                    .forEach(roleDTO -> userService.addRoleToUser(user.getUsername(), roleDTO.getName()));
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<HttpStatus> deleteUserById(@RequestParam("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
