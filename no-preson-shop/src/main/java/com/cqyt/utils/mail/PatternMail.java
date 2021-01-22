package com.cqyt.utils.mail;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验邮箱是否合法
 */
@Component
public class PatternMail {
    public boolean isMail(String mail){
        Pattern compile = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");//\w表示a-z，A-Z，0-9(\\转义符)
        Matcher matcher = compile.matcher(mail);
        //格式错误
        if (matcher.matches()) {
            return false;
        }
        return true;
    }
}
