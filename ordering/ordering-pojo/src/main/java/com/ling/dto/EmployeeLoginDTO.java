package com.ling.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "员工登录时传递的数据模型")
public class EmployeeLoginDTO implements Serializable {

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("姓名")
    private  String name;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("id码")
    private String idNumber;



}
