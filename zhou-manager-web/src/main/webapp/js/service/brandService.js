//创建品牌服务
app.service("brandService",function ($http) {
    this.findAll = function () {
        return $http.get("../brand/findAll.do");
    }

    this.findPage = function (pageNum,pageSize) {
        return $http.get("../brand/findPage.do?pageNum=" + pageNum + "&pageSize=" + pageSize);
    }
    //下拉列表
    this.selectOptionList = function(){
        return $http.get("../brand/selectOptionList.do");
    }
});