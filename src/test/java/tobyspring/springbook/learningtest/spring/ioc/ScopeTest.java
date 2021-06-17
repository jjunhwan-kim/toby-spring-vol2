package tobyspring.springbook.learningtest.spring.ioc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ScopeTest {
    @Test
    void singletonScope() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class, SingletonClientBean.class);

        Set<SingletonBean> bean = new HashSet<SingletonBean>();
        bean.add(ac.getBean(SingletonBean.class));
        bean.add(ac.getBean(SingletonBean.class));
        assertThat(bean.size()).isEqualTo(1);

        bean.add(ac.getBean(SingletonClientBean.class).bean1);
        bean.add(ac.getBean(SingletonClientBean.class).bean2);
        assertThat(bean.size()).isEqualTo(1);
    }

    static class SingletonBean {
    }

    static class SingletonClientBean {
        @Autowired SingletonBean bean1;
        @Autowired SingletonBean bean2;
    }

    @Test
    void prototypeScope() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, PrototypeClientBean.class);

        Set<PrototypeBean> bean = new HashSet<PrototypeBean>();
        bean.add(ac.getBean(PrototypeBean.class));
        assertThat(bean.size()).isEqualTo(1);

        bean.add(ac.getBean(PrototypeBean.class));
        assertThat(bean.size()).isEqualTo(2);

        bean.add(ac.getBean(PrototypeClientBean.class).bean1);
        assertThat(bean.size()).isEqualTo(3);

        bean.add(ac.getBean(PrototypeClientBean.class).bean2);
        assertThat(bean.size()).isEqualTo(4);
    }

    @Component("prototypeBean")
    @Scope("prototype")
    static class PrototypeBean {}

    static class PrototypeClientBean {
        @Autowired	PrototypeBean bean1;
        @Autowired	PrototypeBean bean2;
    }

    @Test
    public void objectFactory() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, ObjectFactoryConfig.class);
        ObjectFactory<PrototypeBean> factoryBeanFactory = ac.getBean("prototypeBeanFactory", ObjectFactory.class);

        Set<PrototypeBean> bean = new HashSet<PrototypeBean>();
        for (int i = 1; i <= 4; i++) {
            bean.add(factoryBeanFactory.getObject());
            assertThat(bean.size()).isEqualTo(i);
        }
    }

    @Configuration
    static class ObjectFactoryConfig {
        @Bean
        public ObjectFactoryCreatingFactoryBean prototypeBeanFactory() {
            ObjectFactoryCreatingFactoryBean factoryBean = new ObjectFactoryCreatingFactoryBean();
            factoryBean.setTargetBeanName("prototypeBean");
            return factoryBean;
        }
    }
}
