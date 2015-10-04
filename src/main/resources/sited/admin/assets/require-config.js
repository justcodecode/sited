require.config({
    paths: {
        jquery: 'lib/js/jquery-1.11.2.min',
        bootstrap: 'lib/js/bootstrap.min',
        angular: 'lib/js/angular.min',
        ngCookies: 'lib/js/angular-cookies.min',
        ngAnimate: 'lib/js/angular-animate.min',
        uiRouter: 'lib/js/angular-ui-router.min',
        uiSelect: 'lib/js/select.min',
        ace: 'lib/js/ace/ace',
        uiAce: 'lib/js/ui-ace',
        loadingBar: 'lib/js/loading-bar.min',
        summernote: 'lib/js/summernote.min',
        ngSummernote: 'lib/js/angular-summernote.min',
        smartTable: 'lib/js/smart-table.min',
        ngFileUpload: 'lib/js/angular-file-upload.min'
    },
    shim: {
        'angular': {'exports': 'angular'},
        'ngCookies': ['angular'],
        'ngAnimate': ['angular'],
        'uiRouter': ['angular'],
        'uiSelect': ['angular'],
        'loadingBar': ['angular'],
        'smartTable': ['angular'],
        'summernote': {
            deps: ['jquery', 'bootstrap'],
            exports: 'summernote'
        },
        'uiAce': ['angular', 'ace'],
        'ngSummernote': ['jquery', 'bootstrap', 'summernote'],
        'bootstrap': ['jquery'],
        'ngFileUpload': ['angular']
    },
    map: {
        '*': {
            'css': 'lib/js/require-css.min'
        }
    }
});

require([
        'angular',
        'jquery',
        'bootstrap',
        'ngCookies',
        'ngAnimate',
        'uiRouter',
        'uiSelect',
        'ace',
        'uiAce',
        'summernote',
        'ngSummernote',
        'smartTable',
        'loadingBar',
        'ngFileUpload',
        'css!lib/css/bootstrap.min',
        'css!lib/css/font-awesome.min',
        'css!lib/css/loading-bar.min',
        'css!lib/css/summernote',
        'css!lib/css/summernote-bs3',
        'css!lib/css/select.min',
        'css!lib/css/theme',
        'app'
    ], function (angular) {
        angular.element().ready(function () {
            angular.bootstrap(document, ['app']);
        });
    }
);