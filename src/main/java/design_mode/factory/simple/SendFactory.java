package design_mode.factory.simple;

/**
 * Created by zhuran on 2019/3/4 0004
 */
public class SendFactory {
    public static Sender produceMail() {
        return new MailSender();
    }

    public static Sender produceQQ() {
        return new QQSender();
    }
}
