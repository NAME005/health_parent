package cn.itjohnny.service;

import cn.itjohnny.entity.PageResult;
import cn.itjohnny.entity.QueryPageBean;
import cn.itjohnny.pojo.Permission;

import java.util.List;

public interface PermissionService {

    /**
     * 添加权限项目
     * @param permission
     * @return
     */
    boolean addPermission(Permission permission);

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
     * 根据id查找权限项
     * @param id
     * @return
     */
    Permission findById(Integer id);

    /**
     * 编辑权限项
     * @param permission
     * @return
     */
    boolean editPermission(Permission permission);

    /**
     * 查询所有权限项
     * @return
     */
    List<Permission> findAll();

    /**
     * 根据角色id查找权限项id
     * @param id
     * @return
     */
    List findPermissionIdByRole(Integer id);
}
