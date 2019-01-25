package constants;

import resource.FileSystemXmlApplicationContext;

/**
 * Created by zhuran on 2019/1/16 0016
 * 配置文件路径
 */
public interface Constants {
    String PATH = FileSystemXmlApplicationContext.class.getResource("/").getPath();
    String contextConfigLocation = "application.xml";
    String springmvcConfigLocation = "spring-mvc.xml";
    String mybatisConfigLocation = "mybatis.xml";
}
