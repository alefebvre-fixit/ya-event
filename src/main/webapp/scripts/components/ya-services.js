angular.module('yaeventApp').factory('YaService',
    ['$rootScope', '$log', 'YaConfig', '$http',
        function($rootScope, $log, YaConfig, $http) {
            var resultService;
            var sanitize = function(){
                startLoading();
                return $http.get(YaConfig.url + '/admin/user/sanitize').then(function (response) {
                    stopLoading();
                    return response.data;
                });
            };

            var isAuthenticated = function(){
                if ($rootScope.userInfo != null){
                    return true;
                } else {
                    var userInfo = getUserInfo();

                    if (userInfo && userInfo.token){
                        installUserInfo(userInfo);
                        return true;
                    }
                    return false;
                }
            };

            var signOut = function(){
                localStorage.removeItem("YaUserInfo");
                delete $http.defaults.headers.common['x-auth-token'];
                delete $rootScope.favorites;
                delete $rootScope.following;
                delete $rootScope.userInfo;
            };

            var getUserInfo = function(){
                return JSON.parse(window.localStorage['YaUserInfo']);
            };

            var setUserInfo = function(userInfo){
                window.localStorage.setItem("YaUserInfo", JSON.stringify(userInfo));
            };

            var installUserInfo = function(userInfo){

                if (userInfo) {
                    window.localStorage.setItem("YaUserInfo", JSON.stringify(userInfo));
                    $http.defaults.headers.common['x-auth-token'] = userInfo.token;
                    $rootScope.favorites = userInfo.followingGroups;
                    $rootScope.following = userInfo.followingUsers;
                    $rootScope.userInfo = userInfo;

                } else {
                    signOut();
                }

            };

            var getUsername = function() {
                return $rootScope.userInfo.user.username;
            };

            var setFavorites = function(favorites){
                $log.debug("setFavorites from service" + favorites);

                $rootScope.favorites = favorites;

                var userInfo = getUserInfo();
                if (userInfo){
                    userInfo.followingGroups = favorites;
                    setUserInfo(userInfo);
                }
            };


            var setFollowing = function(following){
                $log.debug("setFollowing from YaService following=" + following);

                $rootScope.following = following;

                var userInfo = getUserInfo();
                if (userInfo){
                    userInfo.followingUsers = following;
                    setUserInfo(userInfo);
                }

            };


            resultService = {
                installUserInfo: installUserInfo,
                setFavorites: setFavorites,
                setFollowing: setFollowing,
                sanitize: sanitize,
                isFavorite: function(group){
                    if (group){
                        $log.debug("isFavorite " + group.id);
                        return ($rootScope.favorites.indexOf(group.id) >= 0);
                    }
                    return false;
                },
                addFollowing: function(username){
                    $log.debug("addFollowing from service" + username);

                    if (!$rootScope.following){
                        $rootScope.following = [];
                    }
                    $rootScope.following.push(username);

                },
                removeFollowing: function(username){
                    $log.debug("removeFollowing from service" + username);

                    if ($rootScope.following){
                        var index = $rootScope.following.indexOf(username);
                        if (index > -1) {
                            $rootScope.following.splice(index, 1);
                        }
                    }

                },

                isFollowing: function(username){
                    //$log.debug("call isFollowing from YaService following =" + username);
                    if (username && $rootScope.following){
                        return ($rootScope.following.indexOf(username) >= 0);
                    }
                    return false;
                },
                isMine: function(group){
                    if (group){
                        if (group.user){
                            return (group.user.username == $rootScope.user.username);
                        }
                        else if (group.contributor){
                            return (group.contributor == $rootScope.user.username);
                        }
                    }
                    return false;
                },
                getUsername: getUsername,
                isAuthenticated: isAuthenticated,
                signOut: signOut,
                getThemes: function(){
                    return [{type: 'Coffee', selected: false},
                        {type: 'Game', selected: false},
                        {type: 'Music', selected: false},
                        {type: 'Party', selected: false},
                        {type: 'Restaurant', selected: false},
                        {type: 'Shopping', selected: false},
                        {type: 'Soccer', selected: false},
                        {type: 'Video-Game', selected: false}];
                }
            };
            return resultService;
        }]);



