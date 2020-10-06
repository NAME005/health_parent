package cn.itjohnny.service;

import cn.itjohnny.dao.PermissionDao;
import cn.itjohnny.entity.PageResult;
import cn.itjohnny.entity.QueryPageBean;
import cn.itjohnny.pojo.Permission;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;


    @Override
    public boolean addPermission(Permission permission) {

        boolean add = permissionDao.add(permission);

        return add;
    }


    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        Page page = PageHelper.startPage(currentPage, pageSize);
        List<Permission> permissionList = permissionDao.findByQueryString(queryString);
        long total = page.getTotal();

        return new PageResult(total,permissionList);
    }

    @Override
    public boolean deleteById(Integer id) {

        long count = permissionDao.findRelatedCountOfRole(id);

        if (count > 0){
            // 由于是分模块,所以生产者这边还是会打印异常消息
            throw new RuntimeException("有检查组关联,不能删除");
        }

        boolean flag = permissionDao.deleteById(id);

        return flag;
    }

    @Override
    public Permission findById(Integer id) {

        Permission permission = permissionDao.findById(id);

        return permission;
    }

    @Override
    public boolean editPermission(Permission permission) {

        boolean flag = permissionDao.editPermission(permission);

        return flag;
    }

    @Override
    public List<Permission> findAll() {

        List<Permission> permissionList = permissionDao.findByQueryString(null);

        return permissionList;
    }

    @Override
    public List findPermissionIdByRole(Integer id) {

        List permissionIds = permissionDao.findPermissionIdByRoleId(id);

        return permissionIds;
    }


}
