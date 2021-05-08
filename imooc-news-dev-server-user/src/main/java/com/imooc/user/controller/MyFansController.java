package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.MyFansControllerApi;
import com.imooc.enums.Sex;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.vo.FansCountsVO;
import com.imooc.pojo.vo.RegionRatioVO;
import com.imooc.user.service.MyFansService;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MyFansController extends BaseController implements MyFansControllerApi {

    @Autowired
    private MyFansService myFansService;

    @Override
    public GraceJSONResult isMeFollowThisWriter(String writerId, String fanId) {

        boolean meFollowThisWriter = myFansService.isMeFollowThisWriter(writerId, fanId);

        return GraceJSONResult.ok(meFollowThisWriter);
    }

    @Override
    public GraceJSONResult follow(String writerId, String fanId) {
        myFansService.follow(writerId,fanId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult unfollow(String writerId, String fanId) {
        myFansService.unfollow(writerId,fanId);
        return null;
    }

    @Override
    public GraceJSONResult queryAll(String writerId, Integer page, Integer pageSize) {

        if (page==null){
            page = 1;
        }
        if (pageSize==null){
            pageSize = 20;
        }

        PagedGridResult result =myFansService.queryMyFans(writerId,page,pageSize);

        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult queryRatio(String writerId) {
        int manCounts = myFansService.querySex(writerId, Sex.man);
        int womanCounts = myFansService.querySex(writerId, Sex.woman);
        FansCountsVO fansCountsVO = new FansCountsVO();
        fansCountsVO.setManCounts(manCounts);
        fansCountsVO.setWomanCounts(womanCounts);

        return GraceJSONResult.ok(fansCountsVO);
    }

    @Override
    public GraceJSONResult queryRatioByRegion(String writerId) {

        List<RegionRatioVO> fansByRegion = myFansService.queryRatioByRegion(writerId);

        return GraceJSONResult.ok(fansByRegion);
    }
}
