'use strict';

angular.module('plumbeerappApp').controller('PlanificacionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Planificacion', 'User', 'Trabajo',
        function($scope, $stateParams, $uibModalInstance, entity, Planificacion, User, Trabajo) {

        $scope.planificacion = entity;
        $scope.users = User.query();
        $scope.trabajos = Trabajo.query();
        $scope.load = function(id) {
            Planificacion.get({id : id}, function(result) {
                $scope.planificacion = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('plumbeerappApp:planificacionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.planificacion.id != null) {
                Planificacion.update($scope.planificacion, onSaveSuccess, onSaveError);
            } else {
                Planificacion.save($scope.planificacion, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForFecha = {};

        $scope.datePickerForFecha.status = {
            opened: false
        };

        $scope.datePickerForFechaOpen = function($event) {
            $scope.datePickerForFecha.status.opened = true;
        };
}]);
