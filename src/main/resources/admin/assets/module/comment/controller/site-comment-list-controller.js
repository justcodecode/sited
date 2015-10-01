'use strict';

define([
    'angular',
    'module/comment/comment.module'
], function (angular, module) {
    module.controller('SiteCommentListController', ['$scope', '$rootScope', '$http', '$stateParams', '$state', 'FileUploader', function ($scope, $rootScope, $http, $stateParams, $state, FileUploader) {
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

            $http.get('/admin/api/comment/?offset=' + offset + '&fetchSize=' + fetchSize + '&query=' + $scope.query).success(function (result) {
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

            $http.get('/admin/api/comment/?offset=' + offset + '&fetchSize=' + fetchSize + '&query=' + $scope.query).success(function (result) {
                if ($scope.tableState) {
                    $scope.tableState.pagination.numberOfPages = result.total % $scope.pageSize == 0 ? parseInt(result.total / $scope.pageSize) : parseInt(result.total / $scope.pageSize) + 1;
                }
                $scope.rowCollection = result.data;
                $scope.totalResults = result.total;
                $scope.isLoading = false;
            });
        };

        $scope.deleteComment = function (comment) {
            if (confirm('delete comment ' + comment.path)) {
                $http.delete('/admin/api/comment/' + comment.id).success(function () {
                    for (var i = 0; i < $scope.rowCollection.length; i++) {
                        if (comment.id === $scope.rowCollection[i].id) {
                            $scope.rowCollection.splice(i, 1);
                        }
                    }
                });
            }
        };

        $scope.updateComment = function (comment) {
            $state.go('site.comment.update', {id: comment.id});
        };
    }]);
});