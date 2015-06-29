'use strict';

angular.module('moviefanApp')
    .controller('MovieController', function ($scope, Movie, MovieFan, Genre, ParseLinks) {
        $scope.movies = [];
        $scope.favmovies = [];
        $scope.favmoviesList = [];
        $scope.genres = Genre.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Movie.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.movies = result;
            });
            MovieFan.query ({},function (result,headers){
                $scope.favmovies = result;
                angular.forEach($scope.favmovies, function(value, key){
                    $scope.favmoviesList.push(value.id);
                });
            });

        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Movie.get({id: id}, function(result) {
                $scope.movie = result;
                $('#saveMovieModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.movie.id != null) {
                Movie.update($scope.movie,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Movie.save($scope.movie,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Movie.get({id: id}, function(result) {
                $scope.movie = result;
                $('#deleteMovieConfirmation').modal('show');
            });
        };

        $scope.addToFav = function (id) {
            MovieFan.addToFav({id: id}, function(result) {
                $scope.loadAll();
                $scope.clear();
            });
        };

        $scope.removeFromFav = function (id) {
            MovieFan.removeFromFav({id: id}, function(result) {
                $scope.loadAll();
                $scope.clear();
            });
        };

        $scope.confirmDelete = function (id) {
            Movie.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMovieConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveMovieModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.movie = {title: null, rating: null, director: null, id: null};
            $scope.favmovies = {title: null, rating: null, director: null, id: null};
            $scope.favmoviesList = [];
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
