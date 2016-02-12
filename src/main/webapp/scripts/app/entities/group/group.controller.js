'use strict';

angular.module('yaeventApp')
    .controller('GroupController', function ($scope, $state, Group) {

        $scope.groups = [];
        $scope.loadAll = function() {
            Group.query(function(result) {
               $scope.groups = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.group = {
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
