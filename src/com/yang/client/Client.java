package com.yang.client;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.yang.entity.Customer;
import com.yang.factory.HibernateUtil;

public class Client {
	public static void main(String[] args) {
		//hibernate的基本操作
		//1.根据配置文件创建SessionFactory
		//2.根据SessionFactory创建Session
		//3.通过Session开启事务
		//4.执行持久化操作
		//5.提交事务
		//6.关闭session
		Customer cust = new Customer();
		cust.setCustName("唐三藏");
		cust.setCustAddress("大唐");
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx =  session.beginTransaction();
		session.save(cust);
		tx.commit();
		session.close();
		
		
	}
}
