package com.imooc.article.mapper;

import com.imooc.pojo.vo.CommentsVO;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Repository
public interface CommentsCustomMapper {
    // 查询文章评论
    public List<CommentsVO> queryArticleCommentList(@Param("paramMap") Map<String,Object> map);
}
