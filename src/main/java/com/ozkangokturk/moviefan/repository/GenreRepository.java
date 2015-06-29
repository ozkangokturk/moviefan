package com.ozkangokturk.moviefan.repository;

import com.ozkangokturk.moviefan.domain.Genre;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Genre entity.
 */
public interface GenreRepository extends JpaRepository<Genre,Long> {

}
