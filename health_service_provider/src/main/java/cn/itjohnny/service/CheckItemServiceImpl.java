package cn.itjohnny.service;

import cn.itjohnny.dao.CheckItemDao;
import cn.itjohnny.entity.PageResult;
import cn.itjohnny.entity.QueryPageBean;
import cn.itjohnny.pojo.CheckItem;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;


    @Override
    public boolean addCheckItem(CheckItem checkItem) {

        boolean add = checkItemDao.add(checkItem);

        return add;
    }


    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        Page page = PageHelper.startPage(currentPage, pageSize);
        List<CheckItem> checkItemList = checkItemDao.findByQueryString(queryString);
        long total = page.getTotal();

        return new PageResult(total,checkItemList);
    }

    @Override
    public boolean deleteById(Integer id) {

        long count = checkItemDao.findRelatedCountOfCheckGroup(id);

        if (count > 0){
            // 由于是分模块,所以生产者这边还是会打印异常消息
            throw new RuntimeException("有检查组关联,不能删除");
        }

        boolean flag = checkItemDao.deleteById(id);

        return flag;
    }

    @Override
    public CheckItem findById(Integer id) {

        CheckItem checkItem = checkItemDao.findById(id);

        return checkItem;
    }

    @Override
    public boolean editCheckItem(CheckItem checkItem) {

        boolean flag = checkItemDao.editCheckItem(checkItem);

        return flag;
    }

    @Override
    public List<CheckItem> findAll() {

        List<CheckItem> checkItemList = checkItemDao.findByQueryString(null);

        return checkItemList;
    }

    @Override
    public List findCheckItemIdByCheckGroup(Integer id) {

        List checkItemIds = checkItemDao.findCheckItemIdByCheckGroupId(id);

        return checkItemIds;
    }


}
