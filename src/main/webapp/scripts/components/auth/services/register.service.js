'use strict';

angular.module('yaeventApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


