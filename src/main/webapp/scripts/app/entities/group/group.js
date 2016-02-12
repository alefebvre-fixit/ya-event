'use strict';

angular.module('yaeventApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('group', {
                parent: 'entity',
                url: '/groups',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Groups'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/group/groups.html',
                        controller: 'GroupController'
                    }
                },
                resolve: {
                }
            })
            .state('group.detail', {
                parent: 'entity',
                url: '/group/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Group'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/group/group-detail.html',
                        controller: 'GroupDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Group', function($stateParams, Group) {
                        return Group.get({id : $stateParams.id});
                    }]
                }
            })
            .state('group.new', {
                parent: 'group',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/group/group-dialog.html',
                        controller: 'GroupDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    creationDate: null,
                                    modificationDate: null,
                                    version: null,
                                    status: null,
                                    eventSize: null,
                                    type: null,
                                    name: null,
                                    description: null,
                                    location: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('group', null, { reload: true });
                    }, function() {
                        $state.go('group');
                    })
                }]
            })
            .state('group.edit', {
                parent: 'group',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/group/group-dialog.html',
                        controller: 'GroupDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Group', function(Group) {
                                return Group.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('group', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('group.delete', {
                parent: 'group',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/group/group-delete-dialog.html',
                        controller: 'GroupDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Group', function(Group) {
                                return Group.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('group', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
