package com.leokarelsky.learnterminology.controller;

import com.leokarelsky.learnterminology.model.TerminCollection;
import com.leokarelsky.learnterminology.model.User;
import com.leokarelsky.learnterminology.service.CollectionService;
import com.leokarelsky.learnterminology.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
    private final UserService userService;
    private final CollectionService collectionService;

    public HomeController(UserService userService, CollectionService collectionService) {
        this.userService = userService;
        this.collectionService = collectionService;
    }

    @GetMapping({"/", "home"})
    public String home(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        List<TerminCollection> collections = collectionService.getByUserId(user.getId());
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        boolean isAdmin = user.getAuthorities().contains(authority);

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("user", user);
        model.addAttribute("collections", collections);
        model.addAttribute("username", user.getUsername());
        return "collections-user";
    }

    /*@GetMapping({"/", "home"})
    public String home(Model model) {
        model.addAttribute("users", userService.getAll());
        return "home";
    }*/

    @GetMapping({"/forbidden"})
    public String forbidden(Model model) {
        return "forbidden";
    }
}
