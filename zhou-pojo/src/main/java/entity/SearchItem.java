package entity;

import com.zhou.pojo.TbItem;

import java.io.Serializable;
import java.util.List;

/**
 * @description: TbItem 搜索组合类
 * @author: zhoulei
 * @createTime: 2020-04-14 13:05
 **/
public class SearchItem implements Serializable {
    private static final long serialVersionUID = 8904181655445638464L;
    private String keyWords;
    private List<TbItem> itemList;

    public List<TbItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<TbItem> itemList) {
        this.itemList = itemList;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
}
