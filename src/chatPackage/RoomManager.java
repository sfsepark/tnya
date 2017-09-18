package chatPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;

import javax.servlet.http.HttpSession;

import tpacePackage.LoginManager;
import tpacePackage.User;

public class RoomManager {
	
	private static RoomManager roomManager = null;
	private static Hashtable<String, Room> rooms = new Hashtable<String, Room>(); //streamer_name, room object
	
	private Connection chat_db_con = null;
	private PreparedStatement makeRoomPstmt = null;
	
	
	public RoomManager() throws Exception
	{		
		Class.forName("com.mysql.jdbc.Driver");
		chat_db_con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tpace",
				"tnya_admin",
				"1Two34567!");
		
		makeRoomPstmt = chat_db_con.prepareStatement("INSERT INTO room_table(streaer_id) VALUES (?)", new String[] {"room_id"});
	}
	
	
	public static synchronized RoomManager getInstance() throws Exception
	{
		if(roomManager == null)
		{
			roomManager = new RoomManager();
		}
		
		return roomManager;
	}
	
	public Room makeRoom(HttpSession session) throws Exception
	{
		LoginManager loginManager = LoginManager.getInstance();
		User user = loginManager.getUser(session);
		int streamer_id = user.getUser_id();
		
		if(user.isStreamer())
		{
			makeRoomPstmt.setInt(1, streamer_id);
			makeRoomPstmt.executeUpdate();
			ResultSet keySet = makeRoomPstmt.getGeneratedKeys();
			
			int room_id = keySet.getInt(1);
			Room room = new Room(room_id);
			
			rooms.put(user.getUser_name(), room); 
			
			return room;
		}
		else
		{
			return null;
		}
	}

	public void disableRoom(HttpSession session) throws Exception
	{
		LoginManager loginManager = LoginManager.getInstance();
		User user = loginManager.getUser(session);
		
		rooms.remove(user.getUser_name());
	}
	
	public Room getRoom(HttpSession session) throws Exception
	{
		LoginManager loginManager = LoginManager.getInstance();
		User user = loginManager.getUser(session);
		
		return rooms.get(user.getUser_name());
	}
	
	public Room getRoom(String streamer_name)
	{
		return rooms.get(streamer_name);
	}
}
