'use strict';

angular.module('yaeventApp')
    .controller('EventDetailController', function ($scope, $rootScope, $stateParams, entity, Event) {
        $scope.event = entity;
        $scope.load = function (id) {
            Event.get({id: id}, function(result) {
                $scope.event = result;
            });
        };
        var unsubscribe = $rootScope.$on('yaeventApp:eventUpdate', function(event, result) {
            $scope.event = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
