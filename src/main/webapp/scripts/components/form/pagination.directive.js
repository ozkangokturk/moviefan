/* globals $ */
'use strict';

angular.module('moviefanApp')
    .directive('moviefanAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
