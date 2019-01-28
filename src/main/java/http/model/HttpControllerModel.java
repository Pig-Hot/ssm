package http.model;

import lombok.Data;

/**
 * Created by zhuran on 2019/1/28 0028
 */
@Data
public class HttpControllerModel {
    private String path;
    private String className;
    private String methodName;
    private String paramType;
    private String requestType;
}
