'use strict';

angular.module('yaeventApp')
    .controller('EventController', function ($scope, $state, Event) {

        $scope.events = [];
        $scope.loadAll = function() {
            Event.query(function(result) {
               $scope.events = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.event = {
                creationDate: null,
                modificationDate: null,
                version: null,
                status: null,
                eventSize: null,
                type: null,
                name: null,
                description: null,
                location: null,
                id: null
            };
        };
    });
