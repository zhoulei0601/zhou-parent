package entity;

import java.io.Serializable;

/**
 * @description: 返回信息模板
 * @author: zhoulei
 * @createTime: 2020-02-11 17:06
 **/
public class Result implements Serializable {
    private static final long serialVersionUID = 5283793014445351257L;
    private boolean success;
    private String message;


    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
