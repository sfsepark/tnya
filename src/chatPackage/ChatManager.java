package chatPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpSession;

import tpacePackage.LoginManager;
import tpacePackage.User;

public class ChatManager {
	
	/*
	 * chat_table 
	 * - chat_id, room_id, user_id, chat_data, chat_like, chat_dislike, user_display_name
	 * room_table
	 * - room_id, streamer_id
	 */

	private static ChatManager chatManager = null;

	private Connection chat_db_con = null;
	private PreparedStatement getChatPstmt = null;
	private PreparedStatement newChatPstmt = null;
	
	public ChatManager() throws Exception
	{		
		Class.forName("com.mysql.jdbc.Driver");
		chat_db_con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tpace",
				"tnya_admin",
				"1Two34567!");
		
		getChatPstmt = chat_db_con.prepareStatement("SELECT (chat_id, user_display_name, chat_data, chat_like, chat_dislike) FROM chat_table WHERE room_id = ?;");
		newChatPstmt = chat_db_con.prepareStatement("INSERT INTO chat_table(user_display_name, user_id, chat_data) VALUES(?,?,?);", new String[] {"chat_id"});
	}
	
	public static synchronized ChatManager getInstance() throws Exception
	{
		if(chatManager == null)
		{
			chatManager = new ChatManager();
		}
		
		return chatManager;
	}
	
	public Chat newChat(String chat_data , int user_id, String user_display_name) throws Exception
	{
		newChatPstmt.setString(1, user_display_name);
		newChatPstmt.setInt(2, user_id);
		newChatPstmt.setString(3, chat_data);
		
		newChatPstmt.executeUpdate();
		
		ResultSet key = newChatPstmt.getGeneratedKeys();
		
		int chat_id = key.getInt(1);
		
		Chat chat = new Chat(chat_id);
		chat.setChat_data(chat_data);
		chat.setUser_display_name(user_display_name);
		
		return chat;
	}

}