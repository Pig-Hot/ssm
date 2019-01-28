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

    @MyRequest(path = "/test",type = "post")
    public void test(String s){
        System.out.println(testServer.test(s));
    }
}
