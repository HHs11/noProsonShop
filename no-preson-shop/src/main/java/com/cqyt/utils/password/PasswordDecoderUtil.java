package com.cqyt.utils.password;
import java.util.Base64;

/**
 * @author lh 密码加密工具类
 * @date 2019/8/29 14:21
 */
public class PasswordDecoderUtil {
    //准备盐值
    public static final String SALT = "Athena";

    /**
     * 传用户传到controller的密码，此时的密码是明文
     *
     * @param password
     * @return
     */
    public static String getDecodePassword(String password) {
        String salt = "{{" + SALT + "}}";
        // 如果要更加安全，请用base64位加密方式对盐值进行加密
        // 用bAse64对盐值加密
        byte[] encode = Base64.getEncoder().encode(salt.getBytes());
        // 创建加密盐值字符串
        String encodeSalt = new String(encode);
        password = password + encodeSalt;
        for (int x = 0; x < 3; x++) { // 做一个三次循环MD5加密
            password = new MD5Code().getMD5ofStr(password);
        }
        return password;
    }
}
