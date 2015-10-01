'use strict';

define([
    'angular',
    '../page.module'
], function (angular, module) {
    module.controller('SitePageUpdateController', ['$scope', '$rootScope', '$http', '$stateParams', '$state', function ($scope, $rootScope, $http, $stateParams, $state) {
        $scope.page = {
            path: '',
            tags: []
        };
        $scope.original = angular.copy($scope.page);
        $scope.templates = [];
        $scope.yaml = '';
        $scope.editor = 'summernote';

        if ($state.is('site.page.update')) {
            $http.get('/admin/api/page/' + $stateParams.id).success(function (data) {
                $scope.page = data;
                if (!$scope.page.tags) {
                    $scope.page.tags = [];
                }
                $scope.original = angular.copy($scope.page);
                $scope.yaml = jsyaml.dump($scope.page);
            });
        }

        $http.get('/admin/api/template/').success(function (data) {
            $scope.templates = data;
        });

        $scope.editorOptions = {
            height: 400,
            focus: true,
            airMode: false,
            toolbar: [
                ['edit', ['undo', 'redo']],
                ['headline', ['style']],
                ['style', ['bold', 'italic', 'underline', 'superscript', 'subscript', 'strikethrough', 'clear']],
                ['fontface', ['fontname']],
                ['textsize', ['fontsize']],
                ['fontclr', ['color']],
                ['alignment', ['ul', 'ol', 'paragraph', 'lineheight']],
                ['height', ['height']],
                ['table', ['table']],
                ['insert', ['link', 'picture', 'video', 'hr']],
                ['view', ['fullscreen', 'codeview']],
                ['help', ['help']]
            ]
        };

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

        $scope.aceConfig = {
            useWrapMode: true,
            showGutter: true,
            theme: 'eclipse',
            mode: 'yaml',
            firstLineNumber: 1,
            onLoad: function (_editor) {
                $scope.aceSession = _editor.getSession();
            },
            onChange: function () {
                $scope.yaml = $scope.aceSession.getDocument().getValue();
                $scope.draft = jsyaml.load($scope.yaml);
            }
        };


        $scope.deletePage = function () {
            if (confirm('delete page ' + $scope.page.url())) {
                $http.delete('/admin/page?path=' + encodeURIComponent($scope.page.path)).success(function () {
                    window.location.href = 'index.html?console=true';
                });
            }
        };

        $scope.isPageChanged = function () {
            return !angular.equals($scope.page, $scope.original);
        };

        $scope.resetPage = function () {
            $scope.page = angular.copy($scope.original);
        };

        $scope.previewPage = function () {
            var form = document.createElement('form');
            form.setAttribute('method', 'post');
            form.setAttribute('action', '/admin/draft' + $scope.page.path);
            form.setAttribute('target', '_blank');

            var input = document.createElement("input");
            input.setAttribute("name", "page");
            input.setAttribute("value", JSON.stringify($scope.page));
            form.appendChild(input);

            document.body.appendChild(form);
            form.submit();
            document.body.removeChild(form);
        };

        $scope.updatePage = function () {
            $http.put('/admin/api/page/' + $scope.page._id, $scope.page).success(function (data) {
                alert('saved');
            });
        };

        $scope.createPage = function () {
            $http.post('/admin/api/page', $scope.page).success(function (data) {
                alert('saved');
                $state.go('site.page.update', {
                    id: data._id
                })
            });
        };

    }]);
});