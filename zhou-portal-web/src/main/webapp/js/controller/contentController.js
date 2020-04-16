app.controller('contentController',function ($scope,$controller,contentService) {
    $controller('baseController',{$scope:$scope});//继承


    //查询轮播图
    $scope.contentList = [];
    $scope.listContentByCategoryId = function (categoryId) {
        contentService.listContentByCategoryId(categoryId).success(
            function (response) {
                $scope.contentList = response;
            }
        );
    }

    $scope.search = function () {
        window.location.href = `http://localhost:9104/search.html#?keyWords=${$scope.keyWords}`;
    }
});