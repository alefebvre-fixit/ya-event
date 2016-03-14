'use strict';

angular.module('yaeventApp')
    .controller('UserManagementDetailController', function ($scope, $stateParams, User) {
        $scope.user = {};
        $scope.load = function (username) {
            User.get({login: username}, function(result) {
                $scope.user = result;
            });
        };
        $scope.load($stateParams.login);
    });
