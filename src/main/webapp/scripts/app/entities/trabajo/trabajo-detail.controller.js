'use strict';

angular.module('plumbeerappApp')
    .controller('TrabajoDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Trabajo, User) {
        $scope.trabajo = entity;
        $scope.load = function (id) {
            Trabajo.get({id: id}, function(result) {
                $scope.trabajo = result;
            });
        };
        var unsubscribe = $rootScope.$on('plumbeerappApp:trabajoUpdate', function(event, result) {
            $scope.trabajo = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });
