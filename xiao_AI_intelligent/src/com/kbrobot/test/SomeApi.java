package com.kbrobot.test;

import org.jeecgframework.core.util.PasswordUtil;

public class SomeApi {
	public void Test(){
		//MapUtils
		//MutablePair
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println(PasswordUtil.encrypt("admin", "462195349zxc", PasswordUtil.getStaticSalt()));
		//5e2b1a8da7705446
		//c44b01947c9e6e3f
	}
}
