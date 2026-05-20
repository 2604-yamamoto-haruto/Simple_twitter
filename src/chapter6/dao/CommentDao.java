package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.Comment;
import chapter6.beans.CommentUser;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class CommentDao {
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public CommentDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}


	public void insert(Connection connection, Comment comment) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO comments ( ");
			sql.append("    text, ");
			sql.append("    user_id, ");
			sql.append("    message_id, ");
			sql.append("    created_date, ");
			sql.append("    updated_date ");
			sql.append(") VALUES ( ");
			sql.append("    ?, ");                  // user_id
			sql.append("    ?, ");                  // text
			sql.append("    ?, ");
			sql.append("    CURRENT_TIMESTAMP, ");  // created_date
			sql.append("    CURRENT_TIMESTAMP ");   // updated_date
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, comment.getText());
			ps.setInt(2, comment.getUserId());
			ps.setInt(3, comment.getMessageId());

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public List<CommentUser> select(Connection connection, int num) {

		  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	        PreparedStatement ps = null;
	        try {
	            StringBuilder sql = new StringBuilder();
	            sql.append("SELECT ");
	            sql.append("    comments.id as id, ");
	            sql.append("    comments.text as text, ");
	            sql.append("    comments.user_id as user_id, ");
	            sql.append("    comments.message_id as message_id, ");
	            sql.append("    users.account as account, ");
	            sql.append("    users.name as name, ");
	            sql.append("    comments.created_date as created_date ");
	            sql.append("FROM comments ");
	            sql.append("INNER JOIN users ");
	            sql.append("ON comments.user_id = users.id ");
	            sql.append("ORDER BY created_date ASC limit " + num);

	            ps = connection.prepareStatement(sql.toString());

	            ResultSet rs = ps.executeQuery();

	            List<CommentUser> comments = toComenntUsers(rs);
	            return comments;
	        } catch (SQLException e) {
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
	            throw new SQLRuntimeException(e);
	        } finally {
	            close(ps);
	        }
	    }

	    private List<CommentUser> toComenntUsers(ResultSet rs) throws SQLException {


		  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	        List<CommentUser> commentUsers = new ArrayList<CommentUser>();
	        try {
	            while (rs.next()) {
	                CommentUser commentUser = new CommentUser();
	                commentUser.setId(rs.getInt("id"));
	                commentUser.setText(rs.getString("text"));
	                commentUser.setUserId(rs.getInt("user_id"));
	                commentUser.setMessageId(rs.getInt("message_id"));
	                commentUser.setAccount(rs.getString("account"));
	                commentUser.setName(rs.getString("name"));
	                commentUser.setCreatedDate(rs.getTimestamp("created_date"));

	                commentUsers.add(commentUser);
	            }
	            return commentUsers;
	        } finally {
	            close(rs);
	        }
	    }
}
