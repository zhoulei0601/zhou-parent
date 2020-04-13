app.service("contentService",function ($http) {
    this.listContentByCategoryId = function (categoryId) {
        return $http.get(`./content/listContentByCategoryId.do?categoryId=${categoryId}`);
    }
});