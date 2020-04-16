app.controller('itemSearchController',function ($scope,$location,itemSearchService) {

    $scope.searchEntity = {keyWords:'',category:'',brand:'',spec:{},
        price:'',sort:'',pageNo:'1',pageSize:'',totalRows:'',sortField:'',sort:''};
    $scope.searchItem = {keyWords : '',itemList : [],categoryList : [],brandList : [],specList : []};
    //搜索
    $scope.search = function () {
        itemSearchService.search($scope.searchEntity).success(
            function(res){
                $scope.searchItem = res;
                buildPageLabel();
            }
        );
    }

    //构建分页栏
    var buildPageLabel = function(){
        $scope.pageLabel = [];
        $scope.pageLabel.dotted = true;
        let firstPage = 1;//起始页码
        let lastPage = $scope.searchItem.totalPages;//截至页码
        if(lastPage > 5){
            if($scope.searchItem.pageNo <= 3){
                lastPage = 5;
            }else if($scope.searchItem.pageNo >= lastPage -2){
                firstPage = lastPage - 4;
            }else{
                firstPage = $scope.searchItem.pageNo - 2;
                lastPage = parseInt($scope.searchItem.pageNo) + 2;
            }
        }
        for(let i = firstPage; i <= lastPage ;i++){
            $scope.pageLabel.push(i);
            if(i == $scope.searchItem.totalPages){
                $scope.pageLabel.dotted = false;
            }
        }
    }

    //分页查询
    $scope.queryByPage = function(pageNo){
        pageNo = parseInt(pageNo);
        if(pageNo < 1 || pageNo >  $scope.searchItem.totalPages ){
            return ;
        }
        $scope.searchEntity.pageNo = pageNo;
        $scope.search();
    }

    //添加搜索条件
    $scope.addSearchItem = function (key,val) {
        if(key == 'category' || key == 'brand' || key == 'price'){ //点击品牌或规格
            $scope.searchEntity[key] = val;
        }else{ // 点击规格
            $scope.searchEntity.spec[key] = val;
        }
        $scope.search();
    }

    //移除搜索条件
    $scope.removeSearchItem = function(key){
        if(key == 'category' || key == 'brand' || key == 'price'){ //点击品牌或规格
            $scope.searchEntity[key] = '';
        }else{ // 点击规格
            delete $scope.searchEntity.spec[key];
        }
        $scope.search();
    }

    //搜索关键字中有品牌 隐藏品牌列表
    $scope.hideBrandByKeywords = function () {
        for(let val of $scope.searchItem.brandList){
            if($scope.searchEntity.keyWords.indexOf(val.text) >= 0){
                return true;
            }
        }
        return false;
    }

    //排序查询
    $scope.sortSearch = function (field,sort) {
        $scope.searchEntity.sortField = field;
        $scope.searchEntity.sort = sort;
        $scope.search();
    }

    //关键字
    $scope.loadKeyword = function () {
        $scope.searchEntity.keyWords = $location.search()['keyWords'];
        $scope.search();
    }
});