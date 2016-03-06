angular.module('yaeventApp').factory('GroupService',
    ['$http', 'YaConfig', 'YaService', 'Group', '$log',
        function($http, YaConfig, YaService, Group, $log) {

            var resultService;
            resultService = {
                getGroupsFromCache: function () {
                    return  Group.getAll();
                },
                getGroups: function () {
                    //return Group.refreshAll();
                    
                    return $http.get(YaConfig.url + '/groups').then(function (response) {
                        return response.data;
                    });
                    /*
                    return $http.get(YaConfig.url + '/groups').then(function (response) {
                        Group.inject(response.data);
                        return response.data;
                    });
                    */
                },
                getGroupsByOwner: function (username) {
                    return $http.get(YaConfig.url + '/users/' + username + '/groups').then(function (response) {
                        return response.data;
                    });
                },
                getGroup: function (groupId) {
                	/*
                    return Group.find(groupId).then(function (group) {
                        return group;
                    });
                    */
                    return $http.get(YaConfig.url + '/groups/' + groupId).then(function (response) {
                        return response.data;
                    });
                },
                saveSponsors: function (group, sponsors) {
                    var sponsornames = [];
                    var arrayLength = Object.keys(sponsors).length;

                    for (var i = 0; i < arrayLength; i++) {
                        sponsornames.push(sponsors[i].username);
                    }

                    $log.debug(sponsornames);

                    return $http.put(YaConfig.url + '/groups/' + group.id + '/sponsors', sponsornames).then(function (response) {
                        Group.inject(response.data);
                        return response.data;
                    });
                },
                saveGroup: function (group) {
                    
                	/*
                	if (group.id){
                        return Group.update(group.id, group);
                    } else {
                        return Group.create(group);
                    }
                    */
                    return $http.post(YaConfig.url + '/groups', group).then(function (response) {
                        return response.data;
                    });
                },
                followGroup: function (group) {
                    return $http.post(YaConfig.url + '/groups/' + group.id + '/follow').then(function (response) {
                        return response.data;
                    });
                },
                getEventSize: function (groupId) {
                    return $http.get(YaConfig.url + '/groups/' + groupId + '/events/size').then(function (response) {
                        return response.data;
                    });
                },
                getEvents: function (groupId) {
                    return $http.get(YaConfig.url + '/groups/' + groupId + '/events').then(function (response) {
                        return response.data;
                    });
                },
                getEventTimeline: function (groupId) {
                    return $http.get(YaConfig.url + '/groups/' + groupId + '/events/timeline').then(function (response) {
                        return response.data;
                    });
                },
                getLastEvents: function (groupId) {
                    return $http.get(YaConfig.url + '/groups/' + groupId + '/events/last').then(function (response) {
                        return response.data;
                    });
                },
                getFollowerSize: function (groupId) {
                    return $http.get(YaConfig.url + '/groups/' + groupId + '/followers/size').then(function (response) {
                        return response.data;
                    });
                },
                getFollowers: function (groupId) {
                    return $http.get(YaConfig.url + '/groups/' + groupId + '/followers').then(function (response) {
                        return response.data;
                    });
                },
                getSponsors: function (groupId) {
                    return $http.get(YaConfig.url + '/groups/' + groupId + '/sponsors').then(function (response) {
                        return response.data;
                    });
                },
                getCommentSize: function (groupId) {
                    return $http.get(YaConfig.url + '/groups/' + groupId + '/comments/size').then(function (response) {
                        return response.data;
                    });
                },
                getComments: function (groupId) {
                    return $http.get(YaConfig.url + '/groups/' + groupId + '/comments').then(function (response) {
                        return response.data;
                    });
                },
                postComment: function (groupId, content) {
                    return $http.post(YaConfig.url + '/groups/' + groupId + '/comments/' + content).then(function (response) {
                        return response.data;
                    });
                },
                unfollowGroup: function (group) {
                    return $http.post(YaConfig.url + '/groups/' + group.id + '/unfollow').then(function (response) {
                        return response.data;
                    });
                },
                deleteGroup: function (group) {
                    //return Group.destroy(group.id);
                    
                    return $http.post(YaConfig.url + '/groups/' + group.id + '/delete').then(function (response) {
                        return response.data;
                    });
                    
                },
                instanciateGroup: function () {
                    return $http.get(YaConfig.url + '/groups/new').then(function (response) {
                        return response.data;
                    });
                },
                canEdit: function(group){
                    $log.debug("call canUpdate from GroupService canUpdate =" + YaService.getUsername());
                    if (group){
                        if (group.username == YaService.getUsername()){
                            return true;
                        }
                        return (group.sponsors && group.sponsors.indexOf(YaService.getUsername()) > 0);
                    }
                    return false;
                }
            };
            return resultService;
        }]);


