package org.yuanmeng.zwolfserver.bean;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.yuanmeng.zwolfserver.annotation.RequestMapping;
import org.yuanmeng.zwolfserver.entity.RequestBody;
import org.yuanmeng.zwolfserver.entity.RequestInfo;
import org.yuanmeng.zwolfserver.exception.TooManyMappingException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息分发bean
 *
 * @author wangtonghe
 * @since 2018/10/5 15:33
 */
@Component
public class MsgDispatcherBean {

    private Logger logger = LoggerFactory.getLogger(MsgDispatcherBean.class);

    private static final String CHANNEL_NAME = "";

    private static final String STRING_NAME = "java.lang.String";

    private static final String INTEGER_NAME = "java.lang.Integer";


    public void dispatcher(RequestBody requestBody, Channel channel) {

        String targetUrl = requestBody.getUrl();
        Map<String, Object> requestParam = requestBody.getParamMap();
        String[] beans = ApplicationContextHelper.getBeans(Controller.class);
        List<Method> methodsList = new ArrayList<>();
        Arrays.stream(beans).forEach(bean -> {
            Class<?> clazz = ApplicationContextHelper.getType(bean);
            Method[] methods = clazz.getMethods();
            List<Method> methodList = Arrays.stream(methods).filter(m -> {
                RequestMapping mapping = m.getAnnotation(RequestMapping.class);
                if (mapping != null) {
                    String url = mapping.value();
                    if (targetUrl.equalsIgnoreCase(url)) {
                        return true;
                    }
                }
                return false;
            }).collect(Collectors.toList());
            methodsList.addAll(methodList);
        });
        if (methodsList.size() > 1) {
            throw new TooManyMappingException();
        }
        if (methodsList.size() == 0) {
            return;
        }
        Method method = methodsList.get(0);
        Class<?> clazz = method.getDeclaringClass();
        try {
            Object[] args = getRealParamValue(requestParam, method, channel, targetUrl);
            method.invoke(clazz.newInstance(), args);
        } catch (Exception e) {
            logger.error("method invoke error", e);
        }
    }

    private Object[] getRealParamValue(Map<String, Object> requestParam, Method method, Channel channel, String targetUrl) {
        int count = method.getParameterCount();
        Parameter[] parameters = method.getParameters();
        ArrayList<Object> paramList = new ArrayList<>();
        Arrays.stream(parameters).forEach(param -> {
            String name = param.getName();
            Class<?> type = param.getType();
            if (type.isAssignableFrom(RequestInfo.class)) {
                RequestInfo requestInfo = new RequestInfo(channel, targetUrl);
                paramList.add(requestInfo);
            } else {
                paramList.add(requestParam.get(name));
            }
        });
        return paramList.toArray(new Object[count]);
    }
}
