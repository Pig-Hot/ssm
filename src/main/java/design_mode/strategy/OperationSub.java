package design_mode.strategy;

/**
 * Created by zhuran on 2019/3/4 0004
 */
public class OperationSub implements Operation{
    @Override
    public int operation(int x, int y) {
        return x - y;
    }
}
