(function (angular) {
    angular.module("cms").controller('TemplateListController', ['$scope', '$http', "$location", function ($scope, $http, $location) {
        $scope.treeData = {
            children: []
        };
        $scope.selectedNode = {};
        $scope.file = '';
        $scope.original = angular.copy($scope.file);

        $http.get("/admin/template/tree").success(function (data) {
            $scope.treeData = data;
            $scope.selectedNode = $scope.defaultSelectedNode();

            $scope.loadFile();
        });

        $scope.treeOptions = {
            nodeChildren: "children",
            dirSelectable: true,
            injectClasses: {}
        };

        $scope.aceConfig = {
            useWrapMode: true,
            showGutter: true,
            theme: 'eclipse',
            mode: 'html',
            firstLineNumber: 1,
            onLoad: function (_editor) {
                $scope.templateAceSession = _editor.getSession();
            },
            onChange: function () {
                $scope.template = $scope.templateAceSession.getDocument().getValue();
            },
            require: ['ace/ext/language_tools'],
            advanced: {
                enableSnippets: true,
                enableBasicAutocompletion: true,
                enableLiveAutocompletion: true
            }
        };

        $scope.defaultSelectedNode = function () {
            for (var i = 0; i < $scope.treeData.children.length; i++) {
                var node = $scope.treeData.children[i];
                if (node.path === '/index.html') {
                    return node;
                }
            }

            return null;
        };

        $scope.loadFile = function () {
            $http.get('/admin/template?path=' + $scope.selectedNode.path).success(function (data) {
                $scope.file = data;
                $scope.original = angular.copy($scope.file);
            });
        };

        $scope.isFileChanged = function () {
            return !angular.equals($scope.file, $scope.original);
        };

        $scope.selectNode = function (node) {
            $scope.selectedNode = node;

            if (!node.directory) {
                $scope.loadFile();
            }
        };

        $scope.save = function () {
            $http.put('/admin/template?path=' + $scope.selectedNode.path, $scope.file).success(function (data) {
                $scope.file = data;
                $scope.original = angular.copy($scope.file);

                alert('file updated');
            });
        };

        $scope.cancel = function () {
            if ($scope.isFileChanged()) {
                if (confirm('reset changes?')) {
                    $scope.file = angular.copy($scope.original);
                }
            }
        }
    }]);
})(angular);