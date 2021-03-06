package com.zhou.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.zhou.mapper.TbSpecificationOptionMapper;
import com.zhou.pojo.TbSpecificationOption;
import com.zhou.pojo.TbSpecificationOptionExample;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhou.mapper.TbTypeTemplateMapper;
import com.zhou.pojo.TbTypeTemplate;
import com.zhou.pojo.TbTypeTemplateExample;
import com.zhou.pojo.TbTypeTemplateExample.Criteria;
import com.zhou.sellergoods.service.TypeTemplateService;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;

import static com.alibaba.fastjson.JSON.parseArray;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

	@Autowired
	private TbTypeTemplateMapper typeTemplateMapper;
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper; //规格选项mapper
	//redis 服务
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbTypeTemplate> findAll() {
		return typeTemplateMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbTypeTemplate> page=   (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbTypeTemplate typeTemplate) {
		typeTemplateMapper.insert(typeTemplate);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbTypeTemplate typeTemplate){
		typeTemplateMapper.updateByPrimaryKey(typeTemplate);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbTypeTemplate findOne(Long id){
		return typeTemplateMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			typeTemplateMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
	@Override
	public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbTypeTemplateExample example=new TbTypeTemplateExample();
		Criteria criteria = example.createCriteria();
		
		if(typeTemplate!=null){			
						if(typeTemplate.getName()!=null && typeTemplate.getName().length()>0){
				criteria.andNameLike("%"+typeTemplate.getName()+"%");
			}
			if(typeTemplate.getSpecIds()!=null && typeTemplate.getSpecIds().length()>0){
				criteria.andSpecIdsLike("%"+typeTemplate.getSpecIds()+"%");
			}
			if(typeTemplate.getBrandIds()!=null && typeTemplate.getBrandIds().length()>0){
				criteria.andBrandIdsLike("%"+typeTemplate.getBrandIds()+"%");
			}
			if(typeTemplate.getCustomAttributeItems()!=null && typeTemplate.getCustomAttributeItems().length()>0){
				criteria.andCustomAttributeItemsLike("%"+typeTemplate.getCustomAttributeItems()+"%");
			}
	
		}
		
		Page<TbTypeTemplate> page= (Page<TbTypeTemplate>)typeTemplateMapper.selectByExample(example);
		//一次性缓存品牌及规格列表
		cacheBrandAndSpec();
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<Map> findSpecList(Long templateId) {
		TbTypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(templateId);
		List<Map> spcList = JSON.parseArray(typeTemplate.getSpecIds(),Map.class);
		for(Map spcMap : spcList){
			TbSpecificationOptionExample selectByExample = new TbSpecificationOptionExample();
			TbSpecificationOptionExample.Criteria criteria = selectByExample.createCriteria();
			criteria.andSpecIdEqualTo(new Long((Integer)spcMap.get("id")));
			List<TbSpecificationOption> spcOptionList = specificationOptionMapper.selectByExample(selectByExample);
			spcMap.put("options",spcOptionList);
		}
		return spcList;
	}

	/**
	  * @description 缓存品牌及规格列表
	  * @params []
	  * @return void
	  * @author zhoulei
	  * @createtime 2020-04-15 09:06
	  */
	private void cacheBrandAndSpec(){
		List<TbTypeTemplate> typeTemplateList = findAll();
		for(TbTypeTemplate typeTemplate : typeTemplateList){
			List<Map> brandList = JSON.parseArray(typeTemplate.getBrandIds(),Map.class);
			//缓存品牌列表
			redisTemplate.boundHashOps("brandList").put(typeTemplate.getId(),brandList);
			//缓存规格列表
			redisTemplate.boundHashOps("specList").put(typeTemplate.getId(),findSpecList(typeTemplate.getId()));
		}
		System.out.println("缓存： 品牌及规格列表");
	}


}
