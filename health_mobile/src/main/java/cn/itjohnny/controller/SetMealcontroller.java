package cn.itjohnny.controller;


import cn.itjohnny.constant.MessageConstant;
import cn.itjohnny.entity.Result;
import cn.itjohnny.pojo.Setmeal;
import cn.itjohnny.service.SetMealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealcontroller {

    @Reference
    private SetMealService setMealService;

    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){

        try {

            List<Setmeal> setmealList = setMealService.findAll();

            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmealList);

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }

    }


    @RequestMapping("/findById")
    public Result findById(Integer id){


        try {

            Setmeal setmeal = setMealService.findById(id);

            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }


    }




}
