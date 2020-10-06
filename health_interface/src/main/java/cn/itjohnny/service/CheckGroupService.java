package cn.itjohnny.service;


import cn.itjohnny.entity.PageResult;
import cn.itjohnny.entity.QueryPageBean;
import cn.itjohnny.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {

    /**
     * 新增检查组
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    boolean add(CheckGroup checkGroup,Integer[] checkitemIds);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 根据id查找检查组
     * @param id
     * @return
     */
    CheckGroup findCheckGroup(Integer id);


    /**
     * 编辑检查组及中间关系表
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    boolean edit(CheckGroup checkGroup, Integer[] checkitemIds);


    /**
     * 根据id删除检查组
     * @param id
     * @return
     */
    boolean deleteById(Integer id);

    /**
     * 查询所有检查组
     * @return
     */
    List<CheckGroup> findAll();
}
