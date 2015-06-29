
package com.ozkangokturk.moviefan.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozkangokturk.moviefan.domain.Movie;
import com.ozkangokturk.moviefan.repository.MovieRepository;
import com.ozkangokturk.moviefan.service.MovieFanService;
import com.ozkangokturk.moviefan.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * REST controller for managing Movie.
 */
@RestController
@RequestMapping("/api")
public class MovieResource {

    private final Logger log = LoggerFactory.getLogger(MovieResource.class);

    @Inject
    private MovieRepository movieRepository;

    @Inject
    private MovieFanService movieFanService;

    /**
     * POST  /movies -> Create a new movie.
     */
    @RequestMapping(value = "/movies",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Movie movie) throws URISyntaxException {
        log.debug("REST request to save Movie : {}", movie);
        if (movie.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new movie cannot already have an ID").build();
        }
        movieRepository.save(movie);
        return ResponseEntity.created(new URI("/api/movies/" + movie.getId())).build();
    }

    /**
     * PUT  /movies -> Updates an existing movie.
     */
    @RequestMapping(value = "/movies",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Movie movie) throws URISyntaxException {
        log.debug("REST request to update Movie : {}", movie);
        if (movie.getId() == null) {
            return create(movie);
        }
        movieRepository.save(movie);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /movies -> get all the movies.
     */
    @RequestMapping(value = "/movies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Movie>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                              @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Movie> page = movieRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/movies", offset, limit);
        return new ResponseEntity<List<Movie>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /movies/:id -> get the "id" movie.
     */
    @RequestMapping(value = "/movies/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Movie> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Movie : {}", id);
        Movie movie = movieRepository.findOneWithEagerRelationships(id);
        if (movie == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    /**
     * DELETE  /movies/:id -> delete the "id" movie.
     */
    @RequestMapping(value = "/movies/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Movie : {}", id);
        movieRepository.delete(id);
    }

    @RequestMapping(value = "/moviefan/{id}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void addToFav(@PathVariable Long id) {
        log.debug("REST request to add favorites Movie1 : {}", id);
        movieFanService.addMovieToFavoriteList(id);
    }

    @RequestMapping(value = "/moviefan/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void removeFromFav(@PathVariable Long id) {
        log.debug("REST request to add favorites Movie1 : {}", id);
        movieFanService.removeMovieFromFavoriteList(id);
    }


    /**
     * GET  /movies -> get all the movies.
     */
    @RequestMapping(value = "/moviefan",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Movie>> getFavoriteMovies()
        throws URISyntaxException {
        Set<Movie> favoriteMovies = movieFanService.getFavoriteMovies();
        List<Movie> favoriteMovieList = new ArrayList<Movie>(favoriteMovies);
        return new ResponseEntity<List<Movie>>(favoriteMovieList, HttpStatus.OK);
    }
}
