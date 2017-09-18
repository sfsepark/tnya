package tpacePackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {
	private static UserManager userManager = null;

	private Connection tpace_db_con = null;
	private PreparedStatement get_user_pstmt = null;
	
	public UserManager() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		tpace_db_con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tpace",
				"tnya_admin",
				"1Two34567!");
		get_user_pstmt = tpace_db_con.prepareStatement("SELECT user_id, display_name, logo, is_streamer FROM user_table WHERE user_name = ?");
	}
	
	public static synchronized UserManager getInstance() throws ClassNotFoundException, SQLException
	{
		if(userManager == null)
		{
			userManager = new UserManager();
		}
		
		return userManager;
	}
	
	public User getUser(String user_name) throws SQLException
	{
		get_user_pstmt.setString(1, user_name);
		get_user_pstmt.executeQuery();
		
		ResultSet rs = get_user_pstmt.getResultSet();
		if(rs.next())
		{
			int user_id = rs.getInt(1);
			String display_name = rs.getString(2);
			String logo = rs.getString(3);
			
			User user = new User(user_id, user_name, display_name, logo);
			
			return user;
		}
		else
			return null;
	}
	
	
	public User getStreamer(String user_name) throws SQLException
	{
		get_user_pstmt.setString(1, user_name);
		get_user_pstmt.executeQuery();
		
		ResultSet rs = get_user_pstmt.getResultSet();
		if(rs.next())
		{
			if(rs.getInt(4) == 0)
			{
				return null;
			}
			int user_id = rs.getInt(1);
			String display_name = rs.getString(2);
			String logo = rs.getString(3);
			
			User user = new User(user_id, user_name, display_name, logo);
			
			return user;
		}
		else
			return null;
	}
}
