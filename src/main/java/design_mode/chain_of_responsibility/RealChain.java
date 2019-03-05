package design_mode.chain_of_responsibility;

import design_mode.chain_of_responsibility.info.Request;
import design_mode.chain_of_responsibility.info.Result;

import java.util.List;

/**
 * Created by zhuran on 2019/3/5 0005
 */
public class RealChain implements Ratify.Chain {
    public Request request;
    public List<Ratify> ratifyList;
    public int index;

    public RealChain(List<Ratify> ratifyList,Request request,int index){
        this.ratifyList = ratifyList;
        this.request = request;
        this.index = index;
    }

    @Override
    public Request request() {
        return request;
    }

    @Override
    public Result proceed(Request request) {
        Result proceed = null;
        if (ratifyList.size() > index) {
            RealChain realChain = new RealChain(ratifyList, request, index + 1);
            Ratify ratify = ratifyList.get(index);
            proceed = ratify.deal(realChain);
        }
        return proceed;
    }
}
