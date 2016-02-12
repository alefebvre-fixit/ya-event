'use strict';

angular.module('yaeventApp')
	.controller('EventDeleteController', function($scope, $uibModalInstance, entity, Event) {

        $scope.event = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Event.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
