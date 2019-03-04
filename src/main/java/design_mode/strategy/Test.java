package design_mode.strategy;

/**
 * Created by zhuran on 2019/3/4 0004
 */
public class Test {
    public static void main(String[] args) {
        Context context = new Context(new OperationAdd());
        context.executeStrategy(1,2);
    }
}
