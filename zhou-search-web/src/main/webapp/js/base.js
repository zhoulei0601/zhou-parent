//建立模块
var app = angular.module("zhou",[]);

//定义过滤器
app.filter('trustHtml',['$sce',function ($sce) {
    return function (data) {//被过滤内容
        return $sce.trustAsHtml(data);//过滤后内容，新人html
    }
}]);