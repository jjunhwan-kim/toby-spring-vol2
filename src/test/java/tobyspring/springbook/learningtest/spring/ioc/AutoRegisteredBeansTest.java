package tobyspring.springbook.learningtest.spring.ioc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class AutoRegisteredBeansTest {
    @Test
    public void autoRegisteredBean() {
        System.getProperties().put("os.name", "Hi");

        ApplicationContext ac = new AnnotationConfigApplicationContext(SystemBean.class);
        SystemBean bean = ac.getBean(SystemBean.class);

        assertThat(bean.applicationContext).isSameAs(ac);
        assertThat(bean.beanFactory).isSameAs(bean.defaultListableBeanFactory);

        System.out.println(bean.beanFactory);
        System.out.println(bean.defaultListableBeanFactory);
        System.out.println(bean.applicationContext);

        System.out.println(bean.osname);
        System.out.println(bean.path);

        System.out.println("### " + bean.systemProperties);
        System.out.println("$$$ " + bean.systemEnvironment);

    }
    static class SystemBean {
        @Resource ApplicationContext applicationContext;
        @Autowired BeanFactory beanFactory;
        @Autowired DefaultListableBeanFactory defaultListableBeanFactory;
        @Autowired ResourceLoader resourceLoader;
        @Autowired ApplicationEventPublisher applicationEventPublisher;

        @Value("#{systemProperties['os.name']}") String osname;
        @Value("#{systemEnvironment['Path']}") String path;

        @Resource Properties systemProperties;
        @Resource Map systemEnvironment;
    }
}
