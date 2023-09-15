package com.concerto.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class MyAspect {
	
	
	@Before("execution(* com.concerto.aop.service.PaymentServiceImpl.*)")
	public void printBefore() {
		System.out.println("Payment Started////");
	}
	

}
