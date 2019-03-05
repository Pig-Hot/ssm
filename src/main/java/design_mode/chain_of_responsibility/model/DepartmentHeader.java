package design_mode.chain_of_responsibility.model;

import design_mode.chain_of_responsibility.Ratify;
import design_mode.chain_of_responsibility.info.Request;
import design_mode.chain_of_responsibility.info.Result;

public class DepartmentHeader implements Ratify {
 
     @Override
     public Result deal(Chain chain) {
          Request request = chain.request();
          System.out.println("DepartmentHeader=====>request:"
                   + request);
          if (request.getDays() > 7) {
              return new Result(false, "你这个完全没必要");
          }
          return new Result(true, "DepartmentHeader：不要着急，把事情处理完再回来！");
     }
 
}
