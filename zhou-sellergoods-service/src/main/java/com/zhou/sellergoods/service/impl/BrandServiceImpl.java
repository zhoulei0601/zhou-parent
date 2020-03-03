package com.zhou.sellergoods.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhou.mapper.TbBrandMapper;
import com.zhou.pojo.TbBrand;
import com.zhou.pojo.TbBrandExample;
import com.zhou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @description: 品牌serviceimpl
 * @author: zhoulei
 * @createTime: 2020-02-11 10:06
 **/
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper brandMapper;

    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void save(TbBrand brand) {
        if(brand != null && brand.getId() != null){
            brandMapper.updateByPrimaryKey(brand);
        }else{
            brandMapper.insert(brand);
        }
    }

    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long[] ids) {
        int sum = 0;
        for(Long id : ids){
            int num = brandMapper.deleteByPrimaryKey(id);
            sum += num;
        }
        return sum;
    }

    @Override
    public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        if(brand != null){
            if(StringUtils.isNotEmpty(brand.getName())){
                criteria.andNameLike("%" + brand.getName() + "%");
            }
            if(StringUtils.isNotEmpty(brand.getFirstChar())){
                criteria.andFirstCharLike("%" + brand.getFirstChar() + "%");
            }
        }
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Map<String, String>> selectOptionList() {
        return brandMapper.selectOptionList();
    }

}
