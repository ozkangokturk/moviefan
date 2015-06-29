/* globals $ */
'use strict';

angular.module('moviefanApp')
    .directive('moviefanAppPager', function() {
        return {
            templateUrl: 'scripts/components/form/pager.html'
        };
    });
