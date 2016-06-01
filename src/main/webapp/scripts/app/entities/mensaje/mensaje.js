'use strict';

angular.module('plumbeerappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('mensaje', {
                parent: 'entity',
                url: '/mensajes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerappApp.mensaje.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/mensaje/mensajes.html',
                        controller: 'MensajeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('mensaje');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('mensaje.detail', {
                parent: 'entity',
                url: '/mensaje/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerappApp.mensaje.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/mensaje/mensaje-detail.html',
                        controller: 'MensajeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('mensaje');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Mensaje', function($stateParams, Mensaje) {
                        return Mensaje.get({id : $stateParams.id});
                    }]
                }
            })
            .state('mensaje.new', {
                parent: 'mensaje',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/mensaje/mensaje-dialog.html',
                        controller: 'MensajeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    titulo: null,
                                    cuerpo: null,
                                    fecha: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('mensaje', null, { reload: true });
                    }, function() {
                        $state.go('mensaje');
                    })
                }]
            })
            .state('mensaje.edit', {
                parent: 'mensaje',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/mensaje/mensaje-dialog.html',
                        controller: 'MensajeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Mensaje', function(Mensaje) {
                                return Mensaje.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('mensaje', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('mensaje.delete', {
                parent: 'mensaje',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/mensaje/mensaje-delete-dialog.html',
                        controller: 'MensajeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Mensaje', function(Mensaje) {
                                return Mensaje.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('mensaje', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
