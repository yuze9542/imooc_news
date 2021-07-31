package com.imooc.admin.controller;

import com.imooc.admin.service.AdminUserService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.AdminMngControllerApi;
import com.imooc.enums.FaceVerifyType;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.FaceCompareUtils;
import com.imooc.utils.FaceVerifyUtils;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
@RestController
public class AdminMngController extends BaseController implements AdminMngControllerApi {

    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FaceVerifyUtils faceVerifyUtils;
    @Autowired
    private FaceCompareUtils faceCompareUtils;

    @Override
    public GraceJSONResult adminLogin(AdminLoginBO bo,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

        // 0 验证bo里的username和password不为空
        // 1 查询admin用户
        AdminUser adminUser = adminUserService.queryAdminByUsername(bo.getUsername());
        // 2 判断admin是否为空
        if (adminUser == null){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NAME_NULL_ERROR);
        }
        // 3 判断密码是否匹配
        boolean isPwdMatch = BCrypt.checkpw(bo.getPassword(), adminUser.getPassword());
        if (isPwdMatch){
            doLoginSettings(adminUser,request,response);
            return GraceJSONResult.ok();
        }else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NAME_NULL_ERROR);
        }
    }

    @Override
    public GraceJSONResult adminIsExist(String username) {
        checkAdminExist(username);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult addNewAdmin(NewAdminBO bo, HttpServletRequest request, HttpServletResponse response) {

        // 0 TODO 验证BO中用户名和密码不为空

        // 1 base64不为空 标识人脸入库 否则输入密码
        if (StringUtils.isBlank(bo.getImg64())){
            if (StringUtils.isBlank(bo.getPassword()) ||
               StringUtils.isBlank(bo.getConfirmPassword())){
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }

        // 2 判断admin 密码和验证密码是否一致
        if (StringUtils.isNotBlank(bo.getPassword())){
            if (!bo.getPassword().equals(bo.getConfirmPassword())){
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }

        // 3 校验用户名唯一
        checkAdminExist(bo.getUsername());

        // 4 插入
        adminUserService.createAdminUser(bo);
        return GraceJSONResult.ok();

    }

    @Override
    public GraceJSONResult getAdminList(Integer page, Integer pageSize) {

        if (page == null)
            page = 1;
        if (pageSize == null)
            pageSize = 1;
        PagedGridResult gridResult = adminUserService.queryAdminList(page, pageSize);

        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult adminLogout(String adminId, HttpServletRequest request, HttpServletResponse response) {
        redis.del(REDIS_USER_TOKEN+":"+adminId);
        delCookie(request,response,"aid");
        delCookie(request,response,"atoken");
        delCookie(request,response,"aname");
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult adminFaceLogin(AdminLoginBO bo, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 1 验证 用户名和人脸信息不能为空
        if (StringUtils.isBlank(bo.getUsername()) || StringUtils.isBlank(bo.getImg64())){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
        }

        // 2 从数据库中查询faceId
        AdminUser adminUser = adminUserService.queryAdminByUsername(bo.getUsername());
        String faceId = adminUser.getFaceId();
        if (StringUtils.isBlank(faceId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_NULL_ERROR);
        }
        // 3 请求文件服务 获得人脸数据的base64
        String fileServerUrlExecute = "http://files.imoocnews.com:8004/fs/readFace64InGridFS?faceId="+faceId;
        ResponseEntity<GraceJSONResult> forEntity = restTemplate.getForEntity(fileServerUrlExecute, GraceJSONResult.class);
        GraceJSONResult bodyResult = forEntity.getBody();
        String base64DB = (String) bodyResult.getData(); //     人脸base64数据

        // 4 调用阿里ai进行人脸识别对比 判断可信度
        int compare = faceCompareUtils.compare(bo.getImg64(), base64DB);
        if (compare < 60){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_NULL_ERROR);
        }

//         5 admin登陆后的cookie和redis设置
        doLoginSettings(adminUser,request,response);

        return GraceJSONResult.ok();
    }



    private void checkAdminExist(String username){
        AdminUser adminUser = adminUserService.queryAdminByUsername(username);
        if (adminUser!=null){
            GraceException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
    }

    /**
     * 用于admin用户登录过后的一些设置
     * @param user
     * @param request
     * @param response
     */
    private void doLoginSettings(AdminUser user,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
        // 保存token到redis
        String token = UUID.randomUUID().toString();
        redis.set(REDIS_ADMIN_INFO+":"+user.getId(),token);

        // 保存admin登录基本信息到cookie中
        setCookie(request,response,"atoken",token,COOKIE_MONTH);
        setCookie(request,response,"aid",user.getId(),COOKIE_MONTH);
        setCookie(request,response,"aname",user.getAdminName(),COOKIE_MONTH);
    }
}
