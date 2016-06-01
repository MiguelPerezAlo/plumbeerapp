'use strict';

angular.module('plumbeerappApp').controller('MensajeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Mensaje', 'User',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Mensaje, User) {

        $scope.mensaje = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Mensaje.get({id : id}, function(result) {
                $scope.mensaje = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('plumbeerappApp:mensajeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.mensaje.id != null) {
                Mensaje.update($scope.mensaje, onSaveSuccess, onSaveError);
            } else {
                Mensaje.save($scope.mensaje, onSaveSuccess, onSaveError);
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
