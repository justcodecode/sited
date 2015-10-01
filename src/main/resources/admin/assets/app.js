'use strict';

define([
    'angular',
    'jquery',
    'bootstrap',
    'ngCookies',
    'ngAnimate',
    'uiRouter',
    'uiSelect',
    'ace',
    'uiAce',
    'jsyaml',
    'summernote',
    'ngSummernote',
    'smartTable',
    'loadingBar',
    'ngFileUpload',
    'module/page/page',
    'module/file/file',
    'module/user/user',
    'module/comment/comment',
    'css!lib/css/bootstrap.min',
    'css!lib/css/font-awesome.min',
    'css!lib/css/loading-bar.min',
    'css!lib/css/summernote',
    'css!lib/css/summernote-bs3',
    'css!lib/css/select.min',
    'css!lib/css/theme'
], function (angular) {

    var module = angular.module("app", [
        'ngCookies',
        'ui.router',
        'ui.select',
        'ui.ace',
        'smart-table',
        'summernote',
        'angular-loading-bar',
        'angularFileUpload',
        'page',
        'user',
        'file',
        'comment'
    ]);

    module.config(function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/site/page/");

        $stateProvider
            .state('site', {
                url: "/site",
                templateUrl: '/admin/assets/site.html',
                resolve: {
                    site: function ($http) {
                        return $http.get('/admin/api/site');
                    }
                }
            });
    });

    module.run(['$rootScope', '$http', '$filter', '$state', '$cookies', '$templateCache', function ($rootScope, $http, $filter, $state, $cookies, $templateCache) {
        $rootScope.$state = $state;
        $templateCache.put('st-pagination.html', '<nav ng-if="pages.length >= 2">\n    <ul class="pagination pagination-sm">\n        <li><a ng-click="selectPage(1)">&laquo;</a>\n        </li>\n\n        <li ng-repeat="n in [currentPage, numPages] | range " ng-class="{active: n == currentPage}">\n            <a ng-click="selectPage(n)">{{n}}</a>\n        </li>\n\n        <li><a ng-click="selectPage(numPages)">&raquo;</a></li>\n    </ul>\n</nav>');

    }]);

    module.directive('tableCheckbox', function () {
        return {
            require: '^stTable',
            template: '<input type="checkbox"/>',
            scope: {
                row: '=csSelect'
            },
            link: function (scope, element, attr, ctrl) {

                element.bind('change', function (evt) {
                    scope.$apply(function () {
                        ctrl.select(scope.row, 'multiple');
                    });
                });

                scope.$watch('row.isSelected', function (newValue, oldValue) {
                    if (newValue === true) {
                        element.parent().addClass('st-selected');
                    } else {
                        element.parent().removeClass('st-selected');
                    }
                });
            }
        };
    });

    module.filter('range', function () {
        return function (input) {
            var start = input[0] - 10 > 1 ? input[0] - 10 : 1;
            var end = input[0] + 10 < input[1] ? input[0] + 10 : input[1];

            var result = [];
            for (var i = start; i <= end; i++) {
                result.push(i);
            }

            return result;
        };
    });

    return module;
});


