package test.dao;

import annotation.MyParam;
import annotation.MyRepository;
import annotation.MySelect;
import test.model.TestModel;

import java.util.List;


/**
 * Created by zhuran on 2019/1/23 0023
 */
@MyRepository
public interface TestDao {
    @MySelect(value = "select * from test.test where service_id = #{service_id}")
    List<TestModel> test(@MyParam(value = "service_id") String name);
}
