'use strict';

angular.module('yaeventApp').controller('GroupDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Group',
        function($scope, $stateParams, $uibModalInstance, entity, Group) {

        $scope.group = entity;
        $scope.load = function(id) {
            Group.get({id : id}, function(result) {
                $scope.group = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('yaeventApp:groupUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.group.id != null) {
                Group.update($scope.group, onSaveSuccess, onSaveError);
            } else {
                Group.save($scope.group, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForCreationDate = {};

        $scope.datePickerForCreationDate.status = {
            opened: false
        };

        $scope.datePickerForCreationDateOpen = function($event) {
            $scope.datePickerForCreationDate.status.opened = true;
        };
        $scope.datePickerForModificationDate = {};

        $scope.datePickerForModificationDate.status = {
            opened: false
        };

        $scope.datePickerForModificationDateOpen = function($event) {
            $scope.datePickerForModificationDate.status.opened = true;
        };
}]);
