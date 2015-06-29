package com.ozkangokturk.moviefan.web.rest;

import com.ozkangokturk.moviefan.Application;
import com.ozkangokturk.moviefan.domain.Genre;
import com.ozkangokturk.moviefan.repository.GenreRepository;

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
 * Test class for the GenreResource REST controller.
 *
 * @see GenreResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GenreResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private GenreRepository genreRepository;

    private MockMvc restGenreMockMvc;

    private Genre genre;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GenreResource genreResource = new GenreResource();
        ReflectionTestUtils.setField(genreResource, "genreRepository", genreRepository);
        this.restGenreMockMvc = MockMvcBuilders.standaloneSetup(genreResource).build();
    }

    @Before
    public void initTest() {
        genre = new Genre();
        genre.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createGenre() throws Exception {
        int databaseSizeBeforeCreate = genreRepository.findAll().size();

        // Create the Genre
        restGenreMockMvc.perform(post("/api/genres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genre)))
                .andExpect(status().isCreated());

        // Validate the Genre in the database
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres).hasSize(databaseSizeBeforeCreate + 1);
        Genre testGenre = genres.get(genres.size() - 1);
        assertThat(testGenre.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(genreRepository.findAll()).hasSize(0);
        // set the field null
        genre.setName(null);

        // Create the Genre, which fails.
        restGenreMockMvc.perform(post("/api/genres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genre)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllGenres() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genres
        restGenreMockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(genre.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get the genre
        restGenreMockMvc.perform(get("/api/genres/{id}", genre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(genre.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGenre() throws Exception {
        // Get the genre
        restGenreMockMvc.perform(get("/api/genres/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

		int databaseSizeBeforeUpdate = genreRepository.findAll().size();

        // Update the genre
        genre.setName(UPDATED_NAME);
        restGenreMockMvc.perform(put("/api/genres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genre)))
                .andExpect(status().isOk());

        // Validate the Genre in the database
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres).hasSize(databaseSizeBeforeUpdate);
        Genre testGenre = genres.get(genres.size() - 1);
        assertThat(testGenre.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

		int databaseSizeBeforeDelete = genreRepository.findAll().size();

        // Get the genre
        restGenreMockMvc.perform(delete("/api/genres/{id}", genre.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres).hasSize(databaseSizeBeforeDelete - 1);
    }
}
