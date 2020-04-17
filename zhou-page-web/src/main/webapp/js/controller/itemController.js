app.controller('itemController',function ($scope) {

	$scope.specificationItems = {};//用规格选项
	//购物车加减
    $scope.addNum = function(v){
 		$scope.num += v;
 		if($scope.num < 1){
 			$scope.num = 1;
 		}
    }

    //用户选择规格
    $scope.selectSpec = function(key,val){
		$scope.specificationItems[key] = val;
		searchSku();
    }

    //判断用户是否选中
    $scope.isSelected = function(key,val){
    	if($scope.specificationItems[key] == val){
    		return true;
    	}
    	return false;
    }

    $scope.sku = {};//当前选择的sku
    $scope.loadSku = function(){
    	$scope.sku = skuList[0];
    	$scope.specificationItems = JSON.parse(JSON.stringify(skuList[0].spec));
    }



    //匹配对象是否相等
    var matchObject = function(map1,map2){
    	for(let key in map1){
    		if(map2[key] != map1[key]){
    			return false;
    		}
    	}
    	for(let key in map2){
    		if(map1[key] != map2[key]){
    			return false;
    		}
    	}
    	return true;
    }

    //根据规格查询sku
    function searchSku(){
    	for(let sku of skuList){
    		if(matchObject(sku.spec,$scope.specificationItems)){
    			$scope.sku = sku;
    			return;
    		}
    	}
    	$scope.sku = {id:'0',title:'----',price:0,spec:{}};
    }
});