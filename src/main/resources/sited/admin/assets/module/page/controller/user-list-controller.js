(function (angular) {
    angular.module("cms").controller('UserListController', ['$scope', '$http', "$location", function ($scope, $http, $location) {
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

            $http.post('/admin/user/filter?offset=' + offset + '&fetchSize=' + fetchSize, $scope.query).success(function (result) {
                tableState.pagination.numberOfPages = result.total % $scope.pageSize == 0 ? parseInt(result.total / $scope.pageSize - 1) : parseInt(result.total / $scope.pageSize);
                $scope.rowCollection = result.data;
                $scope.isLoading = false;
            });
        };


        $scope.deleteUser = function (user) {
            if (confirm('delete user ' + user.name)) {
                $http.delete('/admin/user?id=' + user.id).success(function () {
                    for (var i = 0; i < $scope.rowCollection.length; i++) {
                        if (user.id === $scope.rowCollection[i].id) {
                            $scope.rowCollection.splice(i, 1);
                        }
                    }
                });
            }
        };

        $scope.updateUser = function (user) {
            $location.search('username', user.username);
            $location.path('/user/update');
        };
    }]);
})(angular);