'use strict';

angular.module('yaeventApp')
    .controller('GroupDetailController', function ($scope, $rootScope, $log, groupId, GroupService, UserService) {
        
        $scope.summary = {followerSize : '-', commentSize : '-', comments: [], lastEvents: [], owner: {}, sponsors:[]};
    	
        GroupService.getGroup(groupId).then(function (group) {
            $log.debug("ViewGroupController getGroup is called groupId=" + groupId);
            $scope.group = group;
            
            GroupService.getEventSize(groupId).then(function (data) {
                $scope.summary.eventSize = data;
            });

            GroupService.getLastEvents(groupId).then(function (data) {
                $scope.summary.lastEvents = data;
            });

            GroupService.getFollowerSize(groupId).then(function (data) {
                $scope.summary.followerSize = data;
            });
            
            UserService.getUser(group.username).then(function (user) {
                $scope.owner = user;
            });
        });
        
        /*
        $scope.load = function (id) {
            Group.get({id: id}, function(result) {
                $scope.group = result;
                console.log('Hello1');
                UserService.getUser(result.username).then(function (user) {
                	console.log('Hello3');
                    $scope.owner = user;
                });
            });
        };
        
        UserService.getUser(entity.username).then(function (user) {
        	console.log('Hello3');
            $scope.owner = user;
        });
        
        var unsubscribe = $rootScope.$on('yaeventApp:groupUpdate', function(event, result) {
            $scope.group = result;
        });
        $scope.$on('$destroy', unsubscribe);
        */
        
      

    });
