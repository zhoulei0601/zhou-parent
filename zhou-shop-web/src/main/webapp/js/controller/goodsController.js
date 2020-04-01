 //控制层 
app.controller('goodsController' ,function($scope,$controller,goodsService,uploadService,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.add=function(){
		$scope.entity.goodsDesc.introduction = editor.html();
		goodsService.add( $scope.entity ).success(
			function(response){
				if(response.success){
					alert("增加成功！");
					$scope.entity = {};
					editor.html('');
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}


	//文件上传
	$scope.uploadFile = function () {
		uploadService.uploadFile().success(
			function (response) {
				if(response.success){
					$scope.image_entity.url=response.message;
				}else{
					alert(response.message);
				}
			}
		).error(function () {
			alert("上传错误");
		});
	}

	$scope.entity = {goods : {} , goodsDesc : {itemImages : [],specificationItems : []}}; //初始
	//增加图片
	$scope.addImage = function () {
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity);
	}

	//删除图片
	$scope.removeImage = function (index) {
		$scope.entity.goodsDesc.itemImages.splice(index,1);
	}

	//查询一级下拉列表
	$scope.itemCatList = function () {
		itemCatService.findItemById(0).success(
			function (response) {
				$scope.itemCat1List = response;
			}
		);
	}

	//级联二级下拉列表
	/**
	 * $watch 监听一个对象，变化时触发事件
	 * $scope.$watch(watchObj,watchCallback,ifDeep)
	 * fDeep值设置为true, 那么angular会检测被监控对象的每个属性是否发生了变化
	 */
	$scope.$watch("entity.goods.category1Id",function (newValue,oldValue) {
		itemCatService.findItemById(newValue).success(
			function (response) {
				$scope.itemCat2List = response;
			}
		);
	});
	//级联三级下拉列表
	$scope.$watch("entity.goods.category2Id",function (newValue,oldValue) {
		itemCatService.findItemById(newValue).success(
			function (response) {
				$scope.itemCat3List = response;
			}
		);
	});

	//级联模板id
	$scope.$watch("entity.goods.category3Id",function (newValue,oldValue) {
		itemCatService.findOne(newValue).success(
			function (response) {
				$scope.entity.goods.typeTemplateId = response.typeId;
			}
		);
	});

	//级联品牌及规格
	$scope.$watch("entity.goods.typeTemplateId",function (newValue,oldValue) {
		typeTemplateService.findOne(newValue).success(
			function (response) {
				$scope.typeTemplate = response;
				$scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);
				//扩展属性
				$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems)
			}
		);
		
		//规格
		typeTemplateService.findSpecList(newValue).success(
			function (response) {
				$scope.specList = response;
			}
		);
	});

	//规格勾选处理
	$scope.updateSpeItem = function ($event,name,value) {
		var obj = $scope.searchObjectBykey($scope.entity.goodsDesc.specificationItems,"attributeName",name);
		if(obj){
			if($event.target.checked){
				obj.attributeValue.push(value);
			}else{
				obj.attributeValue.splice(obj.attributeValue.indexOf(value),1);//移除选项值
				if(obj.attributeValue.length < 1){
					$scope.entity.goodsDesc.specificationItems.splice(
						$scope.entity.goodsDesc.specificationItems.indexOf(obj),1);//移除空值选项
				}
			}
		}else{
			$scope.entity.goodsDesc.specificationItems.push({"attributeName" : name , "attributeValue" : [value]});
		}
	}
});	
