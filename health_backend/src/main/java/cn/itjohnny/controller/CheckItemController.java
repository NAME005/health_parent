package cn.itjohnny.controller;


import cn.itjohnny.entity.PageResult;
import cn.itjohnny.entity.QueryPageBean;
import cn.itjohnny.service.CheckItemService;
import cn.itjohnny.constant.MessageConstant;
import cn.itjohnny.entity.Result;
import cn.itjohnny.pojo.CheckItem;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查项相关
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;


    /**
     * 增加方法
     * @param checkItem
     * @return
     */
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem) {
        Result result = null;
        try {
            checkItemService.addCheckItem(checkItem);
            result = new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
        }catch (Exception e){
            result = new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }

        return result;
    }

    /**
     * 根据条件分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        PageResult pageResult = checkItemService.findPage(queryPageBean);

        return pageResult;
    }

    /**
     * 查询所有检查项
     * @return
     */
    @RequestMapping("/findAll") // 这个方法与课件不同
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findAll(){
        Result result = null;
        try {
            List<CheckItem> checkItemList = checkItemService.findAll();
            result = new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
        }catch (Exception e){
            result = new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }

        return result;
    }

    /**
     * 根据id删除
     * @param checkItem
     * @return
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result delete(CheckItem checkItem) {
        Result result = null;
        Integer id = checkItem.getId();
        try {
            checkItemService.deleteById(id);
            result = new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        }catch (Exception e){
            result = new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }

        return result;
    }

    /**
     * 根据id查找
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findById(Integer id) {
        Result result = null;
        try {
            CheckItem checkItem = checkItemService.findById(id);
            result = new Result(true,null,checkItem);
        }catch (Exception e){
            result = new Result(false,null);
        }

        return result;
    }

    /**
     * 编辑检查项
     * @param checkItem
     * @return
     */
    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public Result edit(@RequestBody CheckItem checkItem) {
        Result result = null;
        try {
            checkItemService.editCheckItem(checkItem);
            result = new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        }catch (Exception e){
            result = new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }

        return result;
    }


    @RequestMapping("/findCheckItemIdByCheckGroup")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findCheckItemIdByCheckGroup(Integer id){

        Result result = null;
        try {
            List checkItemIds = checkItemService.findCheckItemIdByCheckGroup(id);
            result = new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkItemIds);
        }catch (Exception e){
            result = new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }

        return result;
    }




}
