//创建品牌控制器
app.controller("brandController",function ($scope,$http,brandService,$controller) {

    $controller("baseController",{$scope : $scope});//继承

    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页查询
    $scope.findPage = function(pageNum,pageSize){
        brandService.findPage(pageNum,pageSize).success(
            function(response){
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;
            }
        );
    }

    //保存
    $scope.save = function(){
        $http.post("../brand/save.do",$scope.entity).success(
            function (response) {
                if(response.success){
                    $scope.reloadList();
                }else{
                    alert(response.message);
                }
            }
        );
    }

    //修改
    $scope.update = function (id) {
        $http.get("../brand/findOne.do?id=" + id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //删除
    $scope.del = function () {
        if(confirm("确定要删除吗？")){
            $http.get("../brand/delete.do?ids=" + $scope.selectIds).success(
                function (response) {
                    if(response.success){
                        $scope.reloadList();
                    }else{
                        alert(response.message);
                    }
                }
            );
        }
    }

    $scope.searchEntity = {};//初始化，防止对象null.post提交400
    //条件查询
    $scope.search = function (pageNum,pageSize) {
        $http.post("../brand/search.do?pageNum=" + pageNum + "&pageSize=" + pageSize,$scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;
            }
        );
    }
});