'use strict';

define([
    'angular',
    '../user.module.js'
], function (angular, module) {
    module.controller('SiteUserUpdateController', ['$scope', '$rootScope', '$http', '$stateParams', '$state', function ($scope, $rootScope, $http, $stateParams, $state) {
        $scope.user = {
            roles: []
        };
        $scope.original = angular.copy($scope.user);

        if ($state.is('site.user.update')) {
            $http.get('/admin/api/user/' + $stateParams.id).success(function (data) {
                $scope.user = data;

                if (!$scope.user.roles) {
                    $scope.user.roles = [];
                }

                $scope.original = angular.copy($scope.user);
            });
        }

        $scope.deleteUser = function () {
            if (confirm('delete user ' + $scope.user.username)) {
                $http.delete('/admin/api/user/' + $scope.user.id).success(function () {
                    $state.go('site.user.list');
                });
            }
        };

        $scope.isUserChanged = function () {
            return !angular.equals($scope.user, $scope.original);
        };

        $scope.resetUser = function () {
            $scope.user = angular.copy($scope.original);
        };

        $scope.updateUser = function () {
            $http.put('/admin/api/user/' + $scope.user.id, $scope.user).success(function (data) {
                alert('saved');
            });
        };
    }]);
});