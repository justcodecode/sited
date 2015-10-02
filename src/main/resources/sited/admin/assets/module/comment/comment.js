'use strict';

define([
    'angular',
    'comment.module.js',
    'controller/site-comment-controller',
    'controller/site-comment-list-controller'
], function (angular, module) {
    module.config(function ($stateProvider) {
            $stateProvider.state('site.comment', {
                url: '/comment',
                controller: 'SiteCommentController',
                template: '<div ui-view></div>'
            }).state('site.comment.list', {
                url: "/",
                templateUrl: '/admin/assets/module/comment/view/site.comment.list.html',
                controller: 'SiteCommentListController'
            });
        }
    );
});