package com.cqyt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册账户传值
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserVo {
    String userName;
    String password;
    String mailNumber;
    String code;
}
