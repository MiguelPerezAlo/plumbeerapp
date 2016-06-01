'use strict';

angular.module('plumbeerappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('planificacion', {
                parent: 'entity',
                url: '/planificacions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerappApp.planificacion.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/planificacion/planificacions.html',
                        controller: 'PlanificacionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('planificacion');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('planificacion.detail', {
                parent: 'entity',
                url: '/planificacion/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerappApp.planificacion.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/planificacion/planificacion-detail.html',
                        controller: 'PlanificacionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('planificacion');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Planificacion', function($stateParams, Planificacion) {
                        return Planificacion.get({id : $stateParams.id});
                    }]
                }
            })
            .state('planificacion.new', {
                parent: 'planificacion',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/planificacion/planificacion-dialog.html',
                        controller: 'PlanificacionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    fecha: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('planificacion', null, { reload: true });
                    }, function() {
                        $state.go('planificacion');
                    })
                }]
            })
            .state('planificacion.edit', {
                parent: 'planificacion',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/planificacion/planificacion-dialog.html',
                        controller: 'PlanificacionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Planificacion', function(Planificacion) {
                                return Planificacion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('planificacion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('planificacion.delete', {
                parent: 'planificacion',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/planificacion/planificacion-delete-dialog.html',
                        controller: 'PlanificacionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Planificacion', function(Planificacion) {
                                return Planificacion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('planificacion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
