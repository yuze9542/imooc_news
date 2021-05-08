package com.imooc.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
@Data
public class Article {
    @Id
    private String id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章所属分类id
     */
    @Column(name = "category_id")
    private Integer categoryId;

    /**
     * 文章类型，1：图文（1张封面），2：纯文字
     */
    @Column(name = "article_type")
    private Integer articleType;

    /**
     * 文章封面图，article_type=1 的时候展示
     */
    @Column(name = "article_cover")
    private String articleCover;

    /**
     * 是否是预约定时发布的文章，1：预约（定时）发布，0：即时发布    在预约时间到点的时候，把1改为0，则发布
     */
    @Column(name = "is_appoint")
    private Integer isAppoint;

    /**
     * 文章状态，1：审核中（用户已提交），2：机审结束，等待人工审核，3：审核通过（已发布），4：审核未通过；5：文章撤回（已发布的情况下才能撤回和删除）
     */
    @Column(name = "article_status")
    private Integer articleStatus;

    /**
     * 发布者用户id
     */
    @Column(name = "publish_user_id")
    private String publishUserId;

    /**
     * 文章发布时间（也是预约发布的时间）
     */
    @Column(name = "publish_time")
    private Date publishTime;

    /**
     * 用户累计点击阅读数（喜欢数）（点赞） - 放redis
     */
    @Column(name = "read_counts")
    private Integer readCounts;

    /**
     * 文章评论总数。评论防刷，距离上次评论需要间隔时间控制几秒
     */
    @Column(name = "comment_counts")
    private Integer commentCounts;

    @Column(name = "mongo_file_id")
    private String mongoFileId;

    /**
     * 逻辑删除状态，非物理删除，1：删除，0：未删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 文章的创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 文章的修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 文章内容，长度不超过9999，需要在前后端判断
     */
    private String content;

}