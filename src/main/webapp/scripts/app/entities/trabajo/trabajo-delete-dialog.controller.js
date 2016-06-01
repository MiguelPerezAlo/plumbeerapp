'use strict';

angular.module('plumbeerappApp')
	.controller('TrabajoDeleteController', function($scope, $uibModalInstance, entity, Trabajo) {

        $scope.trabajo = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Trabajo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
