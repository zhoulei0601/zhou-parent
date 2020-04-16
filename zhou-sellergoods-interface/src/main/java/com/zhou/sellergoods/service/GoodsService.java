package com.zhou.sellergoods.service;
import java.util.List;
import com.zhou.pojo.TbGoods;

import com.zhou.pojo.TbItem;
import com.zhou.pojo.TbItemCat;
import com.zhou.pojogroup.Goods;
import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(Goods goods);
	
	
	/**
	 * 修改
	 */
	public void update(Goods goods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public Goods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize);

	/**
	  * @description 状态更新
	  * @params [ids, status]
	  * @return int
	  * @author zhoulei
	  * @createtime 2020-04-10 09:12
	  */
	int updateStatus(String[] ids,String status);

	/**
	  * @description 查询商品SKU列表
	  * @params [id, status]
	  * @return java.util.List<com.zhou.pojo.TbItem>
	  * @author zhoulei
	  * @createtime 2020-04-15 18:08
	  */
	List<TbItem> findItemByIdAndStatus(Long[] ids,String status);
	
}
