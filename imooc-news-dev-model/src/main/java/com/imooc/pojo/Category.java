package com.imooc.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
public class Category {

    @Id
    private Integer id;

    private String name;

    @Column(name = "tag_color")
    private String tagColor;

}
