'use strict';

angular.module('plumbeerappApp')
    .controller('MensajeController', function ($scope, $state, DataUtils, Mensaje) {

        $scope.mensajes = [];
        $scope.loadAll = function() {
            Mensaje.query(function(result) {
               $scope.mensajes = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.mensaje = {
                titulo: null,
                cuerpo: null,
                fecha: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });
