package tobyspring.springbook.learningtest.spring.ioc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import tobyspring.springbook.learningtest.spring.ioc.bean.Hello;
import tobyspring.springbook.learningtest.spring.ioc.bean.Printer;
import tobyspring.springbook.learningtest.spring.ioc.bean.StringPrinter;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleAtBeanTest {
    @Test
    public void simpleAtBean() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(HelloService.class);
        Hello h1 = ac.getBean("hello", Hello.class);
        Hello h2 = ac.getBean("hello2", Hello.class);
        assertThat(h1.getPrinter()).isNotNull();
        assertThat(h1.getPrinter()).isSameAs(h2.getPrinter());

        HelloService hs = ac.getBean("simpleAtBeanTest.HelloService", HelloService.class);
        assertThat(hs).isNotNull();
    }

    static class HelloService {
        private Printer printer;

        @Autowired
        public void setPrinter(Printer printer) {
            this.printer = printer;
        }

        @Bean
        private Hello hello() {
            Hello hello = new Hello();
            hello.setName("Spring");
            hello.setPrinter(this.printer);
            return hello;
        }

        @Bean
        private Hello hello2() {
            Hello hello = new Hello();
            hello.setName("Spring2");
            hello.setPrinter(this.printer);
            return hello;
        }

        @Bean
        private Printer printer() {
            return new StringPrinter();
        }
    }
}
