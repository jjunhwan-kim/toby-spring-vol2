package tobyspring.springbook.learningtest.spring.ioc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import tobyspring.springbook.learningtest.spring.ioc.annotation.Hello;

import javax.inject.Inject;
import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotationConfigurationTest {
    @Test
    void atResource() {
        ApplicationContext ac = new GenericXmlApplicationContext("/resource.xml");

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString()).isEqualTo("Hello Spring");
    }

    @Test
    void atAutowiredCollection() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(Client.class, ServiceA.class, ServiceB.class);
        Client client = ac.getBean(Client.class);
        assertThat(client.beanBArray.length).isEqualTo(2);
        assertThat(client.beanBSet.size()).isEqualTo(2);
        assertThat(client.beanBMap.entrySet().size()).isEqualTo(2);
        for (Map.Entry<String, Service> entry : client.beanBMap.entrySet())
        {
            System.out.println(entry.getKey() + ' ' + entry.getValue());
        }
        assertThat(client.beanBList.size()).isEqualTo(2);
        assertThat(client.beanBCollection.size()).isEqualTo(2);
    }

    // atAutowiredCollection test
    static class Client {
        @Autowired Set<Service> beanBSet;
        @Autowired Service[] beanBArray;
        @Autowired Map<String, Service> beanBMap;
        @Autowired List<Service> beanBList;
        @Autowired Collection<Service> beanBCollection;
    }

    interface Service {}
    static class ServiceA implements Service {}
    static class ServiceB implements Service {}

    @Test
    public void atInject() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(IClient.class, IServiceA.class, IServiceB.class);
        IClient iclient = ac.getBean(IClient.class);
        assertThat(iclient.service).isInstanceOf(IServiceA.class);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier    // DIJ qualifier
    @interface Main {}

    static class IClient {
        @Inject
        @Main Service service;
    }

    @Main
    static class IServiceA implements Service {}
    static class IServiceB implements Service {}
}
