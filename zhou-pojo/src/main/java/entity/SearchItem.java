package entity;

import com.zhou.pojo.TbItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @description: TbItem 搜索组合类
 * @author: zhoulei
 * @createTime: 2020-04-14 13:05
 **/
public class SearchItem implements Serializable {
    private static final long serialVersionUID = 8904181655445638464L;
    private String keyWords; //关键字
    private List<TbItem> itemList;//列表
    private List<String> categoryList; //商品分类列表
    private List<Map> brandList ;//品牌列表
    private List<Map> specList;//规格列表
    private String category;//商品分类
    private String brand;//品牌
    private Map spec;//规格
    private String price;//价格
    private String sort; //排序
    private String  pageNo;//页码
    private String pageSize;//每页大小
    private String totalRows;//总记录
    private String totalPages;//总页数
    private String sortField;//排序字段



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

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Map> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Map> brandList) {
        this.brandList = brandList;
    }

    public List<Map> getSpecList() {
        return specList;
    }

    public void setSpecList(List<Map> specList) {
        this.specList = specList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setSpec(Map spec) {
        this.spec = spec;
    }

    public Map getSpec() {
        return spec;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(String totalRows) {
        this.totalRows = totalRows;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }
}
