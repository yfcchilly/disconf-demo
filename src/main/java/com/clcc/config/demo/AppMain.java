package com.clcc.config.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by fanchao on 2016/10/28.
 */
public class AppMain {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DisconfDemoTask disconfDemoTask = (DisconfDemoTask) context.getBean("disconfDemoTask");
        disconfDemoTask.run();
    }

}
