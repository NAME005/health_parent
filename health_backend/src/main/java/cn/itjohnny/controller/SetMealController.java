package cn.itjohnny.controller;


import cn.itjohnny.constant.MessageConstant;
import cn.itjohnny.constant.RedisConstant;
import cn.itjohnny.entity.PageResult;
import cn.itjohnny.entity.QueryPageBean;
import cn.itjohnny.entity.Result;
import cn.itjohnny.pojo.CheckGroup;
import cn.itjohnny.pojo.Setmeal;
import cn.itjohnny.service.CheckGroupService;
import cn.itjohnny.service.SetMealService;
import cn.itjohnny.util.QiniuUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * 检查项相关
 */
@RestController
@RequestMapping("/setmeal")  // TODO 编辑删除没做
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 图片上传
     * @param multipartFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile multipartFile){

        Result result = null;
        try {
            byte[] fileBytes = multipartFile.getBytes();
            String[] split = multipartFile.getOriginalFilename().split("\\.");
            String suffix = "."+split[split.length - 1];
            String imgName = UUID.randomUUID().toString() + suffix;
            QiniuUtils.upload2Qiniu(fileBytes,imgName);
            result = new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,imgName);
            // 上传成功后将文件名保存到redis的set集合中
            // 因为不需要调用service所以在这层执行
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,imgName);
        }catch (Exception e){
            result = new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }

        return result;
    }

    /**
     * 新增套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        Result result = null;
        try {
            setMealService.add(setmeal,checkgroupIds);
            result = new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        }catch (Exception e){
            result = new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return result;
    }


    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

            PageResult pageResult = setMealService.findPage(queryPageBean);

        return pageResult;
    }


}
