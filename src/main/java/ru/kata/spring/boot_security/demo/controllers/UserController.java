package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String showIndexPage() {
        return "redirect:/login";
    }

    @GetMapping("/my")
    public String showMyPage(Model model, Principal principal) {
        User authorizedUser = userService.findByUsername(principal.getName());
        model.addAttribute("authorizedUser", authorizedUser);
        return "my";
    }


    @GetMapping("/user")
    public String showUserPage(Model model, Principal principal) {
        User authorizedUser = userService.findByUsername(principal.getName());
        model.addAttribute("authorizedUser", authorizedUser);
        return "user";
    }

    @GetMapping("/admin")
    public String showAdminPage(Model model, Principal principal) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("allRoles", userService.getAllRoles());

        //User authorizedUser = userService.findByUsername(principal.getName());
        //model.addAttribute("authorizedUser", authorizedUser);
        Optional<User> optional = allUsers.stream().filter(user -> user.getUsername().equals(principal.getName())).findAny();
        if (optional.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("authorizedUser", optional.get());

        model.addAttribute("editUser", new User());
        model.addAttribute("newUserToAdd", new User());
        return "admin";
    }

    @PostMapping(value = "admin/edit-user")
    public String editUser(@ModelAttribute("editUser") User editUser, @RequestParam("formEditRole") String formEditRole) {
        List<User> listUser = userService.getAllUsers();
        if (listUser.stream()
                .noneMatch(u -> u.getUsername().equals(editUser.getUsername())
                        && !u.getId().equals(editUser.getId()))) {
            userService.updateUser(editUser);
            userService.addRoleToUser(editUser.getUsername(), formEditRole);
            return "redirect:/admin";
        }
        return "error-username";
    }

    @PostMapping(value = "admin/saveNewUser")
    public String saveNewUser(@ModelAttribute("add") User user, @RequestParam("formAddRole") String formAddRole) {
        List<User> listUser = userService.getAllUsers();
        if (listUser.stream()
                .noneMatch(u -> u.getUsername().equals(user.getUsername())
                        && !u.getId().equals(user.getId()))) {
            String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
            user.setPassword(encodedPassword);
            userService.saveUser(user);
            userService.addRoleToUser(user.getUsername(), formAddRole);
            return "redirect:/admin";
        }
        return "error-username";
    }

    @PostMapping(value = "admin/deleteUser")
    public String deleteUserById(@RequestParam("userId") Long userId) {
        userService.deleteUser(userId);
        return "redirect:/admin";
    }
}