package cn.itjohnny.service;

import cn.itjohnny.constant.RedisConstant;
import cn.itjohnny.dao.SetMealDao;
import cn.itjohnny.entity.PageResult;
import cn.itjohnny.entity.QueryPageBean;
import cn.itjohnny.pojo.CheckGroup;
import cn.itjohnny.pojo.Setmeal;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")
    private String outputPath;

    @Override
    public boolean add(Setmeal setmeal, Integer[] checkGroupIds) throws Exception {

        setMealDao.add(setmeal);

        // 这个id是执行完上面的方法才封装进去的
        Integer setmealId = setmeal.getId();

        if (checkGroupIds != null){
            for (Integer checkGroupId : checkGroupIds) {

                setMealDao.add2SetMealAndCheckGroup(setmealId,checkGroupId);
            }
        }

        // 添加成功后将图片名称保存到redis
        String img = setmeal.getImg();
        if (img == null || "".equals(img)){
            return true;
        }else {
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
        }

        generateMobileSetmealPage();

        generateMobileSetmealDetailPage();


        return true;
    }

    // 生成移动端套餐列表静态页面的方法
    public void generateMobileSetmealPage() throws Exception {
            FileWriter writer = null;

            List<Setmeal> setmealList = setMealDao.findByQueryString(null);

            HashMap map = new HashMap();

            map.put("setmealList",setmealList);

            String outName = "/m_setmeal.html";

            generatePage("mobile_setmeal.ftl",map,outName);


    }

    // 生成(所有)移动端套餐详细信息页面的方法
    public void generateMobileSetmealDetailPage() throws Exception {

            List<Setmeal> setmealList = setMealDao.findByQueryString(null);

            String outName = null;

            for (Setmeal setmeal : setmealList) {
                Integer id = setmeal.getId();
                HashMap module = new HashMap();
                Setmeal setmeal02 = findById(id);
                System.out.println("id: "+setmeal02.getId());
                module.put("setmeal",setmeal02);
                outName = "/m_setmeal_detail_" + id + ".html";
                generatePage("mobile_setmeal_detail.ftl",module,outName);
            }

    }

    // 生成静态页面通用方法
    public void generatePage(String templateName, Map module, String outName) throws Exception {

        FileWriter writer = new FileWriter(new File(outputPath + outName));
        // 获取配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //4.加载模板
        Template template = configuration.getTemplate(templateName);
        //7.输出
        template.process(module,writer);
        //8.关流
        writer.close();
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        Page page = PageHelper.startPage(currentPage, pageSize);
        List<Setmeal> setmealList = setMealDao.findByQueryString(queryString);
        long total = page.getTotal(); // 实现方式跟课件不一样

        return new PageResult(total,setmealList);

    }

    @Override
    public List<Setmeal> findAll() {

        List<Setmeal> setmealList = setMealDao.findByQueryString(null);

        return setmealList;
    }

    @Override
    public Setmeal findById(Integer id) {

        Setmeal setmeal = setMealDao.findById(id);

        return setmeal;
    }




    @Override
    public Map<String, Object> getSetmealReport() {

        // 先查询各个套餐预约数量,封装集合
        List<Map<String, Object>> setmealCount = setMealDao.getSetmealCounts();

        // 保存套餐名字集合
        ArrayList<String> setmealNames = new ArrayList<>();
        for (Map<String, Object> map : setmealCount) {
            // 每个map都存了两个键值对,分别存了name和value,是{name:xxx,value:yyy},而不是{xxx:yyy}
            String name = (String) map.get("name");
            setmealNames.add(name);

        }

        // 封装结果map
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("setmealCount",setmealCount);
        resultMap.put("setmealNames",setmealNames);

        return resultMap;
    }
}
