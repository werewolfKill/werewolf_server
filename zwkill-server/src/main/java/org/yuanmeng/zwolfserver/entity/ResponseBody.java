package org.yuanmeng.zwolfserver.entity;

import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.util.Map;

/**
 * 封装的响应实体
 *
 * @author wangtonghe
 * @date 2017/7/24 18:08
 */
@Data
public class ResponseBody {

    /**
     * 响应地址
     */
    private String url;

    /**
     * 响应码
     */
    private int code;

    /**
     * 服务id
     */
    private short serviceId;

    /**
     * commandId
     */
    private short command;

    /**
     * 业务用int表示响应码，具体含义与具体命令有关,不用设0
     */
    private int reply;

    /**
     * 发送者id
     */
    private int fromId;

    /**
     * 其他参数
     */
    private Map<String, Object> param;

    public ResponseBody() {
    }

    public ResponseBody(int code, String url, Map<String, Object> param) {
        this.code = code;
        this.url = url;
        this.param = param;
    }

    public ResponseBody(short serviceId, short command, int fromId, int reply) {
        this.serviceId = serviceId;
        this.command = command;
        this.reply = reply;
        this.fromId = fromId;
    }


}
