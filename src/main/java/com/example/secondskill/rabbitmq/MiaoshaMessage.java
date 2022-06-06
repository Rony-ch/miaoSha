package com.example.secondskill.rabbitmq;

import com.example.secondskill.domain.MiaoShaUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiaoshaMessage {
    private MiaoShaUser user;
    private long goodsId;
}
