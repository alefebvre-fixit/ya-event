angular.module('yaeventApp').factory('UserService', ['$http', '$log', 'YaConfig','YaService',
                            function($http, $log, YaConfig, YaService) {

    var resultService;
    resultService = {
        getUser: function (username) {
        	/*
            return User.find(username);
            */
            return $http.get(YaConfig.url + '/users/' + username ).then(function (response) {
                return response.data;
            });
        },
        saveProfile: function (profile) {
            return $http.post(YaConfig.url + '/profile', profile).then(function (response) {
                return response.data;
            });
        },
        signupUser: function (signup) {
            return $http.post(YaConfig.url + '/signup', signup);
        },
        signinUser: function (signin) {
            return $http.post(YaConfig.url + '/signin', signin);
        },
        signInGoogle: function (signin) {
            return $http.post(YaConfig.url + '/signin/google', signin);
        },
        signInFacebook: function (signin) {
            return $http.post(YaConfig.url + '/signin/facebook', signin);
        },
        signInEmail: function (signin) {
            return $http.post(YaConfig.url + '/signin/email', signin);
        },
        follow: function (username) {
            return $http.post(YaConfig.url + '/users/'+ username +'/follow').then(function (response) {
                return response.data;
            });
        },
        unfollow: function (username) {
            return $http.post(YaConfig.url + '/users/'+ username +'/unfollow').then(function (response) {
                return response.data;
            });
        },
        getFollowingGroups: function (username) {
            return $http.get(YaConfig.url + '/users/' + username + '/groups/following').then(function (response) {
                return response.data;
            });
        },
        getFollowingGroupsSize: function (username) {
            return $http.get(YaConfig.url + '/users/' + username + '/groups/following/size').then(function (response) {
                $log.log('#### Expected = 33 ###' + response.data);
                return response.data;
            });
        },
        getFollowingGroupIds: function (username) {
            return $http.get(YaConfig.url + '/users/' + username + '/groups/following/id').then(function (response) {
                return response.data;
            });
        },
        getFollowers: function (username) {
            return $http.get(YaConfig.url + '/users/' + username + '/followers').then(function (response) {
                return response.data;
            });
        },
        getFollowersSize: function (username) {
            return $http.get(YaConfig.url + '/users/' + username + '/followers/size').then(function (response) {
                $log.log('#### Expected = 22 ###' + response.data);
                return response.data;
            });
        },
        getFollowingSize: function (username) {
            return $http.get(YaConfig.url + '/users/' + username + '/following/size').then(function (response) {
                $log.log('#### Expected = 11 ###' + response.data);
                return response.data;
            });
        },
        getFollowing: function (username) {
            return $http.get(YaConfig.url + '/users/' + username + '/following').then(function (response) {
                return response.data;
            });
        },
        getFollowingNames: function (username) {
            return $http.get(YaConfig.url + '/users/' + username + '/following/name').then(function (response) {
                return response.data;
            });
        },
        getUserDiscovery: function () {
            return $http.get(YaConfig.url + '/users/discovery').then(function (response) {
                User.inject(response.data);
                return response.data;
            });
        },
        getUsers: function () {
            return User.findAll();
        },
        canEdit: function(user){
            //$log.debug("call canUpdate from UserService canUpdate =" + YaService.getUsername());
            if (user){
                if (user.username == YaService.getUsername()){
                    return true;
                }
            }
            return false;
        },
        getAccessToken: function(){
            return YaConfig.access_token;
        }
    };

    return resultService;
}]);


