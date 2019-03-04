package design_mode.factory.simple;

/**
 * Created by zhuran on 2019/3/4 0004
 */
public class MailSender implements Sender{
    @Override
    public void send() {
        System.out.println("Mail send");
    }
}
