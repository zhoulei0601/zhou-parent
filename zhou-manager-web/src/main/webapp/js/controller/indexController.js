app.controller("indexController",function ($scope,$controller,loginService) {
    $controller("baseController",{$scope : $scope});

    //获取登录名称
    $scope.loginName = function () {
        loginService.loginName().success(
            function (response) {
                 $scope.loginName = response.loginName;
        });
    }
});