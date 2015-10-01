'use strict';

define([
    'angular',
    'module/user/user.module',
    'module/user/controller/site-user-controller',
    'module/user/controller/site-user-list-controller',
    'module/user/controller/site-user-update-controller'
], function (angular, module) {
    module.config(function ($stateProvider) {
            $stateProvider.state('site.user', {
                url: '/file',
                controller: 'SiteUserController',
                template: '<div ui-view></div>'
            }).state('site.user.list', {
                url: "/",
                templateUrl: '/admin/assets/module/user/view/site.user.list.html',
                controller: 'SiteUserListController'
            }).state('site.user.create', {
                url: "/create",
                templateUrl: '/admin/assets/module/user/view/site.user.update.html',
                controller: 'SiteUserUpdateController'
            }).state('site.user.update', {
                url: "/:id",
                templateUrl: '/admin/assets/module/user/view/site.user.update.html',
                controller: 'SiteUserUpdateController'
            });
        }
    );
});