package app;

import app.jms.TestProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class Main extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

        //Requires ActiveMQ instance to be running

        TestProducer producer = new TestProducer();
        Thread producerThread = new Thread(producer);
        producerThread.start();
    }
}
