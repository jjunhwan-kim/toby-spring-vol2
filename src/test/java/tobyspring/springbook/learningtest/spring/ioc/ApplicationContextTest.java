package tobyspring.springbook.learningtest.spring.ioc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import tobyspring.springbook.learningtest.spring.ioc.bean.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextTest {

    @Test
    void registerBean() {
        StaticApplicationContext ac = new StaticApplicationContext();
        ac.registerSingleton("hello1", Hello.class);

        Hello hello1 = ac.getBean("hello1", Hello.class);
        assertThat(hello1).isNotNull();

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        ac.registerBeanDefinition("hello2", helloDef);

        Hello hello2 = ac.getBean("hello2", Hello.class);
        assertThat(hello2.sayHello()).isEqualTo("Hello Spring");

        assertThat(hello1).isNotSameAs(hello2);

        assertThat(ac.getBeanFactory().getBeanDefinitionCount()).isEqualTo(2);
    }

    @Test
    void registerBeanWithDependency() {
        StaticApplicationContext ac = new StaticApplicationContext();

        ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));

        ac.registerBeanDefinition("hello", helloDef);

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString()).isEqualTo("Hello Spring");
    }

    @Test
    void genericApplicationContext() {
        GenericApplicationContext ac = new GenericApplicationContext();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ac);
        reader.loadBeanDefinitions("/genericApplicationContext.xml");

        ac.refresh();

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString()).isEqualTo("Hello Spring");
    }

    @Test
    void genericXmlApplicationContext() {
        GenericApplicationContext ac = new GenericXmlApplicationContext("/genericApplicationContext.xml");

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString()).isEqualTo("Hello Spring");
    }

    @Test
    void createContextWithoutParent() {
        assertThrows(BeanCreationException.class, () -> new GenericXmlApplicationContext("/childContext.xml"));
    }

    @Test
    void contextHierachy() {
        ApplicationContext parent = new GenericXmlApplicationContext("/parentContext.xml");

        GenericApplicationContext child = new GenericApplicationContext(parent);
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
        reader.loadBeanDefinitions("/childContext.xml");
        child.refresh();

        Printer printer = child.getBean("printer", Printer.class);
        assertThat(printer).isNotNull();

        Hello hello = child.getBean("hello", Hello.class);
        assertThat(hello).isNotNull();

        hello.print();
        assertThat(printer.toString()).isEqualTo("Hello Child");
    }

    @Test
    public void simpleBeanScanning() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("tobyspring.springbook.learningtest.spring.ioc.bean");

        assertThrows(NoSuchBeanDefinitionException.class, () -> ctx.getBean("hello", Hello.class));

        AnnotatedHello hello = ctx.getBean("myAnnotatedHello", AnnotatedHello.class);
        assertThat(hello).isNotNull();
    }

    @Test
    public void filteredBeanScanning() {
        ApplicationContext ctx = new GenericXmlApplicationContext("/filteredScanningContext.xml");

        // 포인트컷 표현식으로 빈 자동등록시 @Component 애노테이션 필요 없음
        Hello hello = ctx.getBean("hello", Hello.class);
        assertThat(hello).isNotNull();

        AnnotatedHello annotatedHello = ctx.getBean("myAnnotatedHello", AnnotatedHello.class);
        assertThat(annotatedHello).isNotNull();
    }

    @Test
    public void configurationBean() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AnnotatedHelloConfig.class);

        AnnotatedHello hello = ctx.getBean("annotatedHello", AnnotatedHello.class);
        assertThat(hello).isNotNull();

        AnnotatedHelloConfig config = ctx.getBean("annotatedHelloConfig", AnnotatedHelloConfig.class);
        assertThat(config).isNotNull();

        assertThat(config.annotatedHello()).isSameAs(hello);
        assertThat(config.annotatedHello()).isSameAs(config.annotatedHello());

        System.out.println(ctx.getBean("systemProperties").getClass());
    }

    @Test
    void constructorArgName() {
        ApplicationContext ac = new GenericXmlApplicationContext("/constructorInjection.xml");

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString()).isEqualTo("Hello Spring");
    }

    @Test
    public void autowire() {
        ApplicationContext ac = new GenericXmlApplicationContext("/autowire.xml");

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString()).isEqualTo("Hello Spring");
    }
}
