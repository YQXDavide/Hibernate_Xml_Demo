package com.yang.client;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import com.yang.entity.Customer;
import com.yang.factory.HibernateUtil;

/**
 * Hibernate的查询方式有5种：1.OID查询2.SQL3.HQL查询4.QBC5.对象导航查询
 * Demo1使用OID查询：
 * 在hibernate中每一个对象会有一个OID：
 * hibernate中把同类型的OID一致的对象看为一个对象
 * 在同一个Session中，不允许出现两个同类型的对象OID一致
 * OID就是映射配置中对应数据库主键的属性
 * 
 * 主键分为自然主键和代理主键：
 * 自然主键：把具有业务含义的字段作为主键，参与程序的业务逻辑
 * 		      不能重复，不能为null
 * 		      一般设计为有规律的，如 通过年-月-日-时间-地区等作为主键
 * 代理主键：把不具备业务含义的字段作为主键
 * 		      一般为取名为ID，比字符串类型节省更多的数据库空间		   
 * hibernate中的主键类型有6种：
 * 1.increment
 *   由hibernate以自动递增的方式生成唯一的标识符
 *   不能用于集群环境，适用于代理主键
 * 2.identity
 * 	 使用数据库底层提供的主键生成标识符
 *  条件是数据库支持自动增长数据类型
 *  在DB2,MYSQL,MS,SQL Server数据库中可以使用该生成器
 *  要求数据库把主键定义为自动增长类型，适用于代理主键
 * 
 * 3.sequence
 *   根据底层数据库序列生成标识
 *   条件是数据库支持序列，适用于代理主键
 * 
 * 4.native
 *   根据底层数据库对自动生成标识符的能力来选择identity，sequence，hilo三种生成器中的一种，适合跨数据库平台开发
 *   适用于代理主键
 *   
 * 5.hilo
 *   高低位算法
 *   高值*（max_lo+1）+不大于max_lo的值
 *   
 * 6.uuid
 * 	 采用128位（16字节）的UUID算法来生成标识符
 *   能够在网络环境中生成唯一的字符串标识符，
 *   其UUID被编码为一个长度为32位的十六进制字符串
 *   适用于代理主键
 * 
 * 7.assigned
 *   由java程序负责生成标识符，如果不指定id元素的generator属性
 *   默认使用该生成器，适用于自然主键
 * 
 * 在Hibenate中对象有几个状态：
 * 1.瞬时态
 *   对象刚被创建出来没有和Session关联，没有OID
 * 2.持久态
 *   瞬时态通过各种保存方法转化为持久态，和session有关联，有OID
 * 3.托管态（游离状态）
 *   脱离了session对象的管理，有OID，和Session没关系
 * 4.删除状态
 *   有OID，和Session有关系，同时已经调用了删除方法
 *   即将从数据库中把记录删除，但事务还没有提交，此时对象的状态就是删除状态
 *  
 * @author yang
 *
 */
public class HibernateDemo1 {
	@Test
	public void save() {
		Customer cust = new Customer();
		cust.setCustName("孙悟空");
		cust.setCustAddress("花果山");
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
		cust.setCustName("唐僧");
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
