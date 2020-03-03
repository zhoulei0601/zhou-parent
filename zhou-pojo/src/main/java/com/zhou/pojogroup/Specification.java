package com.zhou.pojogroup;

import com.zhou.pojo.TbSpecification;
import com.zhou.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 规格组合类
 * @author: zhoulei
 * @createTime: 2020-02-13 09:26
 **/
public class Specification implements Serializable {
    private static final long serialVersionUID = -8620646310327801469L;

    private TbSpecification specification;
    private List<TbSpecificationOption> specificationOptions;

    public Specification(TbSpecification specification, List<TbSpecificationOption> specificationOptions) {
        this.specification = specification;
        this.specificationOptions = specificationOptions;
    }

    public Specification(){}

    public TbSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TbSpecification specification) {
        this.specification = specification;
    }

    public List<TbSpecificationOption> getSpecificationOptions() {
        return specificationOptions;
    }

    public void setSpecificationOptions(List<TbSpecificationOption> specificationOptions) {
        this.specificationOptions = specificationOptions;
    }
}
