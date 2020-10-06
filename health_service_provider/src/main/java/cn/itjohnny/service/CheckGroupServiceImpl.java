package cn.itjohnny.service;

import cn.itjohnny.dao.CheckGroupDao;
import cn.itjohnny.dao.CheckItemDao;
import cn.itjohnny.entity.PageResult;
import cn.itjohnny.entity.QueryPageBean;
import cn.itjohnny.pojo.CheckGroup;
import cn.itjohnny.pojo.CheckItem;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 抽取方法往检查组检查项中间表循环添加数据
     * @param checkitemIds
     * @param checkGroupId
     * @return
     */
    private boolean addToCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        // 往检查项检查组中间表中添加数据
        // 先做健壮性判断
        if (checkitemIds == null || checkitemIds.length <= 0 ){
            return true;
        }
        for (Integer checkitemId : checkitemIds) {
            checkGroupDao.addToCheckGroupAndCheckitem(checkGroupId , checkitemId);
        }
        return true;
    }


    @Override
    public boolean add(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 添加检查组
        checkGroupDao.add(checkGroup);
        Integer checkGroupId = checkGroup.getId();
        // 往检查项检查组中间表中添加数据
        addToCheckGroupAndCheckItem(checkGroupId,checkitemIds);

        return true;
    }


    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        Page page = PageHelper.startPage(currentPage, pageSize);
        List<CheckGroup> checkItemList = checkGroupDao.findByQueryString(queryString);
        long total = page.getTotal(); // 实现方式跟课件不一样

        return new PageResult(total,checkItemList);
    }

    @Override
    public CheckGroup findCheckGroup(Integer id) {

        CheckGroup checkGroup = checkGroupDao.findCheckGroupById(id);

        return checkGroup;
    }

    @Override
    public boolean edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        Integer checkGroupId = checkGroup.getId();
        // 先更新  // 这里的调用顺序与课件不同
        checkGroupDao.update(checkGroup);
        // 把所有该检查组在组项中间表的关系删除
        checkGroupDao.deleteCheckGroupAndItem(checkGroupId);
        // 往检查项检查组中间表中添加数据
        addToCheckGroupAndCheckItem(checkGroupId,checkitemIds);

        return true;
    }

    @Override
    public boolean deleteById(Integer id) {

        // 先删除中间关系表关系
        boolean b = checkGroupDao.deleteCheckGroupAndItem(id);

        // 再删除检查组表中的检查组
        boolean b1 = checkGroupDao.deleteById(id);

        return true;
    }

    @Override
    public List<CheckGroup> findAll() {

        List<CheckGroup> checkGroupList = checkGroupDao.findByQueryString(null);

        return checkGroupList;
    }


}
