package com.yang.client;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Test;

import com.yang.factory.HibernateUtil;

/**
 * Hibernate使用HQL查询方式进行查询
 * HQL语句是把查询的列名用实体类的属性名代替，查询的表明用实体类名代替
 * HQL是通过Query对象进行查询的，该对象通过方法createQuery(String HQLQuery)获取
 * SQL:SELECT * FROM tbl_customer
 * 	   SELECT cust_id FROM tbl_customer
 * HQL:SELECT * FROM Customer
 * 	   SELECT custId FROM Customer
 * @author yang
 *
 */
public class HibernateHQLDemo {
	/**
	 * 通过HQL语句进行查询
	 */
	@Test
	public void testFindOne() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		//获取Query对象
		Query q = s.createQuery("FROM Customer");
		//获取结果集
		List list = q.list();
		for(Object o: list) {
			System.out.println(o);
		}
		tx.commit();
		s.close();
	}
	
	/**
	 * 通过HQL进行条件查询
	 */
	@Test
	public void testFindByCondition() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		Query q = s.createQuery("FROM Customer WHERE custName LIKE :cust_Name");
		q.setParameter("cust_Name", "孙%");
		List list = q.list();
		for(Object o : list) {
			System.out.println(o);
		}
		tx.commit();
		s.close();
	}
	
	/**
	 * 使用HQL进行ORDER BY查询并且分页显示，每页显示3个数据
	 */
	@Test
	public void testOrderBy() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		Query q = s.createQuery("FROM Customer ORDER BY custId DESC");
		q.setFirstResult(0);
		q.setMaxResults(3);
		List list = q.list();
		for(Object o : list) {
			System.out.println(o);
		}
		tx.commit();
		s.close();
	}
	
	/**
	 * 使用聚合函数进行查询
	 * 获取返回结果：
	 * q.uniqueResult();//返回的结果唯一时，可以使用该方法，返回结果不唯一时会报错
	 */
	@Test
	public void testCount() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		Query q = s.createQuery("SELECT COUNT(*) FROM Customer");
		Long l = (Long) q.uniqueResult();//返回的结果唯一时，可以使用该方法。返回的结果不唯一时，使用该方法会报错
		System.out.println(l);
		tx.commit();
		s.close();
	}
	
	/**
	 * 根据条件进行查询返回的结果是一个个Object[]数组，一个Object[]数组对应一条记录
	 * 数组中的每一个元素对应查询的列信息
	 * 
	 * 投影查询：其实就是查询需要的列信息
	 * 它的返回结果使用实体类来封装，而不是使用Object[]
	 * 这就是创建实体类的投影
	 * 1.通过new关键字定位到具体的实体类中的被投影的属性列
	 * 2.在实体类中把需要进行投影查询的属性使用构造方法初始化
	 * 
	 * 普通做法使用HQL进行投影查询
	 * Query q = s.createQuery("SELECT custId,custName FROM Customer");
	 * List<Object[]> list = q.list();
	 * for(Object[] os: list){
	 *  System.out.println(-----投影的属性列数组-----);
	 * 	for(Object o:os){
	 * 		System.out.println(o);
	 * 	}
	 * }
	 */
	@Test
	public void testColumn() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		//使用普通方法进行条件查询
//		Query q = s.createQuery("SELECT custId,custName FROM Customer");
//		List<Object[]> list = q.list();
//		for(Object[] os: list) {
//			System.out.println("-----投影的属性列数组-----");
//			for(Object o : os) {
//				System.out.println(o);
//			}
//		}
		//使用投影查询，把需要查询的列信息对应的属性，在对应的实体类中使用构造函数初始化
		Query q = s.createQuery("SELECT new com.yang.entity.Customer(custId,custName) FROM Customer");
		List list = q.list();
		for(Object o : list) {
			System.out.println(o);
		}
		tx.commit();
		s.close();
	}
}	
