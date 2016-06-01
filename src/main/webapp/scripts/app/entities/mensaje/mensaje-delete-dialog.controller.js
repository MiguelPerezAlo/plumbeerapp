'use strict';

angular.module('plumbeerappApp')
	.controller('MensajeDeleteController', function($scope, $uibModalInstance, entity, Mensaje) {

        $scope.mensaje = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Mensaje.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
