'use strict';

define([
    'angular',
    'module/file/file.module',
], function (angular, module) {
    module.controller('SiteFileListController', ['$scope', '$rootScope', '$http', '$stateParams', '$state', 'FileUploader', function ($scope, $rootScope, $http, $stateParams, $state, FileUploader) {
        $scope.rowCollection = [];

        $scope.fileUploader = new FileUploader({
            url: '/admin/file/upload'
        });

        $scope.fileUploader.onAfterAddingFile = function (fileItem) {
            console.log('upload file ' + fileItem.name);
            fileItem.upload();
        };

        $scope.fileUploader.onSuccessItem = function (fileItem, response) {
            $scope.rowCollection.splice(0, 0, response);
        };

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

            $http.get('/admin/api/file/?offset=' + offset + '&fetchSize=' + fetchSize + '&query=' + $scope.query).success(function (result) {
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

            $http.get('/admin/api/file/?offset=' + offset + '&fetchSize=' + fetchSize + '&query=' + $scope.query).success(function (result) {
                if ($scope.tableState) {
                    $scope.tableState.pagination.numberOfPages = result.total % $scope.pageSize == 0 ? parseInt(result.total / $scope.pageSize) : parseInt(result.total / $scope.pageSize) + 1;
                }
                $scope.rowCollection = result.data;
                $scope.totalResults = result.total;
                $scope.isLoading = false;
            });
        };

        $scope.deleteFile = function (file) {
            if (confirm('delete file ' + file.path)) {
                $http.delete('/admin/api/file/' + file.id).success(function () {
                    for (var i = 0; i < $scope.rowCollection.length; i++) {
                        if (file.id === $scope.rowCollection[i].id) {
                            $scope.rowCollection.splice(i, 1);
                        }
                    }
                });
            }
        };

        $scope.updateFile = function (file) {
            $state.go('site.file.update', {id: file.id});
        };
    }]);
});