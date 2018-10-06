package org.yuanmeng.zwolfserver.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.yuanmeng.zwolfserver.entity.RequestBody;
import org.yuanmeng.zwolfserver.entity.ResponseBody;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangtonghe
 * @since 2018/10/6 09:50
 */
public class RequestToTypeCodec extends ByteToMessageCodec<ResponseBody> {

    private static final String LINE_INNER_SEPARATOR = " ";


    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseBody msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getCode())
                .writeBytes(msg.getUrl().getBytes())
                .writeChar(' ');
        if (msg.getParam() != null) {
            String paramStr = getParamStr(msg.getParam());
            out.writeBytes(paramStr.getBytes());
        }
        out.writeChar('\n');
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        int paramLen = 2;
        int length = in.readableBytes();
        ByteBuf buf = in.readBytes(length);
        String frame = buf.toString(Charset.forName("UTF-8"));
        String[] msgArr = frame.split(LINE_INNER_SEPARATOR);
        RequestBody requestBody = new RequestBody();
        if (msgArr.length == paramLen) {
            requestBody.setUrl(msgArr[0]);
            requestBody.setParam(msgArr[1]);
        } else {
            requestBody.setUrl(msgArr[0]);
        }
        out.add(requestBody);
    }

    private String getParamStr(Map<String, Object> paramMap) {
        StringBuilder sb = new StringBuilder();
        paramMap.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("&");
        });
        return sb.toString().substring(0, sb.length() - 2);
    }
}
