package com.imooc.pojo.mo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document("FriendLink") //别名?? 好像就是加到mongoDB 里用的这个名字
public class FriendLinkMO {

    @Id
    private String id;

    @Field("link_name")
    private String linkName;

    @Field("link_url")
    private String linkUrl;

    @Field("is_delete")
    private Integer isDelete;

    @Field("create_time")
    private Date createTime;

    @Field("update_time")
    private Date updateTime;

}