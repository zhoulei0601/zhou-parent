package com.zhou.pojo;

import java.io.Serializable;

public class TbContentCategory implements Serializable {
    private static final long serialVersionUID = -7517517004657867799L;
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}