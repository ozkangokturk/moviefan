package com.ozkangokturk.moviefan.repository;

import com.ozkangokturk.moviefan.domain.Movie;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Movie entity.
 */
public interface MovieRepository extends JpaRepository<Movie,Long> {

    @Query("select movie from Movie movie left join fetch movie.genres where movie.id =:id")
    Movie findOneWithEagerRelationships(@Param("id") Long id);

}
