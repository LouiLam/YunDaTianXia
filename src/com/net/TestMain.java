package com.net;

import java.net.MalformedURLException;

import com.caucho.hessian.client.HessianProxyFactory;

public class TestMain {
	public static void main(String[] args) {
		
		login();				
	    //lockManager();	
		//completeShipperData();
		//getShipperById();
		//resetPwd();
		//addManager();
		//updateManager();
	}
	
	/*
	 * 
	 */
	private static void regist() {
		try {                                             
			String url = "http://localhost:8088/ql/md/saapi";//;jsessionid="+sid;
			HessianProxyFactory factory = new HessianProxyFactory();			
			ShipperAccountApi hello = (ShipperAccountApi) factory.create(ShipperAccountApi.class, url);
			System.out.print(hello.regist("hyq9000","00012","123456"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	private static void updateManager() {
		try {                                             
			String url = "http://localhost:8088/ql/md/saapi";//jsessionid="+sid;
			HessianProxyFactory factory = new HessianProxyFactory();			
			ShipperAccountApi hello = (ShipperAccountApi) factory.create(ShipperAccountApi.class, url);
			System.out.print(hello.updateManager(3, "13378789090"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private static void addManager() {
		try {                                             
			String url = "http://localhost:8088/ql/md/saapi";//;jsessionid="+sid;
			HessianProxyFactory factory = new HessianProxyFactory();			
			ShipperAccountApi hello = (ShipperAccountApi) factory.create(ShipperAccountApi.class, url);
			System.out.print(hello.addManager(2, "hyq9001", "13356789898", "123456"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	private static void lockManager() {
		try {                                             
			String url = "http://localhost:8088/ql/md/saapi";//;jsessionid="+sid;
			HessianProxyFactory factory = new HessianProxyFactory();			
			ShipperAccountApi hello = (ShipperAccountApi) factory.create(ShipperAccountApi.class, url);
			System.out.print(hello.lockManager(3));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	private static void completeShipperData() {
		try {                                             
			String url = "http://localhost:8088/ql/md/saapi";
			HessianProxyFactory factory = new HessianProxyFactory();			
			ShipperAccountApi hello = (ShipperAccountApi) factory.create(ShipperAccountApi.class, url);
			String rs=hello.completeData(2, "hyq9000@qq.com", "湖南省长沙市岳麓区", "黄立行", "小小的般我,两头法治",(byte)1);
			System.out.println(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 */
	private static void getShipperById() {
		try {                                             
			String url = "http://localhost:8088/ql/md/saapi;jsessionid=4BD6314623CA1F6615C4A3A0A3665B03?getShipperById";
			HessianProxyFactory factory = new HessianProxyFactory();			
			ShipperAccountApi hello = (ShipperAccountApi) factory.create(ShipperAccountApi.class, url);
			String rs=hello.getShipper(2);
			System.out.println(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	private static void resetPwd() {
		try {                                             
			String url = "http://localhost:8088/ql/md/saapi";
			HessianProxyFactory factory = new HessianProxyFactory();			
			ShipperAccountApi hello = (ShipperAccountApi) factory.create(ShipperAccountApi.class, url);
			String rs=hello.resetPwd(3,"654321");
			System.out.println(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}		
		
	private static void login() {
		try {                                             
			String url = "http://localhost:8088/ql/md/saapi?login";
			HessianProxyFactory factory = new HessianProxyFactory();			
			ShipperAccountApi hello = (ShipperAccountApi) factory.create(ShipperAccountApi.class, url);
			String rs=hello.login("hyq9001", "654321",(byte)1);
			System.out.println(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
