package cn.itjohnny.jobs;

import cn.itjohnny.constant.RedisConstant;
import cn.itjohnny.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class CleanImgs {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 定时清理垃圾图片
     */
    public void clean(){
        // 根据Redis中保存的两个set集合进行差值计算，获得垃圾图片名称集合
        // 前者减去后者
        Set<String> set = jedisPool.getResource()
                .sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        System.out.println("=======================");
        // 先做健壮性判断
        if (set == null || set.size() == 0){return;}
        // 再删除云端存储中对应的图片
        for (String name : set) {
            QiniuUtils.deleteFileFromQiniu(name);
            // 再把redis中相关内容删除
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,name);
        }


    }


}
