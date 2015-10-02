(function (angular) {
    angular.module("cms").controller('RoleUpdateController', ['$scope', '$rootScope', '$http', '$stateParams', '$location', '$state', function ($scope, $rootScope, $http, $stateParams, $location, $state) {
        $scope.page.hide();
        $scope.permissios = [];

        $http.post('/admin/permission/list?offset=' + 0 + '&fetchSize=' + 1000, $scope.query).success(function (result) {
            $scope.permissios = result.data;
        });

        if ($location.search().id) {
            $http.get('/admin/role?id=' + $location.search().id).success(function (data) {
                $scope.role = data;
            });
        }
        $scope.save = function () {
            if ($location.search().id) {
                $http.put('/admin/role', $scope.role).success(function () {
                    $state.go('user.list');
                });
            } else {
                $http.post('/admin/role', $scope.role).success(function () {
                    $state.go('user.list');
                });
            }
        };
        $scope.addPermission = function () {
            $http.post("/admin/permission", $scope.permission).success(function () {
                $scope.permissios.push($scope.permission);
            });
        };

        $scope.isValid = function () {
            return $scope.role.name && $scope.role.permission;
        };

        $scope.cancel = function () {
            $state.go('console');
        }
    }]);
})(angular);