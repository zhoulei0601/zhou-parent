app.service("loginService",function ($http) {

    //获取登录名称
    this.loginName = function () {
        return $http.get("../login/getName.do");
    }
})