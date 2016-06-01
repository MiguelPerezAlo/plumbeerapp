'use strict';

angular.module('plumbeerappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('trabajo', {
                parent: 'entity',
                url: '/trabajos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerappApp.trabajo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/trabajo/trabajos.html',
                        controller: 'TrabajoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('trabajo');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('trabajo.detail', {
                parent: 'entity',
                url: '/trabajo/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerappApp.trabajo.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/trabajo/trabajo-detail.html',
                        controller: 'TrabajoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('trabajo');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Trabajo', function($stateParams, Trabajo) {
                        return Trabajo.get({id : $stateParams.id});
                    }]
                }
            })
            .state('trabajo.new', {
                parent: 'trabajo',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/trabajo/trabajo-dialog.html',
                        controller: 'TrabajoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    asunto: null,
                                    descripcion: null,
                                    fecha: null,
                                    visible: null,
                                    abierto: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('trabajo', null, { reload: true });
                    }, function() {
                        $state.go('trabajo');
                    })
                }]
            })
            .state('trabajo.edit', {
                parent: 'trabajo',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/trabajo/trabajo-dialog.html',
                        controller: 'TrabajoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Trabajo', function(Trabajo) {
                                return Trabajo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('trabajo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('trabajo.delete', {
                parent: 'trabajo',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/trabajo/trabajo-delete-dialog.html',
                        controller: 'TrabajoDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Trabajo', function(Trabajo) {
                                return Trabajo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('trabajo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
