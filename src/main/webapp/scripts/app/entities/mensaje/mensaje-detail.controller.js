'use strict';

angular.module('plumbeerappApp')
    .controller('MensajeDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Mensaje, User) {
        $scope.mensaje = entity;
        $scope.load = function (id) {
            Mensaje.get({id: id}, function(result) {
                $scope.mensaje = result;
            });
        };
        var unsubscribe = $rootScope.$on('plumbeerappApp:mensajeUpdate', function(event, result) {
            $scope.mensaje = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });
