'use strict';

angular.module('plumbeerappApp')
    .controller('PlanificacionController', function ($scope, $state, Planificacion) {

        $scope.planificacions = [];
        $scope.loadAll = function() {
            Planificacion.query(function(result) {
               $scope.planificacions = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.planificacion = {
                fecha: null,
                id: null
            };
        };
    });
