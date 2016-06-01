'use strict';

describe('Controller Tests', function() {

    describe('Planificacion Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPlanificacion, MockUser, MockTrabajo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPlanificacion = jasmine.createSpy('MockPlanificacion');
            MockUser = jasmine.createSpy('MockUser');
            MockTrabajo = jasmine.createSpy('MockTrabajo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Planificacion': MockPlanificacion,
                'User': MockUser,
                'Trabajo': MockTrabajo
            };
            createController = function() {
                $injector.get('$controller')("PlanificacionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'plumbeerappApp:planificacionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
