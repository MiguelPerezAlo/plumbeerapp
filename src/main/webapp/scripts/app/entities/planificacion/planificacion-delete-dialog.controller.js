'use strict';

angular.module('plumbeerappApp')
	.controller('PlanificacionDeleteController', function($scope, $uibModalInstance, entity, Planificacion) {

        $scope.planificacion = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Planificacion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
