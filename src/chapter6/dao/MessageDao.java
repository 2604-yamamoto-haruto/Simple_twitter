package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.Message;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class MessageDao {


	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public MessageDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}


	public void insert(Connection connection, Message message) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO messages ( ");
			sql.append("    user_id, ");
			sql.append("    text, ");
			sql.append("    created_date, ");
			sql.append("    updated_date ");
			sql.append(") VALUES ( ");
			sql.append("    ?, ");                  // user_id
			sql.append("    ?, ");                  // text
			sql.append("    CURRENT_TIMESTAMP, ");  // created_date
			sql.append("    CURRENT_TIMESTAMP ");   // updated_date
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, message.getUserId());
			ps.setString(2, message.getText());

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public void deleteMessage(Connection connection, String DeleteId) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" DELETE FROM messages WHERE ");
			sql.append(" id = ?;");

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, DeleteId);

			ps.executeUpdate();
			connection.commit();
		}catch (SQLException e) {
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public String Message(Connection connection, String EditId) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT text FROM messages WHERE ");
			sql.append(" id = ?;");

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, EditId);

			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getString("text");
		}catch (SQLException e) {
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public void editMessage(Connection connection, String EditId, String EditText) {
		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE messages SET ");
			sql.append(" text = ? ");
			sql.append("WHERE id = ?;");

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, EditText);
			ps.setString(2, EditId);

			ps.executeUpdate();
			connection.commit();
		}catch (SQLException e) {
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public String check(Connection connection, String EditId) {
		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT id FROM messages WHERE");
			sql.append(" id = ?");

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, EditId);

			ResultSet rs = ps.executeQuery();
			rs.next();
			String check = null;
			if(rs.getRow() != 0) {
				check = rs.getString(1);
			}
			return check;

		}catch (SQLException e) {
				log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
				throw new SQLRuntimeException(e);
			} finally {
				close(ps);
			}

	}
}