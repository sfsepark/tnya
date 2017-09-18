package chatPackage;


public class Chat {
	private int chat_id;
	private int chat_like = 0;
	private int chat_dislike = 0;
	private int user_id = 0;
	private String user_display_name;
	private String chat_data;
	
	public Chat(int chat_id)
	{
		this.chat_id = chat_id;
	}

	public int getChat_id() {
		return chat_id;
	}

	public void setChat_id(int chat_id) {
		this.chat_id = chat_id;
	}

	public int getChat_like() {
		return chat_like;
	}

	public void setChat_like(int chat_like) {
		this.chat_like = chat_like;
	}

	public int getChat_dislike() {
		return chat_dislike;
	}

	public void setChat_dislike(int chat_dislike) {
		this.chat_dislike = chat_dislike;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_display_name() {
		return user_display_name;
	}

	public void setUser_display_name(String user_display_name) {
		this.user_display_name = user_display_name;
	}

	public String getChat_data() {
		return chat_data;
	}

	public void setChat_data(String chat_data) {
		this.chat_data = chat_data;
	}
	
	
}
