'use strict';

angular.module('plumbeerappApp')
    .controller('PlanificacionDetailController', function ($scope, $rootScope, $stateParams, entity, Planificacion, User, Trabajo) {
        $scope.planificacion = entity;
        $scope.load = function (id) {
            Planificacion.get({id: id}, function(result) {
                $scope.planificacion = result;
            });
        };
        var unsubscribe = $rootScope.$on('plumbeerappApp:planificacionUpdate', function(event, result) {
            $scope.planificacion = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
