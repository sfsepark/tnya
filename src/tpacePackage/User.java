package tpacePackage;

import org.json.simple.JSONObject;

public class User {
	private JSONObject userJSONData;
	private int user_id;
	private String user_name;
	private String user_display_name;
	private String user_logo;
	
	private String streamer_token;
	

	public User(JSONObject userJSONData)
	{
		this.userJSONData = userJSONData;
		
		String user_id_str = (String) userJSONData.get("_id");
		this.user_id = Integer.parseInt(user_id_str);
		
		this.user_name = (String) userJSONData.get("name");
		this.user_display_name = (String) userJSONData.get("display_name");
		this.user_logo = (String) userJSONData.get("logo");
		
		this.streamer_token = "";
	}
	
	public User(int user_id, String user_name, String display_name, String logo)
	{
		this.user_id = user_id;
		this.user_name = user_name;
		this.user_display_name = display_name;
		this.user_logo = logo;
	}
	
	//getters(these elements only use getter)
	
	public int getUser_id() {
		return user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public String getUser_display_name() {
		return user_display_name;
	}


	public JSONObject getUserJSONData() {
		return userJSONData;
	}

	public String getUser_logo() {
		return user_logo;
	}
	
	//getters and setters



	public String getStreamer_token() {
		return streamer_token;
	}

	public void setStreamer_token(String streamer_token) {
		this.streamer_token = streamer_token;
	}
	
	//------------------------------------------------
	
	public boolean isStreamer()
	{
		return !(this.streamer_token.equals(""));
	}

}
