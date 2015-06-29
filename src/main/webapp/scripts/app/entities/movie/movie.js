'use strict';

angular.module('moviefanApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('movie', {
                parent: 'entity',
                url: '/movie',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'moviefanApp.movie.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/movie/movies.html',
                        controller: 'MovieController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('movie');
                        return $translate.refresh();
                    }]
                }
            })
            .state('movieDetail', {
                parent: 'entity',
                url: '/movie/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'moviefanApp.movie.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/movie/movie-detail.html',
                        controller: 'MovieDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('movie');
                        return $translate.refresh();
                    }]
                }
            })
            .state('movieFan', {
                parent: 'entity',
                url: '/movieFan',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'moviefanApp.movie.fan.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/movie/moviefan.html',
                        controller: 'MovieController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('movie');
                        return $translate.refresh();
                    }]
                }
            });
    });
