package test.controller;

import annotation.MyAutowired;
import annotation.MyController;
import annotation.MyRequest;
import test.server.TestServer;

/**
 * Created by zhuran on 2019/1/23 0023
 */
@MyController
public class TestController {
    @MyAutowired
    private TestServer testServer;

    @MyRequest(path = "/test", type = "post")
    public String test(String s) {
        return testServer.test(s);
    }
}
