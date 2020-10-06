package cn.itjohnny.dao;

import cn.itjohnny.pojo.Permission;
import cn.itjohnny.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {

    Set<Permission> findByRoleId(Integer roleId);

    /**
     * 添加权限项
     * @param permission
     * @return
     */
    boolean add(Permission permission);

    /**
     * 按条件分页查询
     * @return
     */
    List<Permission> findByQueryString(String queryString);

    /**
     * 根据id删除,不直接删除,先查找角色是否有关联(引用)
     * @param id
     * @return
     */
    boolean deleteById(Integer id);

    /**
     * 根据id查找权限项
     * @param id
     * @return
     */
    Permission findById(Integer id);

    /**
     * 根据id查找该权限项被几个角色关联
     * @param id
     * @return
     */
    long findRelatedCountOfRole(Integer id);

    /**
     * 编辑权限项
     * @param permission
     * @return
     */
    boolean editPermission(Permission permission);


    /**
     * 根据角色id在中间表查找权限项id
     * @param id
     * @return
     */
    List findPermissionIdByRoleId(Integer id);

}
