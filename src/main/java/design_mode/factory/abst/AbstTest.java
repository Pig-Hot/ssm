package design_mode.factory.abst;

/**
 * Created by zhuran on 2019/3/4 0004
 */
public class AbstTest {
    public static void main(String[] args) {
        Provider provider = new SendMailFactory();
        provider.produce().send();
    }
}
