'use strict';

angular.module('moviefanApp')
    .factory('Movie', function ($resource, DateUtils) {
        return $resource('api/movies/:id', {}, {
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

angular.module('moviefanApp')
    .factory('MovieFan', function ($resource, DateUtils) {
        return $resource('api/moviefan/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'addToFav': { method:'POST',params: {
                id: "@id"
            } },
            'removeFromFav': { method:'DELETE',params: {
                id: "@id"
            } }
        });
    });
