package cn.itjohnny.dao;

import cn.itjohnny.pojo.CheckGroup;
import cn.itjohnny.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetMealDao {

    boolean add(Setmeal setmeal);

    boolean add2SetMealAndCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkGroupId") Integer checkGroupId);

    List<Setmeal> findByQueryString(String queryString);


    /**
     * 根据id查找,所有关系类集合都要封装
     * @param id
     * @return
     */
    Setmeal findById(Integer id);

    /**
     * 查询各个套餐的预约数量
     * @return
     */
    List<Map<String,Object>> getSetmealCounts();

}
