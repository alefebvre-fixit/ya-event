'use strict';

angular.module('yaeventApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('users', {
                parent: 'entity',
                url: '/users',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Users'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/user/users.html',
                        controller: 'UserListController'
                    }
                },
                resolve: {
                }
            })
            .state('user-profile', {
                url: '/user/{username}/profile',
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'User'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/user/user-profile.html',
                        controller: 'UserController'
                    }
                },
                resolve: {
                	username: function ($stateParams) {
                        return $stateParams.username;
                    }
                }
            })
            ;
    });
