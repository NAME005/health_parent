package cn.itjohnny.service;

import cn.itjohnny.dao.PermissionDao;
import cn.itjohnny.dao.RoleDao;
import cn.itjohnny.dao.UserDao;
import cn.itjohnny.pojo.Permission;
import cn.itjohnny.pojo.Role;
import cn.itjohnny.pojo.User;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;


    @Override
    public User findByUsername(String username) {
        // 查询user简单信息
        User user = userDao.findByUsername(username);
        if (user == null){return null;}
        // 封装角色
        Set<Role> roles = roleDao.findUserId(user.getId());
        user.setRoles(roles);
        // 往角色封装权限
        if (roles != null){
            for (Role role : roles) {
                Set<Permission> permissions = permissionDao.findByRoleId(role.getId());
                role.setPermissions(permissions);
            }
        }

        return user;
    }
}
