package com.example.secondskill.conroller;

import com.example.secondskill.rabbitmq.MQSender;
import com.example.secondskill.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {
    @Autowired
    MQSender mqSender;
    @RequestMapping("mq")
    @ResponseBody
    public Result<String> mq(){
        mqSender.send("hello   pppppmmmm");
        return Result.success("hello mmmmqqqqq");
    }
}
