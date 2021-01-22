package com.cqyt.utils.uuid;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author LiBoy
 */
@Component
public class ReturnUUID {
    //得到32位的uuid
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
