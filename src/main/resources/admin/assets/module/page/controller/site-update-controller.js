(function (angular) {
    angular.module("cms").controller('SiteController', ['$scope', '$rootScope', '$http', '$stateParams', '$location', '$state', function ($scope, $rootScope, $http, $stateParams, $location, $state) {
        $scope.profile = {};
        $scope.original = angular.copy($scope.profile);
        $scope.yaml = '';

        $http.get('/admin/profile').success(function (result) {
            $scope.profile = result;
            $scope.original = angular.copy($scope.profile);
            $scope.yaml = jsyaml.dump($scope.profile);
        });


        $scope.save = function () {
            $http.post('/admin/profile', $scope.profile).success(function (result) {
                $scope.profile = result;
                $scope.original = angular.copy($scope.profile);
                $scope.yaml = jsyaml.dump($scope.profile);

                alert('Profile updated');
            });
        };

        $scope.isChanged = function () {
            return !angular.equals($scope.profile, $scope.original);
        };

        $scope.cancel = function () {
            if (confirm('Reset changes?')) {
                $scope.profile = angular.copy($scope.original);
            }
        };

        $scope.restart = function () {
            alert('not implement yet');
        };

        $scope.aceConfig = {
            useWrapMode: true,
            showGutter: true,
            theme: 'eclipse',
            mode: 'yaml',
            firstLineNumber: 1,
            require: ['ace/ext/language_tools'],
            onLoad: function (_editor) {
                $scope.aceSession = _editor.getSession();
            },
            onChange: function () {
                $scope.yaml = $scope.aceSession.getDocument().getValue();
                $scope.profile = jsyaml.load($scope.yaml);
            },
            advanced: {
                enableSnippets: true,
                enableBasicAutocompletion: true,
                enableLiveAutocompletion: true
            }
        };
    }]);
})(angular);