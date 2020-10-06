package cn.itjohnny.service;

import cn.itjohnny.entity.PageResult;
import cn.itjohnny.entity.QueryPageBean;
import cn.itjohnny.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    /**
     * 添加检查项目
     * @param checkItem
     * @return
     */
    boolean addCheckItem(CheckItem checkItem);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    boolean deleteById(Integer id);

    /**
     * 根据id查找检查项
     * @param id
     * @return
     */
    CheckItem findById(Integer id);

    /**
     * 编辑检查项
     * @param checkItem
     * @return
     */
    boolean editCheckItem(CheckItem checkItem);

    /**
     * 查询所有检查项
     * @return
     */
    List<CheckItem> findAll();

    /**
     * 根据检查组id查找检查项id
     * @param id
     * @return
     */
    List findCheckItemIdByCheckGroup(Integer id);
}
