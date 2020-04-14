app.controller('itemSearchController',function ($scope,$controller,itemSearchService) {
    $controller('baseController',{$scope:$scope});//继承

    $scope.searchItem = {};
    //搜索
    $scope.search = function () {
        itemSearchService.search($scope.searchItem).success(
            function(res){
                $scope.searchItem = res.itemList;
            }
        );
    }

});