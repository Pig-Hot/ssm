package rules;

/**
 * Created by zhuran on 2019/1/16 0016
 */
public enum IocRules {
    BEAN_RULE("bean", "id", "class"),
    SNAN_RULE("component-scan", "base-package", "null"),
    /**
     * set注入的规则
     */
    SET_INJECT("property", "name", "value"),
    /**
     * 构造器注入的规则
     */
    CONS_INJECT("constructor-arg", "value", "index");
    private String type;
    private String name;
    private String value;

    IocRules(String property, String name, String value) {
        this.type = property;
        this.name = name;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
