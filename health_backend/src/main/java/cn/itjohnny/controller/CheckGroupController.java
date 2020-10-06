package cn.itjohnny.controller;


import cn.itjohnny.constant.MessageConstant;
import cn.itjohnny.entity.PageResult;
import cn.itjohnny.entity.QueryPageBean;
import cn.itjohnny.entity.Result;
import cn.itjohnny.pojo.CheckGroup;
import cn.itjohnny.pojo.CheckItem;
import cn.itjohnny.service.CheckGroupService;
import cn.itjohnny.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查项相关
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;


    /**
     * 新增检查组
     * @param checkGroup
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        Result result = null;
        try {
            checkGroupService.add(checkGroup,checkitemIds);
            result = new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            result = new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
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

        PageResult pageResult = checkGroupService.findPage(queryPageBean);

        return pageResult;
    }


    /**
     * 根据id查找检查组
     * @param id
     * @return
     */
    @RequestMapping("/findCheckGroup")
    public Result findCheckGroup(Integer id){

        Result result = null;
        try {
            CheckGroup checkGroup = checkGroupService.findCheckGroup(id);
            if (checkGroup == null){
                result = new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
            }
            result = new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }catch (Exception e){
            result = new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }

        return result;
    }


    /**
     * 编辑检查组
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        Result result = null;
        try {
            checkGroupService.edit(checkGroup,checkitemIds);
            result = new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            result = new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
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
            checkGroupService.deleteById(id);
            result = new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            result = new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return result;
    }


    @RequestMapping("/findAll")
    public Result findAll(){
        Result result = null;
        try {
            List<CheckGroup> checkGroups = checkGroupService.findAll();
            result = new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroups);
        }catch (Exception e){
            result = new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }

        return result;
    }









}
