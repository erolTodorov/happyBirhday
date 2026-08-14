package com.birthday.birthday;

import com.birthday.birthday.model.Wish;
import com.birthday.birthday.repository.WishRepository;
import com.birthday.birthday.service.WishService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class WishControllerTest {

    @TempDir
    Path tempDir;

    @Test
    void contextLoads() {
    }

    @Test
    void deleteWish_shouldDeleteDatabaseRecordAndFile() throws IOException {
        WishRepository wishRepository = mock(WishRepository.class);
        Path imageFile = tempDir.resolve("photo.jpg");
        Files.write(imageFile, "hello".getBytes());

        Wish wish = new Wish();
        wish.setId(1L);
        wish.setImagePath("/uploads/photo.jpg");

        when(wishRepository.findById(1L)).thenReturn(Optional.of(wish));

        WishService wishService = new WishService(wishRepository, tempDir.toString());
        wishService.deleteWish(1L);

        verify(wishRepository).delete(wish);
        assertFalse(Files.exists(imageFile));
    }
}
