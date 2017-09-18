package chatPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.websocket.Session;

import tpacePackage.User;

public class Room {

	private Set<Session> clients;
	private int room_id;
	
	private ArrayList<Chat> chat_list = new ArrayList<>();
	
	public Room(int room_id)
	{
		this.room_id = room_id;
		clients =  Collections.synchronizedSet(new HashSet<Session>());
	}
	
	public Set<Session> getClients()
	{
		return this.clients;
	}
	
	public int get_room_id()
	{
		return room_id;
	}
	
	public Chat add_chat(User user, String chatData) throws Exception
	{
		ChatManager chatManager = ChatManager.getInstance();
		
		Chat chat = chatManager.newChat(chatData, user.getUser_id(), user.getUser_display_name());
		chat_list.add(chat);
		
		return chat;
	}
	
	public ArrayList<Chat> getChatList()
	{
		return chat_list;
	}
}
