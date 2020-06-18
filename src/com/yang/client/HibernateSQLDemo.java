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
 * 使用SQL查询方式进行查询
 * 有两种方法：1.通过SQLQuery对象来使用SQL语句来查询
 * 			2.通过session的doWork方法拿到Connection
 * 使用JDBC的API编程步骤：
 * 1.注册驱动
 * 2.获取连接
 * 3.获取预处理对象PreparedStatement并创建SQL
 * 4.获取结果集
 * 5.显示内容
 * 7.关闭资源
 * @author yang
 *
 */
public class HibernateSQLDemo {
	/**
	 * 使用SQLQuery对象来进行查询
	 */
	@Test
	public void testSQL() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();
		//通过该方法就能使用SQL语句来进行查询
		session.createSQLQuery("SELECT * FROM tbl_customer");
		
	}
	/**
	 * 通过doWork方法来直接获取Connection对象，即使用原始的JDBC API进行查询
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
	 * 使用doWork通过查询结果集进行更新
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
					rs.updateString(3, "介绍");
					rs.updateString(5,"级别2");
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
	 * 调用doWork方法执行SQL的UPDATE语句完成更新
	 */
	@Test
	public void testSQLupdate() {
		Session s = HibernateUtil.getCurrentSession();
		Transaction tx = s.beginTransaction();
		s.doWork(new Work() {

			@Override
			public void execute(Connection conn){
				try(PreparedStatement pstmt = conn.prepareStatement("UPDATE tbl_customer SET cust_level=? WHERE cust_name=?")) {
					pstmt.setString(1, "级别1");
					pstmt.setString(2, "孙悟空");
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
	 * 使用doWork方法通过查询结果集进行插入操作，hibernate的Transaction对象必须提交，才能完成插入操作
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
					rs.updateString(2, "白龙马");
					rs.updateString(3, "介绍");
					rs.updateString(4,"");
					rs.updateString(5,"级别3");
					rs.updateString(6, "白龙涧");
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
	 * 调用doWork方法使用INSERT语句添加数据
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
					pstmt.setString(1, "猪八戒");
					pstmt.setString(2, "介绍");
					pstmt.setString(3, "级别2");
					pstmt.setString(4, "高老庄");
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
	 * doWork进行删除操作
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
					pstmt.setString(1,"白龙马");
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
			//注册驱动
			Class.forName("com.mysql.cj.jdbc.Driver");
			//连接数据库
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/customer?serverTimezone=UTC&useSSL=false&useUnicode=true","root","admin12345");
			//获取预处理对象
			pstmt = conn.prepareStatement("SELECT * FROM tbl_customer",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			//获取结果集
			rs = pstmt.executeQuery();
			//更新数据
//			rs.absolute(3);
//			rs.updateString(5, "级别3");
//			rs.updateRow();
//			
			//添加一行数据
			rs.moveToInsertRow();
			rs.updateLong(1, 9);
			rs.updateString(2, "玉皇大帝");
			rs.updateString(3,"介绍");
			rs.updateString(5, "级别0");
			rs.updateString(6, "天宫");
			rs.updateString(7,"11111111111");
			rs.insertRow();
			rs.moveToCurrentRow();
			
			//使用ResultSetMetaData来处理ResultSet结果集中的相关信息
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
