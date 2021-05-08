package com.imooc.user.service;

import com.imooc.utils.PagedGridResult;

public interface AppUserMngService {

    // 查询所有人列表
    public PagedGridResult queryAllUserList(String nickname,
                                            Integer status,
                                            String startDate,
                                            String endDate,
                                            Integer page,
                                            Integer pageSize);

    // 冻结或不冻结
    public void freezeOrNot(String userId, Integer status);

    }