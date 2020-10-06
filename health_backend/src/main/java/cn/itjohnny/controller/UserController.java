package cn.itjohnny.controller;

import cn.itjohnny.constant.MessageConstant;
import cn.itjohnny.entity.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {


    @RequestMapping("/getUsername")
    public Result getUsername(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication = " + authentication);
        // 获取UserDetail实现类User
        User user = (User) authentication.getPrincipal();

        if (user == null){return new Result(false,MessageConstant.GET_USERNAME_FAIL);}

        return new Result(true,MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());

    }


}
