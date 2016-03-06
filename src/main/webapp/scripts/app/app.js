'use strict';

angular.module('yaeventApp', ['LocalStorageModule', 
    'ngResource', 'ngCookies', 'ngAria', 'ngCacheBuster', 'ngFileUpload',
    // jhipster-needle-angularjs-add-module JHipster will add new module
    'ui.bootstrap', 'ui.router',  'infinite-scroll', 'angular-loading-bar'])

    .run(function ($rootScope, $location, $window, $http, $state,  Auth, Principal, ENV, VERSION) {
        
        $rootScope.ENV = ENV;
        $rootScope.VERSION = VERSION;
        $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
            $rootScope.toState = toState;
            $rootScope.toStateParams = toStateParams;

            if (Principal.isIdentityResolved()) {
                Auth.authorize();
            }
            
        });

        $rootScope.$on('$stateChangeSuccess',  function(event, toState, toParams, fromState, fromParams) {
            var titleKey = 'yaevent' ;

            // Remember previous state unless we've been redirected to login or we've just
            // reset the state memory after logout. If we're redirected to login, our
            // previousState is already set in the authExpiredInterceptor. If we're going
            // to login directly, we don't want to be sent to some previous state anyway
            if (toState.name != 'login' && $rootScope.previousStateName) {
              $rootScope.previousStateName = fromState.name;
              $rootScope.previousStateParams = fromParams;
            }

            // Set the page title key to the one configured in state or use default one
            if (toState.data.pageTitle) {
                titleKey = toState.data.pageTitle;
            }
            $window.document.title = titleKey;
        });
        
        $rootScope.back = function() {
            // If previous state is 'activate' or do not exist go to 'home'
            if ($rootScope.previousStateName === 'activate' || $state.get($rootScope.previousStateName) === null) {
                $state.go('home');
            } else {
                $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
            }
        };
    })
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider,  httpRequestInterceptorCacheBusterProvider, AlertServiceProvider) {
        // uncomment below to make alerts look like toast
        //AlertServiceProvider.showAsToast(true);

        //Cache everything except rest api requests
        httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);

        $urlRouterProvider.otherwise('/');
        $stateProvider.state('site', {
            'abstract': true,
            views: {
                'navbar@': {
                    templateUrl: 'scripts/components/navbar/navbar.html',
                    controller: 'NavbarController'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ]
            }
        });

        $httpProvider.interceptors.push('errorHandlerInterceptor');
        $httpProvider.interceptors.push('authExpiredInterceptor');
        $httpProvider.interceptors.push('authInterceptor');
        $httpProvider.interceptors.push('notificationInterceptor');
        
    })
    // jhipster-needle-angularjs-add-config JHipster will add new application configuration
    .config(['$urlMatcherFactoryProvider', function($urlMatcherFactory) {
        $urlMatcherFactory.type('boolean', {
            name : 'boolean',
            decode: function(val) { return val == true ? true : val == "true" ? true : false },
            encode: function(val) { return val ? 1 : 0; },
            equals: function(a, b) { return this.is(a) && a === b; },
            is: function(val) { return [true,false,0,1].indexOf(val) >= 0 },
            pattern: /bool|true|0|1/
        });
    }]);





//angular.module('yaeventApp').constant('YaConfig', {context : 'production', url : 'https://calm-headland-3125.herokuapp.com/api', enablePlugin : true, enableDebug : false});
angular.module('yaeventApp').constant('YaConfig', {context : 'test', url : 'http://localhost:8080/api', enablePlugin : false, enableDebug : true, access_token: 'CAAVKQaHMWpIBAAaNWd5bybmU7raLvONarxkwZCfdItbj6PukTEW1zpXqdh2kvb8pPQCF97lhviWlJ3far0urd8mZBquV7yZCZCbLuy65GMZAteCRzDlZCkIc3x6Ef2HNclPnze5p1l7g29uBZBbZBXLZAzXZA1ii4PtZB2EGbtUwfqxCecuMY9kkixP6pdvH7F1pqQZD'});
//angular.module('ya-app').constant('YaConfig', {context : 'test', url : 'http://localhost:9000/api', enablePlugin : false, enableDebug : true, access_token: 'CAAVKQaHMWpIBAAaNWd5bybmU7raLvONarxkwZCfdItbj6PukTEW1zpXqdh2kvb8pPQCF97lhviWlJ3far0urd8mZBquV7yZCZCbLuy65GMZAteCRzDlZCkIc3x6Ef2HNclPnze5p1l7g29uBZBbZBXLZAzXZA1ii4PtZB2EGbtUwfqxCecuMY9kkixP6pdvH7F1pqQZD'});
//angular.module('ya-app').constant('YaConfig', {context : 'production', url : 'http://vast-gorge-2883.herokuapp.com/api', enablePlugin : true, enableDebug : false});
//angular.module('ya-app').constant('YaConfig', {context : 'simulator', url : 'http://10.0.2.2:9000/api', enablePlugin : true, enableDebug : false});



