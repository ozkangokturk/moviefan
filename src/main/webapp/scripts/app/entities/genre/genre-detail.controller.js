'use strict';

angular.module('moviefanApp')
    .controller('GenreDetailController', function ($scope, $stateParams, Genre, Movie) {
        $scope.genre = {};
        $scope.load = function (id) {
            Genre.get({id: id}, function(result) {
              $scope.genre = result;
            });
        };
        $scope.load($stateParams.id);
    });
