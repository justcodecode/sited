'use strict';

define([
    'angular',
    'file.module.js',
    'controller/site-file-controller',
    'controller/site-file-list-controller',
    'controller/site-file-update-controller'
], function (angular, module) {
    module.config(function ($stateProvider) {
            $stateProvider.state('site.file', {
                url: '/file',
                controller: 'SiteFileController',
                template: '<div ui-view></div>'
            }).state('site.file.list', {
                url: "/",
                templateUrl: '/admin/assets/module/file/view/site.file.list.html',
                controller: 'SiteFileListController'
            }).state('site.file.create', {
                url: "/create",
                templateUrl: '/admin/assets/module/file/view/site.file.update.html',
                controller: 'SiteFileUpdateController'
            }).state('site.file.update', {
                url: "/:id",
                templateUrl: '/admin/assets/module/file/view/site.file.update.html',
                controller: 'SiteFileUpdateController'
            });
        }
    );
});