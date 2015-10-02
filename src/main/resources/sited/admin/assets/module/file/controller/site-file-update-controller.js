'use strict';

define([
    'angular',
    '../file.module.js'
], function (angular, module) {
    module.controller('SiteFileUpdateController', ['$scope', '$rootScope', '$http', '$stateParams', '$state', function ($scope, $rootScope, $http, $stateParams, $state) {
        $scope.file = {
            path: '',
            tags: []
        };
        $scope.original = angular.copy($scope.file);

        if ($state.is('site.file.update')) {
            $http.get('/admin/api/file/' + $stateParams.id).success(function (data) {
                $scope.file = data;

                if (!$scope.file.tags) {
                    $scope.file.tags = [];
                }
                $scope.original = angular.copy($scope.file);
            });
        }

        $scope.imageUpload = function (files, editor) {
            var fd = new FormData();
            fd.append("file", files[0]);

            $http.post('/admin/file/upload', fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            }).success(function (data) {
                editor.insertImage($scope.editorOptions.editable, data.path);
            }).error(function () {
                $scope.errorMessage = 'failed to upload image';
            });
        };

        $scope.deleteFile = function () {
            if (confirm('delete file ' + $scope.file.path)) {
                $http.delete('/admin/api/file/' + $scope.file.id).success(function () {
                    $state.go('site.file.list');
                });
            }
        };

        $scope.isFileChanged = function () {
            return !angular.equals($scope.file, $scope.original);
        };

        $scope.resetFile = function () {
            $scope.file = angular.copy($scope.original);
        };

        $scope.updateFile = function () {
            $http.put('/admin/api/file/' + $scope.file._id, $scope.file).success(function (data) {
                alert('saved');
            });
        };

        $scope.isImageFile = function (file) {
            return true;
        };
    }]);
});