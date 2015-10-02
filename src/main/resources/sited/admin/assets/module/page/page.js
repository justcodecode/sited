'use strict';

define([
    'angular',
    'page.module.js',
    'css!css/page',
    'controller/site-page-controller',
    'controller/site-page-list-controller',
    'controller/site-page-update-controller'
], function (angular, module) {
    module.config(function ($stateProvider) {
            $stateProvider.state('site.page', {
                url: '/page',
                controller: 'SitePageController',
                template: '<div ui-view></div>'
            }).state('site.page.list', {
                url: "/",
                templateUrl: '/admin/assets/module/page/view/site.page.list.html',
                controller: 'SitePageListController'
            }).state('site.page.create', {
                url: "/create",
                templateUrl: '/admin/assets/module/page/view/site.page.update.html',
                controller: 'SitePageUpdateController'
            }).state('site.page.update', {
                url: "/:id",
                templateUrl: '/admin/assets/module/page/view/site.page.update.html',
                controller: 'SitePageUpdateController'
            });
        }
    );
});