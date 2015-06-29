'use strict';

angular.module('moviefanApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
