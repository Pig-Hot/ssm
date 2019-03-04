package design_mode.single;

/**
 * Created by zhuran on 2019/3/4 0004
 */
public class Single {
    private static Single single = new Single();
    private Single(){

    }
    public static Single getInstace(){
        return single;
    }
}
