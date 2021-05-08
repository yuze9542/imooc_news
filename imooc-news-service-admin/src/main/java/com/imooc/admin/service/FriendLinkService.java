package com.imooc.admin.service;

import com.imooc.pojo.mo.FriendLinkMO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface FriendLinkService {

    // 新增或更新友情链接
    public void saveOrUpdateFriendLink(FriendLinkMO mo);

    //查询友情链接
    public List<FriendLinkMO> queryFriendLinkList();

    // 删除友情链接
    void delete(String linkId);

    // 首页查询友情链接
    public  List<FriendLinkMO> queryPortalALlFriendList();
}
