package com.imooc.article.service.Impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.BaseService;
import com.imooc.article.mapper.CommentsCustomMapper;
import com.imooc.article.mapper.CommentsMapper;
import com.imooc.article.service.ArticlePortalService;
import com.imooc.article.service.CommentsPortalService;
import com.imooc.pojo.Comments;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.pojo.vo.CommentsVO;
import com.imooc.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentsPortalServiceImpl extends BaseService implements CommentsPortalService {

    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private CommentsCustomMapper commentsCustomMapper;

    @Autowired
    private ArticlePortalService articlePortalService;

    @Autowired
    private Sid sid;

    @Override
    @Transactional
    public void createComments(String articleId, String fatherCommentId,
                               String content, String userId, String nickname,
                               String face) {

        ArticleDetailVO articleDetailVO = articlePortalService.queryDetail(articleId);
        Comments comments = new Comments();

        String commentId = sid.nextShort();
        comments.setId(commentId);

        comments.setWriterId(articleDetailVO.getPublishUserId());
        comments.setArticleTitle(articleDetailVO.getTitle());
        comments.setArticleCover(articleDetailVO.getCover());
        comments.setArticleId(articleId);
        comments.setFatherId(fatherCommentId);
        comments.setCommentUserFace(face);

        comments.setCommentUserId(userId);
        comments.setCommentUserNickname(nickname);
        comments.setContent(content);
        comments.setCreateTime(new Date());

        commentsMapper.insert(comments);

        // 评论数累加
        redis.increment(REDIS_ARTICLE_COMMENT_COUNTS+":"+articleId,1);

    }

    @Override
    public PagedGridResult queryArticleComments(String articleId, Integer page, Integer pageSize) {

        Map<String,Object> map = new HashMap<>();
        map.put("articleId",articleId);

        PageHelper.startPage(page,pageSize);
        List<CommentsVO> list = commentsCustomMapper.queryArticleCommentList(map);

        return setterPagedGrid(list,page);
    }

    @Override
    public PagedGridResult queryCommentsByWriterId(String writerId, Integer page, Integer pageSize) {

        Comments comment = new Comments();
        comment.setWriterId(writerId);

        PageHelper.startPage(page,pageSize);

        List<Comments> list = commentsMapper.select(comment);

        return setterPagedGrid(list,page);
    }

    @Override
    public void deleteCommentByWriter(String writerId, String commentId) {

        Comments c = new Comments();
        c.setId(commentId);
        Comments result = commentsMapper.selectOne(c);
        String articleId = result.getArticleId();

        Comments comment = new Comments();
        comment.setWriterId(writerId);
        comment.setId(commentId);
        commentsMapper.delete(comment);
        redis.decrement(REDIS_ARTICLE_COMMENT_COUNTS+":"+articleId,1);
    }
}
