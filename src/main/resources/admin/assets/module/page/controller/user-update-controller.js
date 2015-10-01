(function (angular) {
    angular.module("cms").controller('UserUpdateController', ['$scope', '$rootScope', '$http', '$stateParams', '$location', '$state', function ($scope, $rootScope, $http, $stateParams, $location, $state) {
        $scope.page.hide();
        $scope.roles = [];

        $http.post('/admin/role/list?offset=' + 0 + '&fetchSize=' + 1000, $scope.query).success(function (result) {
            $scope.roles = result.data;
        });

        if ($location.search().username) {
            $http.get('/admin/user?username=' + $location.search().username).success(function (data) {
                $scope.user = data;
            });
        }
        $scope.save = function () {
            if ($location.search().username) {
                $http.put('/admin/user', $scope.user).success(function () {
                    $state.go('user.list');
                });
            } else {
                $http.post('/admin/user', $scope.user).success(function () {
                    $state.go('user.list');
                });
            }
        };


        $scope.isValid = function () {
            return $scope.user.username && $scope.user.email && $scope.user.password === $scope.password.confirm;
        };

        $scope.cancel = function () {
            $state.go('console');
        }
    }]);
})(angular);