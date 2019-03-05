package design_mode.chain_of_responsibility;

import design_mode.chain_of_responsibility.info.Request;
import design_mode.chain_of_responsibility.info.Result;

public class Main {
 
     public static void main(String[] args) {
 
          Request request = new Request.Builder().setName("张三").setDays(1)
                   .setReason("事假").build();
          ChainOfResponsibilityClient client = new ChainOfResponsibilityClient();
          Result result = client.execute(request);
 
          System.out.println("结果：" + result.toString());
     } 
}
