package design_mode.chain_of_responsibility.model;

import design_mode.chain_of_responsibility.Ratify;
import design_mode.chain_of_responsibility.info.Request;
import design_mode.chain_of_responsibility.info.Result;

public class Manager implements Ratify {
 
     @Override
     public Result deal(Chain chain) {
          Request request = chain.request();
          System.out.println("Manager=====>request:" + request);
          if (request.getDays() > 3) {
              // 构建新的Request
              Request newRequest = new Request.Builder().newRequest(request)
                        .setManagerInfo(request.getName() + "每月的KPI考核还不错，可以批准")
                        .build();
              return chain.proceed(newRequest);
 
          }
          return new Result(true, "Manager：早点把事情办完，项目离不开你");
     }
 
}