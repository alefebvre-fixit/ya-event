'use strict';

angular.module('yaeventApp')
	.controller('GroupDeleteController', function($scope, $uibModalInstance, entity, Group) {

        $scope.group = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Group.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
