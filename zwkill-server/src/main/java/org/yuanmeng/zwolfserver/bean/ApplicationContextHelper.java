package org.yuanmeng.zwolfserver.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangtonghe
 * @since 2018/10/5 17:47
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static Class<?> getType(String beanName) {
        return applicationContext.getType(beanName);

    }

    public static String[] getAllBeanName() {
        return applicationContext.getBeanDefinitionNames();
    }

    public static String[] getBeans(Class<? extends Annotation> clazz) {
        String[] allBeans = applicationContext.getBeanDefinitionNames();
        List<String> resultList = new ArrayList<>();

        for (String bean : allBeans) {
            Class<?> beanClz = getType(bean);
            if (beanClz.isAnnotationPresent(clazz)) {
                resultList.add(bean);
            }
        }
        return resultList.toArray(new String[0]);
    }
}
