package com.imooc.pojo.vo;

import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;

@Data
public class AppUserVO {
    private String id;
    private String nickname;
    private String face;
    private Integer activeStatus;

    private Integer myFollowCounts;
    private Integer myFansCounts;
}
