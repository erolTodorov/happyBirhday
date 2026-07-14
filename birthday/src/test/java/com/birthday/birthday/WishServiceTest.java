package com.birthday.birthday;

import com.birthday.birthday.model.Wish;
import com.birthday.birthday.repository.WishRepository;
import com.birthday.birthday.service.WishService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class WishServiceTest {

    @Autowired
    private WishService wishService;

    @Autowired
    private WishRepository wishRepository;

    @Test
    void shouldSaveWishAndExposeItToAdmins() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "photo.png", "image/png", "test".getBytes());

        Wish savedWish = wishService.saveWish("Maria", "Честит рожден ден!", true, image);

        assertNotNull(savedWish.getId());
        assertTrue(savedWish.isApproved());

        List<Wish> wishes = wishService.getAllWishesForAdmin();
        assertFalse(wishes.isEmpty());
    }

    @Test
    void shouldSaveOneWishPerUploadedImage() throws Exception {
        wishRepository.deleteAll();

        MockMultipartFile firstImage = new MockMultipartFile("images", "first.png", "image/png", "first".getBytes());
        MockMultipartFile secondImage = new MockMultipartFile("images", "second.png", "image/png", "second".getBytes());

        int savedCount = wishService.saveWishes("Ани", "Поздрав", true, new MockMultipartFile[]{firstImage, secondImage});

        assertEquals(2, savedCount);
        assertEquals(2, wishRepository.findAll().size());
    }
}
