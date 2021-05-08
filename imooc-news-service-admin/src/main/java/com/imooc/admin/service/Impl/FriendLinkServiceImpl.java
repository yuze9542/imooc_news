package com.imooc.admin.service.Impl;

import com.imooc.admin.repository.FriendLinkRepository;
import com.imooc.admin.service.FriendLinkService;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.mo.FriendLinkMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Service
public class FriendLinkServiceImpl implements FriendLinkService {

    // 相当于mapper
    @Autowired
    private FriendLinkRepository friendLinkRepository;

    @Override
    public void saveOrUpdateFriendLink(FriendLinkMO mo) {
        friendLinkRepository.save(mo);  // 这就保存了 ?? mongoDb的保存方式真牛逼
    }

    @Override
    public List<FriendLinkMO> queryFriendLinkList() {
//        Pageable pageable = PageRequest.of(1,2); //查询分页 第几页 显示几个
        return friendLinkRepository.findAll();
    }

    @Override
    public void delete(String linkId) {
        friendLinkRepository.deleteById(linkId);
    }

    @Override
    public List<FriendLinkMO> queryPortalALlFriendList() {
        return friendLinkRepository.getAllByIsDelete(YesOrNo.NO.type);
    }
}
