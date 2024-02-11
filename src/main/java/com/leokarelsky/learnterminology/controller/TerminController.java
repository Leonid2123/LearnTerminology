package com.leokarelsky.learnterminology.controller;

import com.leokarelsky.learnterminology.dto.TerminDTO;
import com.leokarelsky.learnterminology.dto.TerminHolder;
import com.leokarelsky.learnterminology.dto.TerminTransformer;
import com.leokarelsky.learnterminology.model.Termin;
import com.leokarelsky.learnterminology.service.CollectionService;
import com.leokarelsky.learnterminology.service.TerminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

@Controller
@RequestMapping("/termins")
public class TerminController {
    private final CollectionService collectionService;
    private final TerminService terminService;

    public TerminController(CollectionService collectionService, TerminService terminService) {
        this.collectionService = collectionService;
        this.terminService = terminService;
    }

    @GetMapping("/create/collections/{collection_id}")
    public String create(@PathVariable("collection_id") long collectionId, Model model) {
        model.addAttribute("termin", new TerminDTO());
        model.addAttribute("collection", collectionService.readById(collectionId));
        return "create-termin";
    }

    @PostMapping("/create/collections/{collection_id}")
    public String create(@PathVariable("collection_id") long collectionId, Model model,
                         @Validated @ModelAttribute("termin") TerminDTO terminDto, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("collection", collectionService.readById(collectionId));
            return "create-termin";
        }
        Termin termin = TerminTransformer.convertToEntity(
                terminDto,
                collectionService.readById(terminDto.getCollectionId())
        );
        terminService.create(termin);
        return "redirect:/collections/" + collectionId + "/termins";
    }

    @GetMapping("/{termin_id}/update/collections/{collection_id}")
    public String update(@PathVariable("termin_id") long terminId, @PathVariable("collection_id") long collectionId, Model model) {
        TerminDTO terminDto = TerminTransformer.convertToDto(terminService.readById(terminId));
        model.addAttribute("termin", terminDto);
        return "update-termin";
    }

    @PostMapping("/{termin_id}/update/collections/{collection_id}")
    public String update(@PathVariable("termin_id") long terminId, @PathVariable("collection_id") long collectionId, Model model,
                         @Validated @ModelAttribute("termin")TerminDTO terminDto, BindingResult result) {
        if (result.hasErrors()) {
            return "update-termin";
        }
        Termin termin = TerminTransformer.convertToEntity(
                terminDto,
                collectionService.readById(terminDto.getCollectionId())
        );
        terminService.update(termin);
        return "redirect:/collections/" + collectionId + "/termins";
    }

    @GetMapping("/{termin_id}/delete/collections/{collection_id}")
    public String delete(@PathVariable("termin_id") long terminId, @PathVariable("collection_id") long collectionId) {
        terminService.delete(terminId);
        return "redirect:/collections/" + collectionId + "/termins";
    }

    @GetMapping("/import/collections/{collection_id}")
    public String importTermins(@PathVariable("collection_id") long collectionId, Model model) {
        model.addAttribute("termins", new TerminHolder());
        model.addAttribute("collection", collectionService.readById(collectionId));
        return "import";
    }

    @PostMapping("/import/collections/{collection_id}")
    public String importTermins(@PathVariable("collection_id") long collectionId,
                         @ModelAttribute("termins") TerminHolder termins) throws IOException {
        BufferedReader bufReader = new BufferedReader(new StringReader(termins.getText()));
        String line;
        while((line = bufReader.readLine()) != null) {
            int index1 = line.indexOf(" - ");
            int index2 = line.indexOf(" = ");

            if (index1 > 0) {
                terminService.create(
                        new Termin(
                                line.substring(0, index1),
                                line.substring(index1+3),
                                collectionService.readById(collectionId)
                                ));
            }
            if (index2 > 0) {
                terminService.create(
                        new Termin(
                                line.substring(0, index2),
                                line.substring(index2+3),
                                collectionService.readById(collectionId)
                        ));
            }
        }
        return "redirect:/collections/" + collectionId + "/termins";
    }
}
