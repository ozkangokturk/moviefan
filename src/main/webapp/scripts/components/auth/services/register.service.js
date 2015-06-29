'use strict';

angular.module('moviefanApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


