package com.birthday.birthday.controller;

import com.birthday.birthday.model.Wish;
import com.birthday.birthday.service.WishService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("wishes", wishService.getApprovedWishes());
        model.addAttribute("wish", new Wish());
        return "index";
    }

    @PostMapping("/wishes")
    public String createWish(@RequestParam("name") String name,
                             @RequestParam("message") String message,
                             @RequestParam(value = "visible", defaultValue = "true") boolean visible,
                             @RequestParam("images") MultipartFile[] imageFiles,
                             Model model) {
        try {
            if (imageFiles == null || imageFiles.length == 0 || allImagesEmpty(imageFiles)) {
                model.addAttribute("error", "Моля, качете поне една снимка.");
                model.addAttribute("wishes", wishService.getApprovedWishes());
                model.addAttribute("wish", new Wish());
                return "index";
            }

            int savedCount = wishService.saveWishes(name, message, visible, imageFiles);
            model.addAttribute("success", "Изпратихте " + savedCount + " снимки и пожеланията бяха запазени.");
        } catch (Exception e) {
            model.addAttribute("error", "Възникна грешка: " + e.getMessage());
        }
        model.addAttribute("wishes", wishService.getApprovedWishes());
        model.addAttribute("wish", new Wish());
        return "index";
    }

    private boolean allImagesEmpty(MultipartFile[] imageFiles) {
        for (MultipartFile imageFile : imageFiles) {
            if (imageFile != null && !imageFile.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("wishes", wishService.getAllWishesForAdmin());
        return "admin";
    }

    @GetMapping("/gallery")
    public String gallery(Model model) {
        model.addAttribute("wishes", wishService.getApprovedWishes());
        return "gallery";
    }
}
