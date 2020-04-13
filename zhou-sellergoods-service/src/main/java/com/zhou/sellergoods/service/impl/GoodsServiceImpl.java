package com.zhou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhou.mapper.*;
import com.zhou.pojo.*;
import com.zhou.pojo.TbGoodsExample.Criteria;
import com.zhou.pojogroup.Goods;
import com.zhou.sellergoods.service.GoodsService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;

	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	@Autowired
	private TbBrandMapper tbBrandMapper;//品牌
	@Autowired
	private TbItemMapper tbItemMapper;//商品SKU
	@Autowired
	private TbItemCatMapper tbItemCatMapper;//商品分类
	@Autowired
	private TbSellerMapper tbSellerMapper;//商家
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		TbGoods tbGoods = goods.getGoods();
		tbGoods.setAuditStatus("0");//未审核
		goodsMapper.insert(tbGoods);
		TbGoodsDesc tbGoodsDesc = goods.getGoodsDesc();
		tbGoodsDesc.setGoodsId(tbGoods.getId());
		goodsDescMapper.insert(tbGoodsDesc);
		addItem(goods,tbGoods,tbGoodsDesc);

	}

	/**
	  * @description TbItem 赋值
	  * @params [item, tbGoods, tbGoodsDesc]
	  * @return void
	  * @author zhoulei
	  * @createtime 2020-04-09 10:17
	  */
	private void setItemValues(TbItem item,TbGoods tbGoods,TbGoodsDesc tbGoodsDesc){
		item.setCategoryid(tbGoods.getCategory3Id());//商品类目id
		item.setGoodsId(tbGoods.getId());//商品id
		item.setSellerId(tbGoods.getSellerId());//商家id
		item.setCreateTime(new Date());
		item.setUpdateTime(new Date());
		//分类名称
		TbItemCat itemCat = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
		item.setCategory(itemCat.getName());
		//商家名称
		TbSeller seller = tbSellerMapper.selectByPrimaryKey(tbGoods.getSellerId());//商家
		item.setSeller(seller.getName());
		//品牌
		TbBrand brand = tbBrandMapper.selectByPrimaryKey(tbGoods.getBrandId());//品牌
		item.setBrand(brand.getName());
		//图片
		List<Map> imageList = JSON.parseArray(tbGoodsDesc.getItemImages(),Map.class);
		if(imageList != null && !imageList.isEmpty()){
			item.setImage(imageList.get(0).get("url").toString());
		}
	}

	/**
	  * @description 增加 item
	  * @params [goods, tbGoods, tbGoodsDesc]
	  * @return void
	  * @author zhoulei
	  * @createtime 2020-04-09 15:37
	  */
	private void addItem(Goods goods,TbGoods tbGoods,TbGoodsDesc tbGoodsDesc){
		List<TbItem> itemList = goods.getItemList();
		//是否启用规格
		if("1".equals(tbGoods.getIsEnableSpec())){
			for(TbItem item : itemList){
				//SPU + 规格选项值
				String title = tbGoods.getGoodsName();
				Map<String,Object> specMap = JSON.parseObject(item.getSpec());
				for(Object obj : specMap.values()){
					title += " " +  obj.toString();
				}
				item.setTitle(title);
				setItemValues(item,tbGoods,tbGoodsDesc);
				tbItemMapper.insert(item);
			}
		}else{
			TbItem item = new TbItem();
			item.setPrice(tbGoods.getPrice());
			item.setNum(999);
			item.setIsDefault("1");
			item.setTitle(tbGoods.getGoodsName());
			item.setStatus("1");
			item.setSpec("{}");
			setItemValues(item,tbGoods,tbGoodsDesc);
			tbItemMapper.insert(item);
		}
	}
	
	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods){
		TbGoods tbGoods = goods.getGoods();
		TbGoodsDesc tbGoodsDesc = goods.getGoodsDesc();
		//商品基本表
		goodsMapper.updateByPrimaryKey(tbGoods);
		//商品扩展表
		goodsDescMapper.updateByPrimaryKey(tbGoodsDesc);
		//商品SKU
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goods.getGoods().getId());
		tbItemMapper.deleteByExample(example);
		addItem(goods,tbGoods,tbGoodsDesc);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){
		Goods goods = new Goods();
		//商品基本表
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		goods.setGoods(tbGoods);
		//商品扩展表
		TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		goods.setGoodsDesc(goodsDesc);
		//商品SKU
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(tbGoods.getId());
		List<TbItem> itemList = tbItemMapper.selectByExample(example);
		goods.setItemList(itemList);
		return goods;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			goodsMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		
		if(goods!=null){			
			if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
//				criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public int updateStatus(String[] ids, String status) {
		int num = 0;
		for(String id : ids){
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(Long.parseLong(id));
			tbGoods.setAuditStatus(status);
			num += goodsMapper.updateByPrimaryKey(tbGoods);
		}
		return num;
	}

}
