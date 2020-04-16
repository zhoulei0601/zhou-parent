package com.zhou.manager.controller;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zhou.pojo.TbItem;
import com.zhou.pojogroup.Goods;
import com.zhou.search.service.ItemSearchItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.zhou.pojo.TbGoods;
import com.zhou.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;

	@Reference
	private ItemSearchItemService searchItemService; //搜索服务
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			goodsService.delete(ids);
			searchItemService.deleteItemCat(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}

	/**
	  * @description 更新状态
	  * @params [goods, page, rows]
	  * @return entity.PageResult
	  * @author zhoulei
	  * @createtime 2020-04-10 09:18
	  */
	@RequestMapping("/updateStatus")
	public Result updateStatus( String ids,String status){
		String[] idArr = ids.split(",");
		int num = goodsService.updateStatus(idArr,status);
		if(num > 0){
			if("1".equals(status)){
				//商品审核通过 更新索引库
				Long[] idArr2 =  Stream.of(idArr).map(s -> Long.valueOf(s)).collect(Collectors.toList()).toArray(new Long[]{});
				List<TbItem> itemList = goodsService.findItemByIdAndStatus(idArr2,status);
				if(!itemList.isEmpty()){
					searchItemService.importItemCat(itemList);
				}
			}
			return  new Result(true, "成功");
		}
		return new Result(false, "失败");
	}
	
}
