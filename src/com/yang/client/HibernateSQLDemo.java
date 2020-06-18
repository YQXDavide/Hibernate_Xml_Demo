package com.yang.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.junit.Test;

import com.yang.factory.HibernateUtil;

/**
 * ʹ��SQL��ѯ��ʽ���в�ѯ
 * �����ַ�����1.ͨ��SQLQuery������ʹ��SQL�������ѯ
 * 			2.ͨ��session��doWork�����õ�Connection
 * ʹ��JDBC��API��̲��裺
 * 1.ע������
 * 2.��ȡ����
 * 3.��ȡԤ�������PreparedStatement������SQL
 * 4.��ȡ�����
 * 5.��ʾ����
 * 7.�ر���Դ
 * @author yang
 *
 */
public class HibernateSQLDemo {
	/**
	 * ʹ��SQLQuery���������в�ѯ
	 */
	@Test
	public void testSQL() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();
		//ͨ���÷�������ʹ��SQL��������в�ѯ
		session.createSQLQuery("SELECT * FROM tbl_customer");
		
	}
	/**
	 * ͨ��doWork������ֱ�ӻ�ȡConnection���󣬼�ʹ��ԭʼ��JDBC API���в�ѯ
	 */
	@Test
	public void testDoWorkfind() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		s.doWork(new Work() {

			@Override
			public void execute(Connection conn){
				try(PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tbl_customer", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
					ResultSet rs = pstmt.executeQuery()) {
					while(rs.next()) {
						
						System.out.println(rs.getLong(1)+"  "+rs.getString(2)+"  "+rs.getString(3)+"  "+rs.getString(4)+"  "
								+rs.getString(5)+"  "+rs.getString(6)+"  "+rs.getString(7));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}
			
		});
		tx.commit();
		s.close();
	}
	
	/**
	 * ʹ��doWorkͨ����ѯ��������и���
	 */
	@Test
	public void testdoWorkUpdate() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		s.doWork(new Work() {

			@Override
			public void execute(Connection conn){
				try(PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tbl_customer",ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
					ResultSet rs = pstmt.executeQuery()){
					rs.absolute(1);
					rs.updateLong(1, 2L);
					rs.updateString(3, "����");
					rs.updateString(5,"����2");
					rs.updateRow();
				} catch (SQLException e) {
					e.printStackTrace();
				}		
			}
			
		});
		tx.commit();
		s.close();
	}
	/**
	 * ����doWork����ִ��SQL��UPDATE�����ɸ���
	 */
	@Test
	public void testSQLupdate() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		s.doWork(new Work() {

			@Override
			public void execute(Connection conn){
				try(PreparedStatement pstmt = conn.prepareStatement("UPDATE tbl_customer SET cust_level=? WHERE cust_name=?")) {
					pstmt.setString(1, "����1");
					pstmt.setString(2, "�����");
					pstmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
		});
		tx.commit();
		s.close();
	}
	/**
	 * ʹ��doWork����ͨ����ѯ��������в��������hibernate��Transaction��������ύ��������ɲ������
	 */
	@Test
	public void testdoWorkInsert() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		s.doWork(new Work() {

			@Override
			public void execute(Connection conn){
				try(PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tbl_customer", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
					ResultSet rs = pstmt.executeQuery()) {
					rs.absolute(1);
					rs.moveToInsertRow();
					rs.updateLong(1, 1L);
					rs.updateString(2, "������");
					rs.updateString(3, "����");
					rs.updateString(4,"");
					rs.updateString(5,"����3");
					rs.updateString(6, "������");
					rs.updateString(7, "");
					rs.insertRow();
					rs.moveToCurrentRow();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
		});
		tx.commit();
		s.close();
	}
	/**
	 * ����doWork����ʹ��INSERT����������
	 */
	@Test
	public void testInsert() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		s.doWork(new Work() {

			@Override
			public void execute(Connection conn){
				try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO tbl_customer(cust_name,cust_source,cust_level,cust_address) "
						+ "VALUES(?,?,?,?)")){
					pstmt.setString(1, "��˽�");
					pstmt.setString(2, "����");
					pstmt.setString(3, "����2");
					pstmt.setString(4, "����ׯ");
					pstmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}			
			}
			
		});
		tx.commit();
		s.close();
	}
	/**
	 * doWork����ɾ������
	 */
	@Test
	public void testdoWorkDelete() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		s.doWork(new Work() {
			@Override
			public void execute(Connection conn){
				try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tbl_customer", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
					ResultSet rs = pstmt.executeQuery()){
					rs.absolute(2);
					rs.deleteRow();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}		
		});
		tx.commit();
		s.close();
	}
	@Test
	public void testdelete() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		s.doWork(new Work() {

			@Override
			public void execute(Connection conn){
				try(PreparedStatement pstmt = conn.prepareStatement("DELETE FROM tbl_customer WHERE cust_name=?")) {
					pstmt.setString(1,"������");
					pstmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
		});
		tx.commit();
		s.close();
	}
	@Test
	public void testDoWorkOrderBy() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		s.doWork(new Work() {

			@Override
			public void execute(Connection conn){
				try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tbl_customer ORDER BY DESC");
					 ResultSet rs = pstmt.executeQuery()){
					 
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
		});
		tx.commit();
		s.close();
	}
	@Test
	public void JDBCtest() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			//ע������
			Class.forName("com.mysql.cj.jdbc.Driver");
			//�������ݿ�
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/customer?serverTimezone=UTC&useSSL=false&useUnicode=true","root","admin12345");
			//��ȡԤ�������
			pstmt = conn.prepareStatement("SELECT * FROM tbl_customer",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			//��ȡ�����
			rs = pstmt.executeQuery();
			//��������
//			rs.absolute(3);
//			rs.updateString(5, "����3");
//			rs.updateRow();
//			
			//���һ������
			rs.moveToInsertRow();
			rs.updateLong(1, 9);
			rs.updateString(2, "��ʴ��");
			rs.updateString(3,"����");
			rs.updateString(5, "����0");
			rs.updateString(6, "�칬");
			rs.updateString(7,"11111111111");
			rs.insertRow();
			rs.moveToCurrentRow();
			
			//ʹ��ResultSetMetaData������ResultSet������е������Ϣ
//			ResultSetMetaData rsmd = pstmt.getMetaData();
//			int numberOfColumns = rsmd.getColumnCount();
//			for(int i=1;i<=numberOfColumns;i++) {
//				System.out.println(rsmd.getColumnLabel(i));
//				System.out.println(rsmd.getCatalogName(i));
//				
//			}
			
//			while(rs.next()) {
//				
//				System.out.println(rs.getObject(rs.getRow()));
//			}
		} catch ( ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
					if(pstmt!=null) {
						try {
							pstmt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}finally {
							if(conn!=null) {
								try {
									conn.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}
}
