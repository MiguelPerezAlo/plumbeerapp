 'use strict';

angular.module('plumbeerappApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-plumbeerappApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-plumbeerappApp-params')});
                }
                return response;
            }
        };
    });
