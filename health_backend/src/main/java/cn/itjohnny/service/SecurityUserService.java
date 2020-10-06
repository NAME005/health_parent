package cn.itjohnny.service;

import cn.itjohnny.pojo.Permission;
import cn.itjohnny.pojo.Role;
import cn.itjohnny.pojo.User;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

@Component
public class SecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null){
            // 用户不存在,返回空值
            return null;
        }
        // 创建集合用户保存角色和权限
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        // 遍历保存
        Set<Role> roles = user.getRoles();
        if (roles != null){
            for (Role role : roles) {
                // 保存角色
                authorities.add(new SimpleGrantedAuthority(role.getKeyword()));
                // 保存权限
                Set<Permission> permissions = role.getPermissions();
                if (permissions != null){
                    for (Permission permission : permissions) {
                        authorities.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }


        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);
    }












}
