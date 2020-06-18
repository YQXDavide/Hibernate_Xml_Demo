package com.yang.client;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;

import com.yang.entity.Customer;
import com.yang.factory.HibernateUtil;

/**
 * 使用QBC的方式进行查询
 * 通过Criteria对象来查询
 * session.createCriteria(clazz);
 * 参数为要查询的实体类
 * @author yang
 *
 */
public class HibernateQBCDemo {
	/**
	 * 基本查询，SELECT * 
	 */
	@Test
	public void testFind() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		Criteria c = s.createCriteria(Customer.class);
		List list = c.list();
		for(Object o : list) {
			System.out.println(o);
		}
		tx.commit();
		s.close();
	}
	/**
	 * QBC查询通过Criteria对象的add方法可以添加条件来进行条件查询
	 */
	@Test
	public void testCriteriaFindByCondition() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		Criteria c = s.createCriteria(Customer.class);
		//使用ADD方法添加条件
		c.add(Restrictions.eq("custLevel","级别3"));
		List list = c.list();
		for(Object o : list) {
			System.out.println(o);
		}
		tx.commit();
		s.close();
	}
	
	/**
	 * QBC进行排序查询
	 * 通过调用Criteria对象的addOrder方法来实现
	 */
	@Test
	public void testOrderBy(){
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		Criteria c = s.createCriteria(Customer.class);
		c.addOrder(Order.desc("custId"));
		List list = c.list();
		for(Object o :list) {
			System.out.println(o);
		}
		tx.commit();
		s.close();
	}
	
	/**
	 * QBC进行分页查询
	 * 通过方法Criteria.setFirstResult(int);
	 * Criteria.setMaxResult(int)
	 */
	@Test
	public void testLimit() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		Criteria c = s.createCriteria(Customer.class);
		c.setFirstResult(0);
		c.setMaxResults(4);
		List list = c.list();
		for(Object o : list) {
			System.out.println(o);
		}
		tx.commit();
		s.close();
	}
	/**
	 * 统计查询
	 */
	@Test
	public void testTY(){
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		Criteria c = s.createCriteria(Customer.class);
		//使用setProjection方法，使用聚合函数对custId进行统计
		c.setProjection(Projections.rowCount());
		c.setProjection(Projections.count("custId"));
		//与Query一样返回的数值是唯一的，可以使用uniqueResult(int num);方法来显示结果
		Long count = (Long) c.uniqueResult();
		System.out.println(count);
		tx.commit();
		s.close();
	}
}
