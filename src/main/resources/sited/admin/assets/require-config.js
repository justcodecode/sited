require.config({
    paths: {
        jquery: 'lib/js/jquery-1.11.2.min.js',
        bootstrap: 'lib/js/bootstrap.min.js',
        angular: 'lib/js/angular.min.js',
        ngCookies: 'lib/js/angular-cookies.min.js',
        ngAnimate: 'lib/js/angular-animate.min.js',
        uiRouter: 'lib/js/angular-ui-router.min.js',
        uiSelect: 'lib/js/select.min.js',
        ace: 'lib/js/ace/ace',
        uiAce: 'lib/js/ui-ace',
        jsyaml: 'lib/js/js-yaml.min.js',
        loadingBar: 'lib/js/loading-bar.min.js',
        summernote: 'lib/js/summernote.min.js',
        ngSummernote: 'lib/js/angular-summernote.min.js',
        smartTable: 'lib/js/smart-table.min.js',
        ngFileUpload: 'lib/js/angular-file-upload.min.js'
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
        'lib/js/ace/ace',
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