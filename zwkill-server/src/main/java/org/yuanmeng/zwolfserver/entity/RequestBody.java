package org.yuanmeng.zwolfserver.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangtonghe
 * @since 2018/10/5 15:08
 */
@Data
public class RequestBody {

    private final static String NULL_STRING = "null";

    private int length;

    /**
     * 请求url
     */
    private String url;

    /**
     * 请求参数
     */
    private Map<String, Object> paramMap = new HashMap<>();


    private int code;

    private int roomId;

    private int fromId;

    private int bout;

    private String param;


    public void setParam(String paramStr) {
        String[] paramArr = paramStr.split("&");
        Arrays.stream(paramArr).forEach((pa -> {
            String[] eachArr = pa.split("=");
            String value = eachArr[0];
            String name = eachArr.length == 2 ? eachArr[1] : "";
            if (StringUtils.isNumeric(name)) {
                paramMap.put(value, Integer.valueOf(name));
            } else {
                paramMap.put(value, name);
            }
        }));
    }


}
