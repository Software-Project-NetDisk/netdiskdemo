package com.springboot.entity;

// 超出 2xx 范围的状态码都会被前端响应拦截器当成请求出错并拦截。
public enum ReturnCode {
    /**操作成功**/
    RC200(200,"操作成功"),
    /**操作失败**/
    RC999(999,"操作失败"),
    /**服务限流**/
//    RC201(201,"服务开启降级保护,请稍后再试!"),
//    /**热点参数限流**/
//    RC202(202,"热点参数限流,请稍后再试!"),
//    /**系统规则不满足**/
//    RC203(203,"系统规则不满足要求,请稍后再试!"),
//    /**授权规则不通过**/
//    RC204(204,"授权规则不通过,请稍后再试!"),
//    /**access_denied**/
//    RC403(403,"无访问权限,请联系管理员授予权限"),
//    /**access_denied**/
//    RC401(401,"匿名用户访问无权限资源时的异常"),
    /**服务异常**/
    FILE_EXIT(300, "文件已存在"),
    NEED_MERGE(301, "需要合并"),
    UPLOAD_FAILED(400, "上传文件失败"),
    MERGE_ERROR(401, "文件合并失败"),
    WRITE_ERROR(415, "写文件出错"),
    RC500(500,"系统异常，请稍后重试"),

    CLIENT_AUTHENTICATION_FAILED(1001,"客户端认证失败"),
    EMAIL_OR_PASSWORD_ERROR(1002,"邮箱或密码错误"),
    UNSUPPORTED_GRANT_TYPE(1003, "不支持的认证模式"),

    INVALID_TOKEN(2001,"token不合法"),
    OVERDUE_TOKEN(2002, "token已过期"),
    // 未携带token
    LOST_TOKEN(2003, "请先登录"),
    ACCESS_DENIED(2004,"没有权限访问该资源"),

    EMAIL_EXIST(3001, "该邮箱已注册"),
    FAILED_TO_CREATE_FOLDER(3002, "该文件夹已存在，创建文件夹失败");

    /**自定义状态码**/
    private final int code;
    /**自定义描述**/
    private final String message;

    ReturnCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
