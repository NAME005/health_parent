package cn.itjohnny.controller;


import cn.itjohnny.constant.MessageConstant;
import cn.itjohnny.entity.PageResult;
import cn.itjohnny.entity.QueryPageBean;
import cn.itjohnny.entity.Result;
import cn.itjohnny.pojo.Permission;
import cn.itjohnny.service.PermissionService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限项相关
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;


    /**
     * 增加方法
     * @param permission
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission) {
        Result result = null;
        try {
            permissionService.addPermission(permission);
            result = new Result(true,MessageConstant.ADD_PERMISSION_SUCCESS);
        }catch (Exception e){
            result = new Result(false,MessageConstant.ADD_PERMISSION_FAIL);
        }

        return result;
    }

    /**
     * 根据条件分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        PageResult pageResult = permissionService.findPage(queryPageBean);

        return pageResult;
    }

    /**
     * 查询所有权限项
     * @return
     */
    @RequestMapping("/findAll") // 这个方法与课件不同
    public Result findAll(){
        Result result = null;
        try {
            List<Permission> permissionList = permissionService.findAll();
            result = new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permissionList);
        }catch (Exception e){
            result = new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        }

        return result;
    }

    /**
     * 根据id删除
     * @param permission
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Permission permission) {
        Result result = null;
        Integer id = permission.getId();
        try {
            permissionService.deleteById(id);
            result = new Result(true,MessageConstant.DELETE_PERMISSION_SUCCESS);
        }catch (Exception e){
            result = new Result(false,MessageConstant.DELETE_PERMISSION_FAIL);
        }

        return result;
    }

    /**
     * 根据id查找
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        Result result = null;
        try {
            Permission permission = permissionService.findById(id);
            result = new Result(true,null,permission);
        }catch (Exception e){
            result = new Result(false,null);
        }

        return result;
    }

    /**
     * 编辑权限项
     * @param permission
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission permission) {
        Result result = null;
        try {
            permissionService.editPermission(permission);
            result = new Result(true,MessageConstant.EDIT_PERMISSION_SUCCESS);
        }catch (Exception e){
            result = new Result(false,MessageConstant.EDIT_PERMISSION_FAIL);
        }

        return result;
    }


    @RequestMapping("/findPermissionIdByRole")
    public Result findPermissionIdByRole(Integer id){

        Result result = null;
        try {
            List permissionIds = permissionService.findPermissionIdByRole(id);
            result = new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,permissionIds);
        }catch (Exception e){
            result = new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        }

        return result;
    }




}
