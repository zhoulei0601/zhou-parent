app.controller('itemSearchController',function ($scope,itemSearchService) {

    $scope.searchEntity = {keywords:'',category:'',brand:'',spec:{}};
    $scope.searchItem = {keyWords : '',itemList : [],categoryList : [],brandList : [],specList : []};
    //搜索
    $scope.search = function () {
        itemSearchService.search($scope.searchEntity).success(
            function(res){
                $scope.searchItem = res;
            }
        );
    }

    //添加搜索条件
    $scope.addSearchItem = function (key,val) {
        if(key == 'category' || key == 'brand'){ //点击品牌或规格
            $scope.searchEntity[key] = val;
        }else{ // 点击规格
            $scope.searchEntity.spec[key] = val;
        }
        $scope.search();
    }

    //移除搜索条件
    $scope.removeSearchItem = function(key){
        if(key == 'category' || key == 'brand'){ //点击品牌或规格
            $scope.searchEntity[key] = '';
        }else{ // 点击规格
            delete $scope.searchEntity.spec[key];
        }
        $scope.search();
    }
});