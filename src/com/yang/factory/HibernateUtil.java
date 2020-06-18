package com.yang.factory;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory factory;
	static {
		try {
			Configuration config = new Configuration();
			config.configure();
			factory = config.buildSessionFactory();
		} catch (HibernateException e) {
			throw new HibernateException(e.toString());
		}
	}
	
	/**
	 * ��ȡһ���µ�Session����
	 * ʹ��openSession�õ��Ķ����µ�Session����
	 * @return
	 */
	public static Session openSession() {
		return factory.openSession();
	}
	
	/**��ȡ��ǰ�̵߳�Session����
	 * ���Ǳ����������ļ�������Session������̰߳󶨲��У���Ȼ����null
	 * @return
	 */
	public static Session getCurrentSession() {
		return factory.getCurrentSession();
	}
}
