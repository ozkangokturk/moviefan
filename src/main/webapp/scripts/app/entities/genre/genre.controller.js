'use strict';

angular.module('moviefanApp')
    .controller('GenreController', function ($scope, Genre, Movie, ParseLinks) {
        $scope.genres = [];
        $scope.movies = Movie.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Genre.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.genres = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Genre.get({id: id}, function(result) {
                $scope.genre = result;
                $('#saveGenreModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.genre.id != null) {
                Genre.update($scope.genre,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Genre.save($scope.genre,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Genre.get({id: id}, function(result) {
                $scope.genre = result;
                $('#deleteGenreConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Genre.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteGenreConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveGenreModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.genre = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
