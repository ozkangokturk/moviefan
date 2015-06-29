'use strict';

angular.module('moviefanApp')
    .controller('MovieDetailController', function ($scope, $stateParams, Movie, Genre) {
        $scope.movie = {};
        $scope.load = function (id) {
            Movie.get({id: id}, function(result) {
              $scope.movie = result;
            });
        };
        $scope.load($stateParams.id);
    });
