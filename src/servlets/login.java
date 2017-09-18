package servlets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import tpacePackage.LoginManager;
import tpacePackage.SystemMethod;
import tpacePackage.User;


/**
 * Servlet implementation class login_ser
 */
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
		
	private String homeURL = "http://tnya.kr:8080";
	private String authLoginServletPath = "/login/auth";
	private String clientID = "";
	private String clientSecret = "";
	
	private static String clientIDforUser = "y27dc0jsftjxir5i9a6gkhu3s4ekv8";
	private static String clientSecretforUser = "cf0lxhe1ukhr9o284hhu7mz3dn78nn";

	private static String clientIDforStreamer = "bcm76sjzcpv8skpy6gl1w3c3oqurxi";
	private static String clientSecretforStreamer = "m951700rov0dr1nfeh0jpzbhwau4co";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login() {
        super();
        // TODO Auto-generated constructor stub

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//한글 처리
		response.setContentType("text/plain;charset=UTF-8");
		
		String pathinfo = request.getPathInfo();
		String loginMode = pathinfo.substring(1);
	
		
		if(loginMode.equals("streamer") || loginMode.equals("user"))
		{
			this.getAuthCode(request, response, loginMode);
		}
		else
		{
			String scope = request.getParameter("scope");
			
			if(scope.contains("chat_login"))
			{
				clientID = login.clientIDforStreamer;
				clientSecret = login.clientSecretforStreamer;
			}
			else
			{
				clientID = login.clientIDforUser;
				clientSecret = login.clientSecretforUser;
			}
			
			User user = this.getUserFromTwitch(request, response);
			
			if(user != null)
			{
				this.loginToDB(request, response, user);
			}
			else
			{
				SystemMethod.popupAndRedirect(response, "올바른 유저가 아닙니다.",homeURL);
			}
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	//-------------------------------------------------------------------------------------------
	
	protected void getAuthCode(HttpServletRequest request, HttpServletResponse response, String loginMode)
	{		
		
		if(loginMode.equals("streamer"))
		{
			clientID = login.clientIDforStreamer;
			clientSecret = login.clientSecretforStreamer;
		}
		else
		{
			clientID = login.clientIDforUser;
			clientSecret = login.clientSecretforUser;
		}
		
		String urlStr = "https://api.twitch.tv/kraken/oauth2/authorize" + 
				"?response_type=code&client_id=" + clientID  +
				"&redirect_uri=" + homeURL + authLoginServletPath + "&scope=user_read";	

		if(loginMode.equals("streamer"))
		{
			urlStr += "+chat_login";
		}
		
		try
		{		
			response.sendRedirect(urlStr);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	protected String getAuthToken(String code) throws Exception
	{
		URL url = new URL("https://api.twitch.tv/kraken/oauth2/token" +
				"?client_id=" + clientID + 
				"&&client_secret=" + clientSecret + 
				"&code=" + code +
	    		"&grant_type=authorization_code" +
				"&redirect_uri=" + homeURL + authLoginServletPath)	;
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.connect();
		
		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		
		JSONParser jsonParser = new JSONParser();
		JSONObject obj = (JSONObject) jsonParser.parse(rd.readLine());
		String token = (String) obj.get("access_token");
		
		connection.disconnect();
		
		return token;
	}
	
	protected JSONObject getUserJSONData(String token) throws Exception
	{
		URL url = new URL("https://api.twitch.tv/kraken/user")	;
		
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/vnd.twitchtv.v5+json");
		connection.setRequestProperty("Client-ID", clientID);
		connection.setRequestProperty("Authorization", "OAuth " + token);
		connection.connect();
		
		int res_code = connection.getResponseCode();
		
		if(res_code == 401)
		{
			connection.disconnect();
			return null;
		}
		else
		{

			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	        
			JSONParser jsonParser = new JSONParser();
			JSONObject obj = (JSONObject) jsonParser.parse(rd.readLine());	
			connection.disconnect();
			
			return obj;
		}
	}
	
	protected User getUserFromTwitch(HttpServletRequest request, HttpServletResponse response) 
	{
		String code = request.getParameter("code");
		String scope = request.getParameter("scope");
		
		if(code == null || code == "")
		{
			SystemMethod.popupAndRedirect(response, "잘 못된 접근입니다.",homeURL);
		}
		else
		{	
			String token = "";
			JSONObject userJSONData = null;
			
			try
			{
				//2단계 인증 - 인증코드로 토큰 받아오기
				token = getAuthToken(request.getParameter("code"));
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				SystemMethod.popupAndRedirect(response, "인증 과정에서 문제가 발생했습니다. 관리자에게 문의하십시오(주소 갱신)", homeURL);
			}
				
			try
			{	
				//3단계 인증 - 토큰으로 유저 이름 가져오기	
				userJSONData = getUserJSONData(token);
				if(userJSONData == null)
				{
					SystemMethod.popupAndRedirect(response, "인증 과정에서 문제가 발생했습니다. 관리자에게 문의하십시오(401)", homeURL);
				}
				else
				{
					User user = new User(userJSONData);
					if(scope.contains("chat_login"))
					{
						user.setStreamer_token(token);
					}
					
					return user;
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				SystemMethod.popupAndRedirect(response, "인증 과정에 문제가 발생했습니다. 관리자에게 연락하십시오(client 갱신).", homeURL);
			}

		}
		
		return null;
	}
	

	//DB에서 회원 확인 후 로그인
	protected void loginToDB(HttpServletRequest request, HttpServletResponse response, User user)
	{
		try
		{					
			LoginManager loginManager = LoginManager.getInstance();
			String msg = loginManager.login(request.getSession(), user);
			SystemMethod.popupAndRedirect(response, msg ,homeURL);
		
		} catch (Exception e) {
			SystemMethod.popupAndRedirect(response, "유저 DB 접근에 실패 하였습니다",homeURL);
		}		
	}
	
}
 