package entity;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 分页模板
 * @author: zhoulei
 * @createTime: 2020-02-11 15:11
 **/
public class PageResult implements Serializable {
    private static final long serialVersionUID = 2740111006651432734L;
    private Long total;
    private List rows;

    public PageResult(Long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
