 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location,goodsService,uploadService,itemCatService,typeTemplateService){
	
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
	$scope.findOne=function(){
		const id = $location.search()['id'];
		if(id){
			goodsService.findOne(id).success(
				function(response){
					$scope.entity= response;
					editor.html($scope.entity.goodsDesc.introduction);//富文本
					$scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);//图片
					$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);//扩展属性
					$scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);//规格属性
					for(let val of $scope.entity.itemList){
						val.spec = JSON.parse(val.spec);
					}
				}
			);
		}
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

	$scope.save=function(){
		//提取文本编辑器的值
		$scope.entity.goodsDesc.introduction=editor.html();
		var serviceObject;//服务层对象
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加
		}
		serviceObject.success(
			function(response){
				if(response.success){
					alert('保存成功');
					window.location.href = "goods.html";
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
				if(!$location.search()['id']){ //新增商品（防止修改时覆盖无值）
					$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems)
				}
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

	//创建SKU列表
	$scope.createItemList = function(){
		$scope.entity.itemList = [{spec : {},price : 0,num : 999, status : '0', isDefault : '0'}];
		var items = $scope.entity.goodsDesc.specificationItems;
		for(let i = 0; i < items.length; i++){
			$scope.entity.itemList = addItemList($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
		}
	}

	//添加列表
	addItemList = function(list,columnNames,columnValues){
		var newList = [];
		for(let i = 0; i < list.length; i++){
			var oldItem = list[i];
			for(let j = 0; j < columnValues.length; j++){
				var newItem = JSON.parse(JSON.stringify(oldItem));
				newItem.spec[columnNames] = columnValues[j];
				newList.push(newItem);
			}
		}
		return newList;
	}

	//状态值
	$scope.status = ['未审核','已审核','审核未通过','关闭'];
	$scope.categoryList = [];
	//分类
	$scope.findItemCatList = function () {
		itemCatService.findAll().success(
			function(response){
				for(let val of response){
					$scope.categoryList[val.id] = val.name;
				}
			}
		);
	}

	//判断规格选项值
	$scope.checkAttributeValue = function(text,optionName){
		const attributeList = $scope.entity.goodsDesc.specificationItems;
		const obj = $scope.searchObjectBykey(attributeList,'attributeName',text)
		if(obj){
			if(obj.attributeValue.indexOf(optionName) >= 0){
				return true;
			}
		}
		return false;
	}
});	
