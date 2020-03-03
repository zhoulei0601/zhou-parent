package com.zhou.sellergoods.service;
import java.util.List;
import java.util.Map;

import com.zhou.pojo.TbSpecification;

import com.zhou.pojogroup.Specification;
import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SpecificationService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbSpecification> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(Specification specification);
	
	
	/**
	 * 修改
	 */
	public void update(Specification specification);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public Specification findOne(Long id);
	
	
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
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize);

	/**
	 * @description 规格下拉数据
	 * @params []
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
	 * @author zhoulei
	 * @createtime 2020-02-13 22:49
	 */
	List<Map<String,String>> findSpecificationList();
}
