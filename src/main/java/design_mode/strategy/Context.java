package design_mode.strategy;

/**
 * Created by zhuran on 2019/3/4 0004
 */
public class Context {
    private Operation operation;
    public Context(Operation operation){
        this.operation = operation;
    }
    public int executeStrategy(int num1, int num2){
        return operation.operation(num1, num2);
    }
}
