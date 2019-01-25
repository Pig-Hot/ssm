package rules;

/**
 * Created by zhuran on 2019/1/24 0024
 */
public enum MyBatisRules {
    URL("url", "value"),
    USER("user", "value"),
    PASSWORD("password", "value");
    private String type;
    private String value;

    MyBatisRules(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

}
