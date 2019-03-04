package design_mode.factory.abst;

import design_mode.factory.simple.MailSender;
import design_mode.factory.simple.Sender;

/**
 * Created by zhuran on 2019/3/4 0004
 */
public class SendMailFactory implements Provider{
    @Override
    public Sender produce() {
        return new MailSender();
    }
}
