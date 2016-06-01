'use strict';

angular.module('plumbeerappApp').controller('TrabajoDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Trabajo', 'User',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Trabajo, User) {

        $scope.trabajo = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Trabajo.get({id : id}, function(result) {
                $scope.trabajo = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('plumbeerappApp:trabajoUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.trabajo.id != null) {
                Trabajo.update($scope.trabajo, onSaveSuccess, onSaveError);
            } else {
                Trabajo.save($scope.trabajo, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
        $scope.datePickerForFecha = {};

        $scope.datePickerForFecha.status = {
            opened: false
        };

        $scope.datePickerForFechaOpen = function($event) {
            $scope.datePickerForFecha.status.opened = true;
        };
}]);
