package com.birthday.birthday.repository;

import com.birthday.birthday.model.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByApprovedTrueOrderByIdDesc();
    List<Wish> findAllByOrderByIdDesc();
}
