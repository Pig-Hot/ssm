package design_mode.proxy;

import java.util.List;

public interface IUser {
    List<String> findAllUser();
    int deleteUser(int id);
    int saveUser(String userName);
}