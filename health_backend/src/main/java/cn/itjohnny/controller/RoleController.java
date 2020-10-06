package cn.itjohnny.controller;


import cn.itjohnny.constant.MessageConstant;
import cn.itjohnny.entity.PageResult;
import cn.itjohnny.entity.QueryPageBean;
import cn.itjohnny.entity.Result;
import cn.itjohnny.pojo.Role;
import cn.itjohnny.service.RoleService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查项相关
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;


    /**
     * 新增检查组
     * @param role
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Role role, Integer[] permissionIds) {
        Result result = null;
        try {
            roleService.add(role,permissionIds);
            result = new Result(true,MessageConstant.ADD_ROLE_SUCCESS);
        }catch (Exception e){
            result = new Result(false,MessageConstant.ADD_ROLE_FAIL);
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

        PageResult pageResult = roleService.findPage(queryPageBean);

        return pageResult;
    }


    /**
     * 根据id查找检查组
     * @param id
     * @return
     */
    @RequestMapping("/findRole")
    public Result findRole(Integer id){

        Result result = null;
        try {
            Role role = roleService.findRole(id);
            if (role == null){
                result = new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
            }
            result = new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,role);
        }catch (Exception e){
            result = new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        }

        return result;
    }


    /**
     * 编辑检查组
     * @param role
     * @param permissionIds
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Role role, Integer[] permissionIds) {
        Result result = null;
        try {
            roleService.edit(role,permissionIds);
            result = new Result(true,MessageConstant.EDIT_ROLE_SUCCESS);
        }catch (Exception e){
            result = new Result(false,MessageConstant.EDIT_ROLE_FAIL);
        }
        return result;
    }


    /**
     * 删除方法,课件还没写这个方法,这里写的是允许删除的逻辑,先删关系表再删检查组表
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        Result result = null;
        try {
            roleService.deleteById(id);
            result = new Result(true,MessageConstant.DELETE_ROLE_SUCCESS);
        }catch (Exception e){
            result = new Result(false,MessageConstant.DELETE_ROLE_FAIL);
        }
        return result;
    }


    @RequestMapping("/findAll")
    public Result findAll(){
        Result result = null;
        try {
            List<Role> roles = roleService.findAll();
            result = new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,roles);
        }catch (Exception e){
            result = new Result(false,MessageConstant.QUERY_ROLE_FAIL);
        }

        return result;
    }









}
