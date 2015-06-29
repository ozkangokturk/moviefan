'use strict';

angular.module('moviefanApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('genre', {
                parent: 'entity',
                url: '/genre',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'moviefanApp.genre.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/genre/genres.html',
                        controller: 'GenreController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('genre');
                        return $translate.refresh();
                    }]
                }
            })
            .state('genreDetail', {
                parent: 'entity',
                url: '/genre/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'moviefanApp.genre.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/genre/genre-detail.html',
                        controller: 'GenreDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('genre');
                        return $translate.refresh();
                    }]
                }
            });
    });
