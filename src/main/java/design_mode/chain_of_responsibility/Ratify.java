package design_mode.chain_of_responsibility;

import design_mode.chain_of_responsibility.info.Request;
import design_mode.chain_of_responsibility.info.Result;

public interface Ratify {
     // 处理请求
     Result deal(Chain chain);

     /**
      * 接口描述：对request和Result封装，用来转发
      */
     interface Chain {
          // 获取当前request
          Request request();

          // 转发request
          Result proceed(Request request);
     }

}