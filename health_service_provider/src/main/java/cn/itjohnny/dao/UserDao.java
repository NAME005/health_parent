package cn.itjohnny.dao;


import cn.itjohnny.pojo.User;

public interface UserDao {

    /**
     * 查询User简单信息
     * @param username
     * @return
     */
    public User findByUsername(String username);

}
