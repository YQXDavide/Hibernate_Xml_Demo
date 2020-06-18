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
	 * 获取一个新的Session对象
	 * 使用openSession得到的都是新的Session对象
	 * @return
	 */
	public static Session openSession() {
		return factory.openSession();
	}
	
	/**获取当前线程的Session对象
	 * 但是必须在配置文件中配置Session对象和线程绑定才行，不然返回null
	 * @return
	 */
	public static Session getCurrentSession() {
		return factory.getCurrentSession();
	}
}
