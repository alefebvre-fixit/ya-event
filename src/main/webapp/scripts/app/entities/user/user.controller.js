'use strict';

angular.module('yaeventApp')
    .controller('UserListController', function ($scope, $state, UserService) {

        $scope.users = [];
        
        $scope.loadAll = function() {
            UserService.getUsers().then(function (users) {
                $scope.users = users;
            });
  
        };
        $scope.loadAll();

        $scope.refresh = function () {
            $scope.loadAll();
        };

    });


angular.module('yaeventApp')
.controller('UserController', function ($scope, $state, username, UserService) {

    $scope.summary = {groupSize : '-', followingSize : '-', followerSize : '-', user: {}, followingGroups : []};

    var loadUser = function(username){
        UserService.getUser(username).then(function (user) {
            $scope.summary.user = user;
        });

        UserService.getFollowingGroupsSize(username).then(function (groupSize) {
            $scope.summary.groupSize = groupSize;
        });

        UserService.getFollowersSize(username).then(function (followerSize) {
            $scope.summary.followerSize = followerSize;
        });

        UserService.getFollowingSize(username).then(function (followingSize) {
            $scope.summary.followingSize = followingSize;
        });

        UserService.getFollowingGroups(username).then(function (groups) {
            $scope.summary.followingGroups = groups;
        });

    };
    
    loadUser(username);

    $scope.refresh = function () {
    	loadUser(username);
    };

});