package cn.itjohnny.dao;

import cn.itjohnny.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckItemDao {

    /**
     * 添加检查项
     * @param checkItem
     * @return
     */
    boolean add(CheckItem checkItem);

    /**
     * 按条件分页查询
     * @return
     */
    List<CheckItem> findByQueryString(String queryString);

    /**
     * 根据id删除,不直接删除,先查找检查组是否有关联(引用)
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
     * 根据id查找该检查项被几个检查组关联
     * @param id
     * @return
     */
    long findRelatedCountOfCheckGroup(Integer id);

    /**
     * 编辑检查项
     * @param checkItem
     * @return
     */
    boolean editCheckItem(CheckItem checkItem);


    /**
     * 根据检查组id在中间表查找检查项id
     * @param id
     * @return
     */
    List findCheckItemIdByCheckGroupId(Integer id);





}
