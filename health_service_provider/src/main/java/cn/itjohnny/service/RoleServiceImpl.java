package cn.itjohnny.service;

import cn.itjohnny.dao.RoleDao;
import cn.itjohnny.entity.PageResult;
import cn.itjohnny.entity.QueryPageBean;
import cn.itjohnny.pojo.Role;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    /**
     * 抽取方法往角色权限项中间表循环添加数据
     * @param permissionIds
     * @param roleId
     * @return
     */
    private boolean addToRoleAndPermission(Integer roleId, Integer[] permissionIds) {
        // 往权限项角色中间表中添加数据
        // 先做健壮性判断
        if (permissionIds == null || permissionIds.length <= 0 ){
            return true;
        }
        for (Integer permissionId : permissionIds) {
            roleDao.addToRoleAndPermission(roleId , permissionId);
        }
        return true;
    }


    @Override
    public boolean add(Role role, Integer[] permissionIds) {
        // 添加角色
        roleDao.add(role);
        Integer roleId = role.getId();
        // 往权限项角色中间表中添加数据
        addToRoleAndPermission(roleId,permissionIds);

        return true;
    }


    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        Page page = PageHelper.startPage(currentPage, pageSize);
        List<Role> checkItemList = roleDao.findByQueryString(queryString);
        long total = page.getTotal(); // 实现方式跟课件不一样

        return new PageResult(total,checkItemList);
    }

    @Override
    public Role findRole(Integer id) {

        Role role = roleDao.findRoleById(id);

        return role;
    }

    @Override
    public boolean edit(Role role, Integer[] permissionIds) {
        Integer roleId = role.getId();
        // 先更新  // 这里的调用顺序与课件不同
        roleDao.update(role);
        // 把所有该角色在组项中间表的关系删除
        roleDao.deleteRoleAndItem(roleId);
        // 往权限项角色中间表中添加数据
        addToRoleAndPermission(roleId,permissionIds);

        return true;
    }

    @Override
    public boolean deleteById(Integer id) {

        // 先删除中间关系表关系
        boolean b = roleDao.deleteRoleAndItem(id);

        // 再删除角色表中的角色
        boolean b1 = roleDao.deleteById(id);

        return true;
    }

    @Override
    public List<Role> findAll() {

        List<Role> roleList = roleDao.findByQueryString(null);

        return roleList;
    }


}
