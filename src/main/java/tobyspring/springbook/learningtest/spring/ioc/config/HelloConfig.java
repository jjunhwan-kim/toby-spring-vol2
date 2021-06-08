package tobyspring.springbook.learningtest.spring.ioc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tobyspring.springbook.learningtest.spring.ioc.bean.Hello;
import tobyspring.springbook.learningtest.spring.ioc.bean.Printer;
import tobyspring.springbook.learningtest.spring.ioc.bean.StringPrinter;

@Configuration
public class HelloConfig {
    @Bean
    public Hello hello() {
        Hello hello = new Hello();
        hello.setName("Spring");
        hello.setPrinter(printer());
        return hello;
    }

    @Bean
    public Hello hello2() {
        Hello hello = new Hello();
        hello.setName("Spring2");
        hello.setPrinter(printer());
        return hello;
    }

    @Bean
    private Printer printer() {
        return new StringPrinter();
    }
}
