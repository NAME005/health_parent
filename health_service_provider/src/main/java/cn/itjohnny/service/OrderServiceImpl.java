package cn.itjohnny.service;

import cn.itjohnny.constant.MessageConstant;
import cn.itjohnny.dao.MemberDao;
import cn.itjohnny.dao.OrderDao;
import cn.itjohnny.dao.OrderSettingDao;
import cn.itjohnny.dao.SetMealDao;
import cn.itjohnny.entity.Result;
import cn.itjohnny.pojo.Member;
import cn.itjohnny.pojo.Order;
import cn.itjohnny.pojo.OrderSetting;
import cn.itjohnny.pojo.Setmeal;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private SetMealDao setMealDao;


    @Override
    public Result submitOrder(Map map) throws Exception {

        String telephone = (String) map.get("telephone");
        // 先判断用户是否是会员, 跟课件不一样, 这里是即使预约失败也帮用户注册会员
        Member member = memberDao.findByTelephone(telephone);

        if (member == null){
            // 不是会员,登记成会员
            String name = (String) map.get("name");
            String sex = (String) map.get("sex");
            String idCard = (String) map.get("idCard");
            member = new Member();
            member.setName(name);
            member.setSex(sex);
            member.setIdCard(idCard);
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date()); // 这里后添加了注册时间
            // 若不是会员,经过下面的添加之后id已经被赋上值,若是会员则id本身有值,后面需要再取
            memberDao.add(member);
        }

        // 校验该日期是否设置了可预约人数,以及是否约满
        String orderDate = (String) map.get("orderDate");
        // 这里加了类型转换,单独获取日期对象
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(orderDate);

        long count = orderSettingDao.countByOrderDate(date);
        if (count == 0){
            // 该日期未设置预约,不能预约
            return new Result(false,MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        // 该日期可预约,检查是否约满  // 这里跟课件逻辑不一样,课件是分别查已预约人数和可预约人数,在代码中对比
        OrderSetting fullOrderSetting = orderSettingDao.findFullOrderSettingByDate(date);
        if (fullOrderSetting != null){
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        // 校验是否存在完全相同的订单 -- memberID,订单日期,套餐ID 均相同
        String setmealId = (String) map.get("setmealId");
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(date);
        order.setSetmealId(Integer.parseInt(setmealId));
        List<Order> orders = orderDao.findByCondition(order);
        // 注意这里使用&&连接,两个都要满足
        if (orders != null && orders.size() != 0 ){
            return new Result(false,MessageConstant.HAS_ORDERED);
        }

        // 校验通过,补全order信息,添加订单
        order.setOrderType(Order.ORDERTYPE_WEIXIN);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        orderDao.add(order);
        // 并维护ordersetting表已预约人数
        OrderSetting orderSetting = orderSettingDao.findByDate(date);
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.updateByOrderDate(orderSetting);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId() + "");
    }


    /*<p>体检人：{{orderInfo.member}}</p>
      <p>体检套餐：{{orderInfo.setmeal}}</p>
      <p>体检日期：{{orderInfo.orderDate}}</p>
      <p>预约类型：{{orderInfo.orderType}}</p>*/
    @Override
    public Map getSuccessPageByOrderId(Integer id) {

        // Map detail = orderDao.findById4Detail(id); // 课件专门封装了一个sql

        Order order = new Order(id);

        List<Order> orders = orderDao.findByCondition(order);
        if (orders == null || orders.size() <= 0 || orders.get(0) == null){
            throw new RuntimeException();
        }

        order = orders.get(0);

        // 未做健壮性判断,报空指针则直接提示查询失败
        String member = memberDao.findById(order.getMemberId()).getName();

        String setmeal = setMealDao.findById(order.getSetmealId()).getName();

        HashMap map = new HashMap();

        map.put("member",member);
        map.put("setmeal",setmeal);
        map.put("orderDate",order.getOrderDate());
        map.put("orderType",order.getOrderType());

        return map;
    }





}
