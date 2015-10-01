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
        jsyaml: 'lib/js/js-yaml.min',
        loadingBar: 'lib/js/loading-bar.min',
        summernote: 'lib/js/summernote.min',
        ngSummernote: 'lib/js/angular-summernote.min',
        smartTable: 'lib/js/smart-table.min',
        ngFileUpload: 'lib/js/angular-file-upload.min'
    },
    shim: {
        'angular': {'exports': 'angular'},
        'jsyaml': {'exports': 'jsyaml'},
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
        'ngCookies',
        'ngAnimate',
        'uiRouter',
        'uiSelect',
        'ace',
        'uiAce',
        'smartTable',
        'bootstrap',
        'ngSummernote',
        'loadingBar',
        'app'
    ], function (angular) {
        angular.element().ready(function () {
            angular.bootstrap(document, ['app']);
        });
    }
);