'use strict';

angular.module('plumbeerappApp')
    .controller('TrabajoController', function ($scope, $state, DataUtils, Trabajo) {

        $scope.trabajos = [];
        $scope.loadAll = function() {
            Trabajo.query(function(result) {
               $scope.trabajos = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.trabajo = {
                asunto: null,
                descripcion: null,
                fecha: null,
                visible: null,
                abierto: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });
