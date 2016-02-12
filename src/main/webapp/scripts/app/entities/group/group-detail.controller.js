'use strict';

angular.module('yaeventApp')
    .controller('GroupDetailController', function ($scope, $rootScope, $stateParams, entity, Group) {
        $scope.group = entity;
        $scope.load = function (id) {
            Group.get({id: id}, function(result) {
                $scope.group = result;
            });
        };
        var unsubscribe = $rootScope.$on('yaeventApp:groupUpdate', function(event, result) {
            $scope.group = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
