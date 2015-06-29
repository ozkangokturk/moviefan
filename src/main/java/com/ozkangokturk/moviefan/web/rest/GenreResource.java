package com.ozkangokturk.moviefan.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozkangokturk.moviefan.domain.Genre;
import com.ozkangokturk.moviefan.repository.GenreRepository;
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
import java.util.List;

/**
 * REST controller for managing Genre.
 */
@RestController
@RequestMapping("/api")
public class GenreResource {

    private final Logger log = LoggerFactory.getLogger(GenreResource.class);

    @Inject
    private GenreRepository genreRepository;

    /**
     * POST  /genres -> Create a new genre.
     */
    @RequestMapping(value = "/genres",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Genre genre) throws URISyntaxException {
        log.debug("REST request to save Genre : {}", genre);
        if (genre.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new genre cannot already have an ID").build();
        }
        genreRepository.save(genre);
        return ResponseEntity.created(new URI("/api/genres/" + genre.getId())).build();
    }

    /**
     * PUT  /genres -> Updates an existing genre.
     */
    @RequestMapping(value = "/genres",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Genre genre) throws URISyntaxException {
        log.debug("REST request to update Genre : {}", genre);
        if (genre.getId() == null) {
            return create(genre);
        }
        genreRepository.save(genre);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /genres -> get all the genres.
     */
    @RequestMapping(value = "/genres",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Genre>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Genre> page = genreRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/genres", offset, limit);
        return new ResponseEntity<List<Genre>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /genres/:id -> get the "id" genre.
     */
    @RequestMapping(value = "/genres/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genre> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Genre : {}", id);
        Genre genre = genreRepository.findOne(id);
        if (genre == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }

    /**
     * DELETE  /genres/:id -> delete the "id" genre.
     */
    @RequestMapping(value = "/genres/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Genre : {}", id);
        genreRepository.delete(id);
    }
}
