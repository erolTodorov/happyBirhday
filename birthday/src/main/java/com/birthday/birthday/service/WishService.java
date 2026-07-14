package com.birthday.birthday.service;

import com.birthday.birthday.model.Wish;
import com.birthday.birthday.repository.WishRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final Path uploadDirectory;

    public WishService(WishRepository wishRepository, @Value("${app.upload-dir:uploads}") String uploadDir) {
        this.wishRepository = wishRepository;
        this.uploadDirectory = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Неуспешно създаване на директория за снимки", e);
        }
    }

    public int saveWishes(String name, String message, boolean visibleToAll, MultipartFile[] imageFiles) throws IOException {
        int savedCount = 0;
        for (MultipartFile imageFile : imageFiles) {
            if (imageFile == null || imageFile.isEmpty()) {
                continue;
            }
            saveWish(name, message, visibleToAll, imageFile);
            savedCount++;
        }
        return savedCount;
    }

    public Wish saveWish(String name, String message, boolean visibleToAll, MultipartFile imageFile) throws IOException {
        String originalFileName = imageFile.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        }
        String storedFileName = UUID.randomUUID() + fileExtension;
        Path targetPath = uploadDirectory.resolve(storedFileName);
        Files.copy(imageFile.getInputStream(), targetPath);

        Wish wish = new Wish();
        wish.setName(name);
        wish.setMessage(message);
        wish.setVisibleToAll(visibleToAll);
        wish.setImagePath("/uploads/" + storedFileName);
        wish.setApproved(true);
        return wishRepository.save(wish);
    }

    public List<Wish> getApprovedWishes() {
        return wishRepository.findAllByOrderByIdDesc();
    }

    public List<Wish> getAllWishesForAdmin() {
        return wishRepository.findAllByOrderByIdDesc();
    }

    public Wish approveWish(Long id) {
        Wish wish = wishRepository.findById(id).orElseThrow();
        wish.setApproved(true);
        return wishRepository.save(wish);
    }
}
