package cn.itjohnny.service;


import cn.itjohnny.entity.PageResult;
import cn.itjohnny.entity.QueryPageBean;
import cn.itjohnny.pojo.Role;

import java.util.List;

public interface RoleService {

    /**
     * 新增角色
     * @param role
     * @param permissionIds
     * @return
     */
    boolean add(Role role, Integer[] permissionIds);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 根据id查找角色
     * @param id
     * @return
     */
    Role findRole(Integer id);


    /**
     * 编辑角色及中间关系表
     * @param role
     * @param permissionIds
     * @return
     */
    boolean edit(Role role, Integer[] permissionIds);


    /**
     * 根据id删除角色
     * @param id
     * @return
     */
    boolean deleteById(Integer id);

    /**
     * 查询所有角色
     * @return
     */
    List<Role> findAll();
}
