package tobyspring.springbook.learningtest.spring.ioc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tobyspring.springbook.learningtest.spring.ioc.bean.Hello;

import javax.annotation.Resource;
import javax.inject.Named;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/identifier.xml")
class BeanIdentifierTest {
    @Autowired
    Hello hello;
    @Resource
    Hello 하이;
    @Resource
    ApplicationContext ac;

    @Test
    void id() {
        assertThat(hello).isNotNull();
        assertThat(하이).isNotNull();
        assertThat(hello).isSameAs(하이);
        assertThat(hello).isSameAs(ac.getBean("1234"));
        assertThat(hello).isSameAs(ac.getBean("/hello"));
        assertThat(hello).isSameAs(ac.getBean("헬로우"));
        System.out.println("OK5");
    }

    @Component("하이")
    static class Hi {
    }

    @Component
    @Named("하우디")
    static class Howdy {
        @Resource
        Hi 하이;
    }

    @Configuration
    static class Config {
        @Bean(name={"울랄라", "흠흠"})
        Howdy lala(Hi 하이) {
            Howdy h = new Howdy();
            h.하이 = 하이;
            return h;
        }
    }

    @Test
    void hi() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Hi.class, Howdy.class, Config.class);
        Hi 하이 = ac.getBean("하이", Hi.class);
        assertThat(하이).isNotNull();

        Howdy h = ac.getBean("하우디", Howdy.class);
        assertThat(h.하이).isSameAs(하이);

        Howdy h2 = ac.getBean("울랄라", Howdy.class);
        assertThat(h2.하이).isSameAs(하이);

        Howdy h3 = ac.getBean("흠흠", Howdy.class);
        assertThat(h3.하이).isSameAs(하이);

        assertThat(h).isNotSameAs(h2);
        assertThat(h2).isSameAs(h3);
    }
}
