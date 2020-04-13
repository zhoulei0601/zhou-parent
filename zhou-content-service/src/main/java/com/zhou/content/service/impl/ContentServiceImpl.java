package com.zhou.content.service.impl;
import java.util.List;

import com.alibaba.dubbo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhou.mapper.TbContentMapper;
import com.zhou.pojo.TbContent;
import com.zhou.pojo.TbContentExample;
import com.zhou.pojo.TbContentExample.Criteria;
import com.zhou.content.service.ContentService;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbContent> page=   (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {
		contentMapper.insert(content);
		redisTemplate.boundHashOps("content").delete(content.getCategoryId());//清除缓存
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContent content){
		contentMapper.updateByPrimaryKey(content);
		redisTemplate.boundHashOps("content").delete(content.getCategoryId());//清除缓存
		TbContent content1 = contentMapper.selectByPrimaryKey(content.getId());
		if(content.getCategoryId().equals(content1.getCategoryId())){
			redisTemplate.boundHashOps("content").delete(content1.getCategoryId());//修改categoryid情况，清除以前缓存
		}
	}
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id){
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			contentMapper.deleteByPrimaryKey(id);
			TbContent content = contentMapper.selectByPrimaryKey(id);
			if(content != null){
				redisTemplate.boundHashOps("content").delete(content.getCategoryId());//清除缓存
			}
		}
	}
	
	
		@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		
		if(content!=null){			
						if(content.getTitle()!=null && content.getTitle().length()>0){
				criteria.andTitleLike("%"+content.getTitle()+"%");
			}
			if(content.getUrl()!=null && content.getUrl().length()>0){
				criteria.andUrlLike("%"+content.getUrl()+"%");
			}
			if(content.getPic()!=null && content.getPic().length()>0){
				criteria.andPicLike("%"+content.getPic()+"%");
			}
			if(content.getStatus()!=null && content.getStatus().length()>0){
				criteria.andStatusLike("%"+content.getStatus()+"%");
			}
	
		}
		
		Page<TbContent> page= (Page<TbContent>)contentMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	  * @description 根据categoryId 查询内容
	  * @params [categoryId]
	  * @return java.util.List<com.zhou.pojo.TbContent>
	  * @author zhoulei
	  * @createtime 2020-04-13 14:15
	  */
	@Override
	public List<TbContent> listContentByCategoryId(Long categoryId) {
		List<TbContent> cachedContent = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);//查询缓存
		if(cachedContent != null && !cachedContent.isEmpty()){
			System.out.println("查询缓存");
			return cachedContent;
		}else{
			TbContentExample example = new TbContentExample();
			Criteria criteria = example.createCriteria();
			criteria.andCategoryIdEqualTo(categoryId);
			criteria.andStatusEqualTo("1");//状态 启用
			example.setOrderByClause(" sort_order");
			List<TbContent> contentList = contentMapper.selectByExample(example);
			redisTemplate.boundHashOps("content").put(categoryId,contentList);//放入缓存
			System.out.println("查询数据库");
			return contentList;
		}
	}

}
