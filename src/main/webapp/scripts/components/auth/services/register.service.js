'use strict';

angular.module('plumbeerappApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


