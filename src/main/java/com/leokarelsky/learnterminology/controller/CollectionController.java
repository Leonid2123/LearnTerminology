package com.leokarelsky.learnterminology.controller;

import com.leokarelsky.learnterminology.model.Termin;
import com.leokarelsky.learnterminology.model.TerminCollection;
import com.leokarelsky.learnterminology.model.User;
import com.leokarelsky.learnterminology.service.CollectionService;
import com.leokarelsky.learnterminology.service.TerminService;
import com.leokarelsky.learnterminology.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/collections")
public class CollectionController {

    private final CollectionService collectionService;
    private final TerminService terminService;
    private final UserService userService;

    public CollectionController(CollectionService collectionService, TerminService terminService, UserService userService) {
        this.collectionService = collectionService;
        this.terminService = terminService;
        this.userService = userService;
    }

    @GetMapping("/create/users/{owner_id}")
    public String create(@PathVariable("owner_id") long ownerId, Model model) {
        model.addAttribute("collection", new TerminCollection());
        model.addAttribute("ownerId", ownerId);
        return "create-collection";
    }

    @PostMapping("/create/users/{owner_id}")
    public String create(@PathVariable("owner_id") long ownerId, @Validated @ModelAttribute("collection") TerminCollection terminCollection, BindingResult result) {
        if (result.hasErrors()) {
            return "create-collection";
        }
        terminCollection.setCreatedAt(LocalDateTime.now());
        terminCollection.setOwner(userService.findById(ownerId));
        collectionService.create(terminCollection);
        return "redirect:/collections/all/users/" + ownerId;
    }

    @GetMapping("/{id}/termins")
    public String read(@PathVariable long id, Model model) {
        TerminCollection terminCollection = collectionService.readById(id);
        List<Termin> termins = terminService.getByCollectionId(id);
        User owner = collectionService.readById(id).getOwner();
        List<User> users = userService.getAll().stream()
                .filter(user -> user.getId() != terminCollection.getOwner().getId()).collect(Collectors.toList());
        model.addAttribute("collection", terminCollection);
        model.addAttribute("termins", termins);
        model.addAttribute("owner", owner);
        model.addAttribute("users", users);
        return "collection-termins";
    }

    @GetMapping("/{collection_id}/update/users/{owner_id}")
    public String update(@PathVariable("collection_id") long collectionId, @PathVariable("owner_id") long ownerId, Model model) {
        TerminCollection collection = collectionService.readById(collectionId);
        model.addAttribute("collection", collection);
        return "update-collection";
    }

    @PostMapping("/{collection_id}/update/users/{owner_id}")
    public String update(@PathVariable("collection_id") long collectionId, @PathVariable("owner_id") long ownerId,
                         @Validated @ModelAttribute("collection") TerminCollection collection, BindingResult result) {
        if (result.hasErrors()) {
            collection.setOwner(userService.findById(ownerId));
            return "update-collection";
        }
        TerminCollection oldo = collectionService.readById(collectionId);
        collection.setOwner(oldo.getOwner());
        collection.setCollaborators(oldo.getCollaborators());
        collectionService.update(collection);
        return "redirect:/collections/all/users/" + ownerId;
    }

    @GetMapping("/{collection_id}/delete/users/{owner_id}")
    public String delete(@PathVariable("collection_id") long collectionId, @PathVariable("owner_id") long ownerId) {
        collectionService.delete(collectionId);
        return "redirect:/collections/all/users/" + ownerId;
    }

    @GetMapping("/all/users/{user_id}")
    public String getAll(@PathVariable("user_id") long userId, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findById(userId);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        boolean isAdmin = authentication.getAuthorities().contains(authority);

        if (!isAdmin && !user.getUsername().equals(currentPrincipalName)) return "forbidden";

        List<TerminCollection> collections;
        if (user.getRole().getName().equals("ADMIN")
                && currentPrincipalName.equals(user.getUsername())) {
            collections = collectionService.getAll();
        } else {
            collections = collectionService.getByUserId(userId);
        }
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("collections", collections);
        model.addAttribute("user", user);
        model.addAttribute("username", currentPrincipalName);
        return "collections-user";
    }

    @GetMapping("/{collection_id}/learn")
    public String learn(@PathVariable("collection_id") long collection_id, Model model) {
        List<Termin> termins = collectionService.readById(collection_id).getTermins();
        if (!termins.isEmpty()) {
            Termin first = termins.get(0);
            termins.remove(0);
            model.addAttribute("collection_id", collection_id);
            model.addAttribute("first", first);
            model.addAttribute("termins", termins);
        }
        return "learn-termins";
    }

    @GetMapping("/{id}/add")
    public String addCollaborator(@PathVariable long id, @RequestParam("user_id") long userId) {
        if (userId == -1) {
            return "redirect:/collections/" + id + "/termins";
        }
        TerminCollection collection = collectionService.readById(id);
        List<User> collaborators = collection.getCollaborators();
        collaborators.add(userService.findById(userId));
        collection.setCollaborators(collaborators);
        collectionService.update(collection);
        return "redirect:/collections/" + id + "/termins";
    }

    @GetMapping("/{id}/remove")
    public String removeCollaborator(@PathVariable long id, @RequestParam("user_id") long userId) {

        TerminCollection collection = collectionService.readById(id);
        List<User> collaborators = collection.getCollaborators();
        collaborators.remove(userService.findById(userId));
        collection.setCollaborators(collaborators);
        collectionService.update(collection);
        return "redirect:/collections/" + id + "/termins";
    }
}
