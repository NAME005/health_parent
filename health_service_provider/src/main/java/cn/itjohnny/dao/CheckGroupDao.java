package cn.itjohnny.dao;

import cn.itjohnny.pojo.CheckGroup;
import cn.itjohnny.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {

    /**
     * 新增检查组
     * @param checkGroup
     * @return
     */
    boolean add(CheckGroup checkGroup);

    /**
     * 往检查组检查项中间便添加项
     * 与课件不同,课件是先把数据封装在map中
     * @param checkGroupId
     * @param checkitemId
     * @return
     */
    boolean addToCheckGroupAndCheckitem(@Param("checkGroupId") Integer checkGroupId,@Param("checkitemId") Integer checkitemId);


    /**
     * 按条件分页查询
     * @return
     */
    List<CheckGroup> findByQueryString(String queryString);


    /**
     * 根据id查找检查组
     * @param id
     * @return
     */
    CheckGroup findCheckGroupById(Integer id);


    /**
     * 跟新检查组数据
     * @param checkGroup
     * @return
     */
    boolean update(CheckGroup checkGroup);


    /**
     * 在组项中间表中根据组id删除数据
     * @param checkGroupId
     * @return
     */
    boolean deleteCheckGroupAndItem(Integer checkGroupId);

    /**
     * 根据检查组id删除检查组表中的检查组
     * @param id
     * @return
     */
    boolean deleteById(Integer id);
}
