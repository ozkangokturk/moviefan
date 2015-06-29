'use strict';

angular.module('moviefanApp')
    .factory('Genre', function ($resource, DateUtils) {
        return $resource('api/genres/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
