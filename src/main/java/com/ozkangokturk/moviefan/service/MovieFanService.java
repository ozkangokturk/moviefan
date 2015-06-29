package com.ozkangokturk.moviefan.service;

import com.ozkangokturk.moviefan.domain.Movie;
import com.ozkangokturk.moviefan.domain.User;
import com.ozkangokturk.moviefan.repository.AuthorityRepository;
import com.ozkangokturk.moviefan.repository.MovieRepository;
import com.ozkangokturk.moviefan.repository.UserRepository;
import com.ozkangokturk.moviefan.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Set;

/**
 * Service class for managing movie fan pages.
 */
@Service
@Transactional
public class MovieFanService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private MovieRepository movieRepository;

    public void addMovieToFavoriteList (Long movieid){
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        Movie movie = movieRepository.findOne(movieid);
        currentUser.getMovies().add(movie);
        userRepository.save(currentUser);
        log.debug("Add movie to Favorite List for User: {}", currentUser);

    }

    public void removeMovieFromFavoriteList (Long movieid){
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        Movie movie = movieRepository.findOne(movieid);
        currentUser.getMovies().remove(movie);
        userRepository.save(currentUser);
        log.debug("Add movie to Favorite List for User: {}", currentUser);

    }

    public Set<Movie> getFavoriteMovies (){
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        return currentUser.getMovies();
    }

}
