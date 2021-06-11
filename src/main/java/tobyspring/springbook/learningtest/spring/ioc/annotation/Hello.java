package tobyspring.springbook.learningtest.spring.ioc.annotation;

import javax.annotation.Resource;

public class Hello {
    String name;
    Printer printer;

    public Hello() {
    }

    public Hello(Printer printer) {
        this.printer = printer;
    }

    public Hello(String name, Printer printer) {
        this.name = name;
        this.printer = printer;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Resource
    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public String sayHello() {
        return "Hello " + name;
    }

    public void print() {
        this.printer.print(sayHello());
    }
}
