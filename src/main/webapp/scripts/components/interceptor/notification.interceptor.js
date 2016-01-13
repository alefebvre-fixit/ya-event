 'use strict';

angular.module('yaeventApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-yaeventApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-yaeventApp-params')});
                }
                return response;
            }
        };
    });
