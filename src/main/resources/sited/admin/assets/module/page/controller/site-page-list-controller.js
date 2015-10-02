'use strict';

define([
    'angular',
    '../page.module.js'
], function (angular, module) {
    module.controller('SitePageListController', ['$scope', '$rootScope', '$http', '$stateParams', '$state', function ($scope, $rootScope, $http, $stateParams, $state) {
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

            //Hold table state for filter
            $scope.tableState = tableState;

            $http.get('/admin/api/page/?offset=' + offset + '&fetchSize=' + fetchSize + '&query=' + $scope.query).success(function (result) {
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

            $http.get('/admin/api/page/?offset=' + offset + '&fetchSize=' + fetchSize + '&query=' + $scope.query).success(function (result) {
                if ($scope.tableState) {
                    $scope.tableState.pagination.numberOfPages = result.total % $scope.pageSize == 0 ? parseInt(result.total / $scope.pageSize) : parseInt(result.total / $scope.pageSize) + 1;
                }
                $scope.rowCollection = result.data;
                $scope.totalResults = result.total;
                $scope.isLoading = false;
            });
        };

        $scope.templates = [];
        $http.get('/admin/api/template/').success(function (data) {
            $scope.templates = data;
        });

        $scope.deletePage = function (page) {
            if (confirm('delete page ' + page.path)) {
                $http.delete('/admin/api/page/' + page._id).success(function () {
                    for (var i = 0; i < $scope.rowCollection.length; i++) {
                        if (page._id === $scope.rowCollection[i]._id) {
                            $scope.rowCollection.splice(i, 1);
                        }
                    }
                });
            }
        };

        $scope.updatePage = function (page) {
            $state.go('site.page.update', {id: page._id});
        };

        $scope.fullIndex = function () {
            if (confirm('rebuild index?')) {
                $http.post('/admin/page/index').success(function () {
                    alert('index job is running');
                });
            }
        }
    }]);
});