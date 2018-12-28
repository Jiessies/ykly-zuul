package com.ykly.zuul.entity;

import lombok.Data;

@Data
public class MQOrderMsg {
    private String orderNo;
    private String userName;
    private String userType;
    private Integer orderPrice;
}
