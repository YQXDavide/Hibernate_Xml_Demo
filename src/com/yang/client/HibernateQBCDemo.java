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
 * ʹ��QBC�ķ�ʽ���в�ѯ
 * ͨ��Criteria��������ѯ
 * session.createCriteria(clazz);
 * ����ΪҪ��ѯ��ʵ����
 * @author yang
 *
 */
public class HibernateQBCDemo {
	/**
	 * ������ѯ��SELECT * 
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
	 * QBC��ѯͨ��Criteria�����add���������������������������ѯ
	 */
	@Test
	public void testCriteriaFindByCondition() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		Criteria c = s.createCriteria(Customer.class);
		//ʹ��ADD�����������
		c.add(Restrictions.eq("custLevel","����3"));
		List list = c.list();
		for(Object o : list) {
			System.out.println(o);
		}
		tx.commit();
		s.close();
	}
	
	/**
	 * QBC���������ѯ
	 * ͨ������Criteria�����addOrder������ʵ��
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
	 * QBC���з�ҳ��ѯ
	 * ͨ������Criteria.setFirstResult(int);
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
	 * ͳ�Ʋ�ѯ
	 */
	@Test
	public void testTY(){
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		Criteria c = s.createCriteria(Customer.class);
		//ʹ��setProjection������ʹ�þۺϺ�����custId����ͳ��
		c.setProjection(Projections.rowCount());
		c.setProjection(Projections.count("custId"));
		//��Queryһ�����ص���ֵ��Ψһ�ģ�����ʹ��uniqueResult(int num);��������ʾ���
		Long count = (Long) c.uniqueResult();
		System.out.println(count);
		tx.commit();
		s.close();
	}
}
