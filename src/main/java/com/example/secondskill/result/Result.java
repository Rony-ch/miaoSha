package com.example.secondskill.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    /*
    成功时调用
     */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }
    public Result(T data) {
        this.code=0;
        this.msg="success";
        this.data=data;
    }
    /*
    失败时调用
     */
    public static <T> Result<T> error(CodeMsg cm){
        return new Result<T>(cm);
    }
    public Result(CodeMsg cm){
        if(cm==null){
            return;
        }
        this.code=cm.getCode();
        this.msg=cm.getMsg();
    }
}
