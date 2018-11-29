package org.yuanmeng.zwolfserver.sso.entity.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wangtonghe
 * @since 2018/11/30 00:32
 */
@Data
public class ServiceResult implements Serializable {

    private int code;

    private String msg;

    private Object data;
}
