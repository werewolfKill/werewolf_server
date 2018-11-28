package org.yuanmeng.zwolfserver.metadata.business;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 请求信息
 *
 * @author wangtonghe
 * @since 2018/10/6 13:35
 */
@Data
@AllArgsConstructor
public class RequestInfo {

    private Channel channel;

    private String targetUrl;

}
