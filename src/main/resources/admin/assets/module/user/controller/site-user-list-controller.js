'use strict';

define([
    'angular',
    'module/user/user.module'
], function (angular, module) {
    module.controller('SiteUserListController', ['$scope', '$rootScope', '$http', '$stateParams', '$state', function ($scope, $rootScope, $http, $stateParams, $state) {
        $scope.rowCollection = [];
        $scope.isLoading = true;
        $scope.pageSize = 30;
        $scope.tableState = null;
        $scope.totalResults = 0;
        $scope.query = '';

        $scope.loadData = function (tableState) {
            var pagination = tableState.pagination;
            var offset = pagination.start || 0;
            var fetchSize = pagination.number || $scope.pageSize;
            $scope.tableState = tableState;

            $http.get('/admin/api/user/?offset=' + offset + '&fetchSize=' + fetchSize + '&query=' + $scope.query).success(function (result) {
                tableState.pagination.numberOfPages = result.total % $scope.pageSize == 0 ? parseInt(result.total / $scope.pageSize) : parseInt(result.total / $scope.pageSize) + 1;
                $scope.rowCollection = result.data;
                $scope.totalResults = result.total;
                $scope.isLoading = false;
            });
        };


        $scope.filter = function () {
            $scope.isLoading = true;

            var offset = 0;
            var fetchSize = $scope.pageSize;

            $http.get('/admin/api/user/?offset=' + offset + '&fetchSize=' + fetchSize + '&query=' + $scope.query).success(function (result) {
                if ($scope.tableState) {
                    $scope.tableState.pagination.numberOfPages = result.total % $scope.pageSize == 0 ? parseInt(result.total / $scope.pageSize) : parseInt(result.total / $scope.pageSize) + 1;
                }
                $scope.rowCollection = result.data;
                $scope.totalResults = result.total;
                $scope.isLoading = false;
            });
        };

        $scope.deleteUser = function (user) {
            if (confirm('delete user ' + user.username)) {
                $http.delete('/admin/api/user/' + user.id).success(function () {
                    for (var i = 0; i < $scope.rowCollection.length; i++) {
                        if (user.id === $scope.rowCollection[i].id) {
                            $scope.rowCollection.splice(i, 1);
                        }
                    }
                });
            }
        };

        $scope.updateUser = function (user) {
            $state.go('site.user.update', {id: user.id});
        };
    }]);
});