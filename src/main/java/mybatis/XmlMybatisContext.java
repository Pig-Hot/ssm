package mybatis;

import constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import rules.MyBatisRules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuran on 2019/1/24 0024
 */
@Slf4j
public class XmlMybatisContext {

    private String url;
    private String user;
    private String password;

    public void initMybatis() {
        List<Element> elementList = getElements();
        for (Element element : elementList) {
            if (element.getName().equals("jdbc")) {
                List<Element> elements = element.elements();
                for (Element childElements : elements) {
                    if (childElements.getName().equals(MyBatisRules.URL.getType())) {
                        url = childElements.attributeValue(MyBatisRules.URL.getValue());
                    } else if (childElements.getName().equals(MyBatisRules.USER.getType())) {
                        user = childElements.attributeValue(MyBatisRules.USER.getValue());
                    } else if (childElements.getName().equals(MyBatisRules.PASSWORD.getType())) {
                        password = childElements.attributeValue(MyBatisRules.PASSWORD.getValue());
                    }
                }
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    private static List<Element> getElements() {
        //创建saxReader对象
        SAXReader saxReader = new SAXReader();
        //通过read方法读取一个文件 转换成Document对象
        Document document = null;
        //解析文件的路径
        String pathName = Constants.PATH + Constants.mybatisConfigLocation;
        try {
            document = saxReader.read(pathName);
        } catch (DocumentException e) {
            log.error("文件没有被找到,{}", e);
        }
        //获取Root节点
        Element element = document.getRootElement();
        //获取所有字节点
        List<Element> elementsList = element.elements();
        return elementsList;
    }

}
