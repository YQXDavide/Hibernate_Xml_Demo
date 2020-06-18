package com.yang.client;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Test;

import com.yang.factory.HibernateUtil;

/**
 * Hibernateʹ��HQL��ѯ��ʽ���в�ѯ
 * HQL����ǰѲ�ѯ��������ʵ��������������棬��ѯ�ı�����ʵ����������
 * HQL��ͨ��Query������в�ѯ�ģ��ö���ͨ������createQuery(String HQLQuery)��ȡ
 * SQL:SELECT * FROM tbl_customer
 * 	   SELECT cust_id FROM tbl_customer
 * HQL:SELECT * FROM Customer
 * 	   SELECT custId FROM Customer
 * @author yang
 *
 */
public class HibernateHQLDemo {
	/**
	 * ͨ��HQL�����в�ѯ
	 */
	@Test
	public void testFindOne() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		//��ȡQuery����
		Query q = s.createQuery("FROM Customer");
		//��ȡ�����
		List list = q.list();
		for(Object o: list) {
			System.out.println(o);
		}
		tx.commit();
		s.close();
	}
	
	/**
	 * ͨ��HQL����������ѯ
	 */
	@Test
	public void testFindByCondition() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		Query q = s.createQuery("FROM Customer WHERE custName LIKE :cust_Name");
		q.setParameter("cust_Name", "��%");
		List list = q.list();
		for(Object o : list) {
			System.out.println(o);
		}
		tx.commit();
		s.close();
	}
	
	/**
	 * ʹ��HQL����ORDER BY��ѯ���ҷ�ҳ��ʾ��ÿҳ��ʾ3������
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
	 * ʹ�þۺϺ������в�ѯ
	 * ��ȡ���ؽ����
	 * q.uniqueResult();//���صĽ��Ψһʱ������ʹ�ø÷��������ؽ����Ψһʱ�ᱨ��
	 */
	@Test
	public void testCount() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		Query q = s.createQuery("SELECT COUNT(*) FROM Customer");
		Long l = (Long) q.uniqueResult();//���صĽ��Ψһʱ������ʹ�ø÷��������صĽ����Ψһʱ��ʹ�ø÷����ᱨ��
		System.out.println(l);
		tx.commit();
		s.close();
	}
	
	/**
	 * �����������в�ѯ���صĽ����һ����Object[]���飬һ��Object[]�����Ӧһ����¼
	 * �����е�ÿһ��Ԫ�ض�Ӧ��ѯ������Ϣ
	 * 
	 * ͶӰ��ѯ����ʵ���ǲ�ѯ��Ҫ������Ϣ
	 * ���ķ��ؽ��ʹ��ʵ��������װ��������ʹ��Object[]
	 * ����Ǵ���ʵ�����ͶӰ
	 * 1.ͨ��new�ؼ��ֶ�λ�������ʵ�����еı�ͶӰ��������
	 * 2.��ʵ�����а���Ҫ����ͶӰ��ѯ������ʹ�ù��췽����ʼ��
	 * 
	 * ��ͨ����ʹ��HQL����ͶӰ��ѯ
	 * Query q = s.createQuery("SELECT custId,custName FROM Customer");
	 * List<Object[]> list = q.list();
	 * for(Object[] os: list){
	 *  System.out.println(-----ͶӰ������������-----);
	 * 	for(Object o:os){
	 * 		System.out.println(o);
	 * 	}
	 * }
	 */
	@Test
	public void testColumn() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		//ʹ����ͨ��������������ѯ
//		Query q = s.createQuery("SELECT custId,custName FROM Customer");
//		List<Object[]> list = q.list();
//		for(Object[] os: list) {
//			System.out.println("-----ͶӰ������������-----");
//			for(Object o : os) {
//				System.out.println(o);
//			}
//		}
		//ʹ��ͶӰ��ѯ������Ҫ��ѯ������Ϣ��Ӧ�����ԣ��ڶ�Ӧ��ʵ������ʹ�ù��캯����ʼ��
		Query q = s.createQuery("SELECT new com.yang.entity.Customer(custId,custName) FROM Customer");
		List list = q.list();
		for(Object o : list) {
			System.out.println(o);
		}
		tx.commit();
		s.close();
	}
}	
