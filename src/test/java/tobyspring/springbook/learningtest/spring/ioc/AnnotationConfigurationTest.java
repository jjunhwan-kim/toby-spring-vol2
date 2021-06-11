package tobyspring.springbook.learningtest.spring.ioc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import tobyspring.springbook.learningtest.spring.ioc.annotation.Hello;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotationConfigurationTest {
    @Test
    void atResource() {
        ApplicationContext ac = new GenericXmlApplicationContext("/resource.xml");

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString()).isEqualTo("Hello Spring");
    }
}
