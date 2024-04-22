package com.vietphuongdo.shopapp.repositories;

import com.vietphuongdo.shopapp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
