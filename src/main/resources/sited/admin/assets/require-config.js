require.config({
    paths: {
        jquery: '../../assets/js/jquery-1.11.2.min',
        bootstrap: '../../assets/js/bootstrap.min',
        angular: '../../assets/js/angular.min',
        ngCookies: '../../assets/js/angular-cookies.min',
        ngAnimate: '../../assets/js/angular-animate.min',
        uiRouter: '../../assets/js/angular-ui-router.min',
        uiSelect: '../../assets/js/select.min',
        ace: '../../assets/js/ace/ace',
        uiAce: '../../assets/js/ui-ace',
        loadingBar: '../../assets/js/loading-bar.min',
        summernote: '../../assets/js/summernote.min',
        ngSummernote: '../../assets/js/angular-summernote.min',
        smartTable: '../../assets/js/smart-table.min',
        ngFileUpload: '../../assets/js/angular-file-upload.min'
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
            'css': '../../assets/js/require-css.min'
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
        'css!../../assets/css/bootstrap.min',
        'css!../../assets/css/font-awesome.min',
        'css!../../assets/css/loading-bar.min',
        'css!../../assets/css/summernote',
        'css!../../assets/css/summernote-bs3',
        'css!../../assets/css/select.min',
        'css!../../assets/css/theme',
        'app'
    ], function (angular) {
        angular.element().ready(function () {
            angular.element('.spinner').hide();
            angular.bootstrap(document, ['app']);
        });
    }
);