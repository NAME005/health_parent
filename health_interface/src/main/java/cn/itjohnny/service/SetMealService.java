package cn.itjohnny.service;

import cn.itjohnny.entity.PageResult;
import cn.itjohnny.entity.QueryPageBean;
import cn.itjohnny.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealService {

    boolean add(Setmeal setmeal, Integer[] checkGroupIds) throws Exception;

    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 纯粹的查询所有
     * @return
     */
    List<Setmeal> findAll();


    /**
     * 根据id查找,所有关系类集合都要封装
     * @param id
     * @return
     */
    Setmeal findById(Integer id);


    /**
     * 获取各个套餐的占比信息,包括套餐名字的集合,每个套餐预定的数量
     * @return
     */
    Map<String,Object> getSetmealReport();


}
