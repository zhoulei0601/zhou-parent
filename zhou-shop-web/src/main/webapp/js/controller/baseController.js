app.controller("baseController",function($scope){
       //分页
        $scope.paginationConf = {
            currentPage: 1,//当前页
            totalItems: 10,//总记录数
            itemsPerPage: 10,//每页记录数
            perPageOptions: [10, 20, 30, 40, 50],//分页选项
            onChange: function () {
                $scope.reloadList();
            }
        }

        //刷新列表
        $scope.reloadList = function(){
            $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
        };

         $scope.selectIds = [];//选择ids

        //记录选择ids
        $scope.updateSelection = function ($event,id) {
            if($event.target.checked){
                $scope.selectIds.push(id);
            }else{
                var index = $scope.selectIds.indexOf(id);
                $scope.selectIds.splice(index,1);//移除位置，移除数量
            }
        }
});