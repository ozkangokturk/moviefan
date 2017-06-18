package com.ozkangokturk.moviefan.web.rest;

import com.ozkangokturk.moviefan.Application;
import com.ozkangokturk.moviefan.domain.Movie;
import com.ozkangokturk.moviefan.repository.MovieRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MovieResource REST controller.
 *
 * @see MovieResource
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
//@IntegrationTest
public class MovieResourceTest {

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_RATING = "SAMPLE_TEXT";
    private static final String UPDATED_RATING = "UPDATED_TEXT";
    private static final String DEFAULT_DIRECTOR = "SAMPLE_TEXT";
    private static final String UPDATED_DIRECTOR = "UPDATED_TEXT";

    @Inject
    private MovieRepository movieRepository;

    private MockMvc restMovieMockMvc;

    private Movie movie;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MovieResource movieResource = new MovieResource();
        ReflectionTestUtils.setField(movieResource, "movieRepository", movieRepository);
        this.restMovieMockMvc = MockMvcBuilders.standaloneSetup(movieResource).build();
    }

    @Before
    public void initTest() {
        movie = new Movie();
        movie.setTitle(DEFAULT_TITLE);
        movie.setRating(DEFAULT_RATING);
        movie.setDirector(DEFAULT_DIRECTOR);
    }

    //@Test
    @Transactional
    public void createMovie() throws Exception {
        int databaseSizeBeforeCreate = movieRepository.findAll().size();

        // Create the Movie
        restMovieMockMvc.perform(post("/api/movies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(movie)))
                .andExpect(status().isCreated());

        // Validate the Movie in the database
        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).hasSize(databaseSizeBeforeCreate + 1);
        Movie testMovie = movies.get(movies.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMovie.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testMovie.getDirector()).isEqualTo(DEFAULT_DIRECTOR);
    }

    //@Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(movieRepository.findAll()).hasSize(0);
        // set the field null
        movie.setTitle(null);

        // Create the Movie, which fails.
        restMovieMockMvc.perform(post("/api/movies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(movie)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).hasSize(0);
    }

    //@Test
    @Transactional
    public void checkDirectorIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(movieRepository.findAll()).hasSize(0);
        // set the field null
        movie.setDirector(null);

        // Create the Movie, which fails.
        restMovieMockMvc.perform(post("/api/movies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(movie)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).hasSize(0);
    }

    //@Test
    @Transactional
    public void getAllMovies() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movies
        restMovieMockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(movie.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.toString())))
                .andExpect(jsonPath("$.[*].director").value(hasItem(DEFAULT_DIRECTOR.toString())));
    }

    //@Test
    @Transactional
    public void getMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get the movie
        restMovieMockMvc.perform(get("/api/movies/{id}", movie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(movie.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.toString()))
            .andExpect(jsonPath("$.director").value(DEFAULT_DIRECTOR.toString()));
    }

    //@Test
    @Transactional
    public void getNonExistingMovie() throws Exception {
        // Get the movie
        restMovieMockMvc.perform(get("/api/movies/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    //@Test
    @Transactional
    public void updateMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

		int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie
        movie.setTitle(UPDATED_TITLE);
        movie.setRating(UPDATED_RATING);
        movie.setDirector(UPDATED_DIRECTOR);
        restMovieMockMvc.perform(put("/api/movies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(movie)))
                .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movies.get(movies.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMovie.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testMovie.getDirector()).isEqualTo(UPDATED_DIRECTOR);
    }

    //@Test
    @Transactional
    public void deleteMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

		int databaseSizeBeforeDelete = movieRepository.findAll().size();

        // Get the movie
        restMovieMockMvc.perform(delete("/api/movies/{id}", movie.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).hasSize(databaseSizeBeforeDelete - 1);
    }
}
