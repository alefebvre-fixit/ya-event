'use strict';

angular.module('yaeventApp').controller('EventDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Event',
        function($scope, $stateParams, $uibModalInstance, entity, Event) {

        $scope.event = entity;
        $scope.load = function(id) {
            Event.get({id : id}, function(result) {
                $scope.event = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('yaeventApp:eventUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.event.id != null) {
                Event.update($scope.event, onSaveSuccess, onSaveError);
            } else {
                Event.save($scope.event, onSaveSuccess, onSaveError);
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
