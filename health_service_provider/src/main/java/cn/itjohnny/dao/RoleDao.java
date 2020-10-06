package cn.itjohnny.dao;

import cn.itjohnny.pojo.Role;
import cn.itjohnny.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface RoleDao {

    public Set<Role> findUserId(Integer userId);


    /**
     * 新增角色
     * @param role
     * @return
     */
    boolean add(Role role);

    /**
     * 往角色权限项中间便添加项
     * 与课件不同,课件是先把数据封装在map中
     * @param roleId
     * @param permissionId
     * @return
     */
    boolean addToRoleAndPermission(@Param("roleId") Integer roleId, @Param("permissionId") Integer permissionId);


    /**
     * 按条件分页查询
     * @return
     */
    List<Role> findByQueryString(String queryString);


    /**
     * 根据id查找角色
     * @param id
     * @return
     */
    Role findRoleById(Integer id);


    /**
     * 跟新角色数据
     * @param role
     * @return
     */
    boolean update(Role role);


    /**
     * 在组项中间表中根据组id删除数据
     * @param roleId
     * @return
     */
    boolean deleteRoleAndItem(Integer roleId);

    /**
     * 根据角色id删除角色表中的角色
     * @param id
     * @return
     */
    boolean deleteById(Integer id);



}
