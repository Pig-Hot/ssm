package test.server.impl;

import annotation.MyAutowired;
import annotation.MyService;
import test.dao.TestDao;
import test.model.TestModel;
import test.server.TestServer;

import java.util.List;

/**
 * Created by zhuran on 2019/1/23 0023
 */
@MyService
public class TestServerImpl implements TestServer {
    @MyAutowired
    private TestDao testDao;

    @Override
    public String test(String s) {
        List<TestModel> list = testDao.test(s);
        return list.get(0).getServiceId();
    }
}
