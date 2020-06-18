package com.yang.client;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.yang.entity.Customer;
import com.yang.factory.HibernateUtil;

public class Client {
	public static void main(String[] args) {
		//hibernate�Ļ�������
		//1.���������ļ�����SessionFactory
		//2.����SessionFactory����Session
		//3.ͨ��Session��������
		//4.ִ�г־û�����
		//5.�ύ����
		//6.�ر�session
		Customer cust = new Customer();
		cust.setCustName("������");
		cust.setCustAddress("����");
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx =  session.beginTransaction();
		session.save(cust);
		tx.commit();
		session.close();
		
		
	}
}
