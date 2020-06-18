package com.yang.client;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import com.yang.entity.Customer;
import com.yang.factory.HibernateUtil;

/**
 * Hibernate�Ĳ�ѯ��ʽ��5�֣�1.OID��ѯ2.SQL3.HQL��ѯ4.QBC5.���󵼺���ѯ
 * Demo1ʹ��OID��ѯ��
 * ��hibernate��ÿһ���������һ��OID��
 * hibernate�а�ͬ���͵�OIDһ�µĶ���Ϊһ������
 * ��ͬһ��Session�У��������������ͬ���͵Ķ���OIDһ��
 * OID����ӳ�������ж�Ӧ���ݿ�����������
 * 
 * ������Ϊ��Ȼ�����ʹ���������
 * ��Ȼ�������Ѿ���ҵ������ֶ���Ϊ��������������ҵ���߼�
 * 		      �����ظ�������Ϊnull
 * 		      һ�����Ϊ�й��ɵģ��� ͨ����-��-��-ʱ��-��������Ϊ����
 * �����������Ѳ��߱�ҵ������ֶ���Ϊ����
 * 		      һ��Ϊȡ��ΪID�����ַ������ͽ�ʡ��������ݿ�ռ�		   
 * hibernate�е�����������6�֣�
 * 1.increment
 *   ��hibernate���Զ������ķ�ʽ����Ψһ�ı�ʶ��
 *   �������ڼ�Ⱥ�����������ڴ�������
 * 2.identity
 * 	 ʹ�����ݿ�ײ��ṩ���������ɱ�ʶ��
 *  ���������ݿ�֧���Զ�������������
 *  ��DB2,MYSQL,MS,SQL Server���ݿ��п���ʹ�ø�������
 *  Ҫ�����ݿ����������Ϊ�Զ��������ͣ������ڴ�������
 * 
 * 3.sequence
 *   ���ݵײ����ݿ��������ɱ�ʶ
 *   ���������ݿ�֧�����У������ڴ�������
 * 
 * 4.native
 *   ���ݵײ����ݿ���Զ����ɱ�ʶ����������ѡ��identity��sequence��hilo�����������е�һ�֣��ʺϿ����ݿ�ƽ̨����
 *   �����ڴ�������
 *   
 * 5.hilo
 *   �ߵ�λ�㷨
 *   ��ֵ*��max_lo+1��+������max_lo��ֵ
 *   
 * 6.uuid
 * 	 ����128λ��16�ֽڣ���UUID�㷨�����ɱ�ʶ��
 *   �ܹ������绷��������Ψһ���ַ�����ʶ����
 *   ��UUID������Ϊһ������Ϊ32λ��ʮ�������ַ���
 *   �����ڴ�������
 * 
 * 7.assigned
 *   ��java���������ɱ�ʶ���������ָ��idԪ�ص�generator����
 *   Ĭ��ʹ�ø�����������������Ȼ����
 * 
 * ��Hibenate�ж����м���״̬��
 * 1.˲ʱ̬
 *   ����ձ���������û�к�Session������û��OID
 * 2.�־�̬
 *   ˲ʱ̬ͨ�����ֱ��淽��ת��Ϊ�־�̬����session�й�������OID
 * 3.�й�̬������״̬��
 *   ������session����Ĺ�����OID����Sessionû��ϵ
 * 4.ɾ��״̬
 *   ��OID����Session�й�ϵ��ͬʱ�Ѿ�������ɾ������
 *   ���������ݿ��аѼ�¼ɾ����������û���ύ����ʱ�����״̬����ɾ��״̬
 *  
 * @author yang
 *
 */
public class HibernateDemo1 {
	@Test
	public void save() {
		Customer cust = new Customer();
		cust.setCustName("�����");
		cust.setCustAddress("����ɽ");
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();
		session.save(cust);
		tx.commit();
		session.close();
	}
	
	@Test
	public void update() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();
		Customer cust = session.get(Customer.class, 1L);
		cust.setCustName("��ɮ");
		session.update(cust);
		tx.commit();
		session.close();
	}
	
	@Test
	public void findOne() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();
		Customer cust = session.find(Customer.class, 1L);
		tx.commit();
		System.out.println(cust);
	}
	@Test
	public void deleteOne() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();
		Customer cust = session.get(Customer.class, 1L);
		session.delete(cust);
		tx.commit();
		session.close();
	}
	
	
	
}
