package serverEndpoints;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import chatPackage.Chat;
import chatPackage.ChatManager;
import chatPackage.Room;
import chatPackage.RoomManager;
import tpacePackage.LoginManager;
import tpacePackage.User;

@ServerEndpoint(value = "/room/{streamer_name}")
public class getAcheiveServerEndpoint {
	
	@OnOpen
	public void onOpen(Session session, @PathParam("streamer_name") String streamer_name, EndpointConfig config)
	{
		try
		{
			RoomManager roomManager = RoomManager.getInstance();
			ChatManager chatManager = ChatManager.getInstance();
			LoginManager loginManager = LoginManager.getInstance();
			
			Room room = roomManager.getRoom(streamer_name);
			
			if(room != null)
			{
				Set<Session> clients = room.getClients();
				clients.add(session);
				
				HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());	
				User user = loginManager.getUser(httpSession);
				
				ArrayList<Chat> chatList = room.getChatList();
				
				synchronized (clients) {
					
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
