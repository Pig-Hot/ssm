package design_mode.proxy;

import java.util.List;

public class UserServImpl implements IUser {
    public int deleteUser(int id) {
        System.out.println("******执行删除方法******");
        return 0;
    }

    public List<String> findAllUser() {
        System.out.println("*******执行查询方法*******");
        return null;
    }

    public int saveUser(String userName) {
        System.out.println("*******执行添加方法********");
        return 0;
    }
}