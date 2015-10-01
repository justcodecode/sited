(function (angular) {
    angular.module("cms").controller('RoleListController', ['$scope', '$http', "$location", function ($scope, $http, $location) {
        $scope.rowCollection = [];
        $scope.isLoading = true;
        $scope.pageSize = 30;
        $scope.query = {
            template: '',
            query: '',
            lastUpdateStartTime: null,
            lastUpdateEndTime: null
        };

        $scope.loadData = function (tableState) {
            var pagination = tableState.pagination;

            var offset = pagination.start || 0;
            var fetchSize = pagination.number || $scope.pageSize;

            $http.post('/admin/role/list?offset=' + offset + '&fetchSize=' + fetchSize, $scope.query).success(function (result) {
                tableState.pagination.numberOfPages = result.total % $scope.pageSize == 0 ? parseInt(result.total / $scope.pageSize - 1) : parseInt(result.total / $scope.pageSize);
                $scope.rowCollection = result.data;
                $scope.isLoading = false;
            });

        };


        $scope.deleteRole = function (role) {
            if (confirm('delete tag ' + role.name)) {
                $http.delete('/admin/role?id=' + role.id).success(function () {
                    for (var i = 0; i < $scope.rowCollection.length; i++) {
                        if (user.id === $scope.rowCollection[i].id) {
                            $scope.rowCollection.splice(i, 1);
                        }
                    }
                });
            }
        };

        $scope.updateRole = function (role) {
            $location.search('id', role.id);
            $location.path('/role/update');
        };
    }]);
})(angular);