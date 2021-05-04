package com.imooc.api.controller.admin;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "管理员admin维护的api")
@RequestMapping("adminMng")
public interface AdminMngControllerApi {
    @ApiOperation(value = "admin管理员的登录",httpMethod = "POST")
    @PostMapping("/adminLogin")
    public GraceJSONResult adminLogin(@RequestBody AdminLoginBO bo,
                                      HttpServletRequest request,
                                      HttpServletResponse response);

    @ApiOperation(value = "查询admin用户名是否存在",httpMethod = "POST")
    @PostMapping("/adminIsExist")
    public GraceJSONResult adminIsExist(@RequestParam String username);

    @ApiOperation(value = "创建admin",httpMethod = "POST")
    @PostMapping("/addNewAdmin")
    public GraceJSONResult addNewAdmin(@RequestBody NewAdminBO bo,
                                       HttpServletRequest request,
                                       HttpServletResponse response);


    @ApiOperation(value = "查询admin列表",httpMethod = "POST")
    @PostMapping("/getAdminList")
    public GraceJSONResult getAdminList(
            @ApiParam(name = "page", value = "查询下一页的第几页",required = false)
                    @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页查询每一页显示的条数",required = false)
                    @RequestParam Integer pageSize );


    @ApiOperation(value = "退出登录",httpMethod = "POST")
    @PostMapping("/adminLogout")
    public GraceJSONResult adminLogout(@RequestParam String adminId,
                                       HttpServletRequest request,
                                       HttpServletResponse response);
}