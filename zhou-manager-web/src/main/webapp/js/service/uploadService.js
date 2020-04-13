app.service("uploadService",function ($http) {
    //文件上传
    this.uploadFile = function () {
        var formData = new FormData();//html5新增 文件form表单
        formData.append("file",file.files[0]);//input name file
        return $http({
            url : '../file/uploadFile.do',
            method : 'POST',
            data : formData,
            headers : {"Content-Type" : undefined},//不写默认json,Content-Type 设置为 multipart/form-data
            transformRequest : angular.identity //将序列化我们的formdata object
        });
    }
});

