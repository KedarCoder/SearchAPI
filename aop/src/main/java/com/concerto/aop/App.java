package com.concerto.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.concerto.aop.service.PaymentService;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        ApplicationContext contex = new ClassPathXmlApplicationContext("com/concerto/aop/config.xml");
        
        PaymentService paymentService =  contex.getBean("payment" , PaymentService.class);
        
        paymentService.makePayment();
    }
}
