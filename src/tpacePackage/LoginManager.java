package tpacePackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class LoginManager implements HttpSessionBindingListener {

	/*login manager는 두 가지 방식으로 보안을 유지하고있다.
	 * 
	 * 1) 최초 로그인 시 loginUser 해쉬 테이블에 (session, user_id) 를 넣음으로서 중복 로그인 방지 (isUsing 메서드)
	 * 2) session 의 auth 속성으로 auth_code(일회용 UUID) 를 넣고 이를 각 유저에 해당하는 오브젝트에 등록하여 이용할 때마다 확인
	 */
	
	/* login 순서
	 * isValid 체크 - > isUsing 체크 -> setSessionUserID 호출
	 * 해당 메서드에서 세션에 this(login_manager 인스턴스) 를 넣으면 이벤트로 valueBound 메서드 호출
	 */
	
	private Connection tket_db_con = null;
	private PreparedStatement user_search_pstmt = null;
	private PreparedStatement user_insert_pstmt = null;
	private PreparedStatement streamer_insert_pstmt = null;
	private PreparedStatement streamer_update_pstmt = null;
	
	public String test = "";
	
	private static LoginManager loginManager = null;
	
	private static Hashtable<HttpSession, String> loginSessions = new Hashtable<HttpSession, String>();
	private static Hashtable<String, User> userTable = new Hashtable<String,User>();
	
	public LoginManager() throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		tket_db_con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tpace",
				"tnya_admin",
				"1Two34567!");

		user_search_pstmt = tket_db_con.prepareStatement("select user_id from user_table where user_id = ?;");
		
		user_insert_pstmt = tket_db_con.prepareStatement("INSERT into user_table(user_id,user_name, display_name, logo) values(?,?,?,?);");		
		streamer_insert_pstmt = tket_db_con.prepareStatement("INSERT INTO user_table(user_id,user_name, display_name, logo, is_streamer) VALUES(?,?,?,?,1);");
		streamer_update_pstmt = tket_db_con.prepareStatement("UPDATE user_table SET is_streamer = 1 WHERE user_id = ?");
	}
	
	public static synchronized LoginManager getInstance() throws Exception {
		if(loginManager == null)
		{
			loginManager = new LoginManager();
		}
		return loginManager;
	}
	
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		loginSessions.put(event.getSession(),event.getName());
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		
		String user_id_str = loginSessions.remove(event.getSession());
		userTable.remove(user_id_str);
	}
	
	//세션에 UUID 로 세션을 바운드 시키면 valueBound 메서드가 실행됨.
	public void setSessionUserID(HttpSession session, int user_id) throws SQLException
	{
		session.setAttribute(String.format("%d", user_id),this);
	}
	
	
	public boolean isValid(int user_id) throws SQLException
	{
		user_search_pstmt.setInt(1,user_id);
		ResultSet rs =  user_search_pstmt.executeQuery();
	
		boolean valid = rs.next();
		
		rs.close();
		
		return valid;
	}
	
	public boolean isUsing(int user_id)
	{
		return loginSessions.containsValue(String.format("%d", user_id));
	}
	
	public boolean isLogined(HttpSession session)
	{
		return loginSessions.containsKey(session);
	}
	
	//--------------------- 로그인 관련 메서드들 -------------------------------
	/*
	 * 로그인 알고리즘
	 * 1. DB에 엑세스하여 정보 삽입
	 * - 가입이 안 되어 있다면  :             new_streamer, new_user
	 * - 가입이 되어있고 사용하지 않는 아이디라면 :  streamer_login, user_login
	 * 2. 세션에 user_id 속성에 this(리스너)를 바운드 시킴
	 * -> valueBound 메서드가 호출됨 -> loginSessions 테이블에 key는 세션, value는 이름 저장
	 * 3. UUID 값 생성 후 유저 오브젝트에 등록
	 * 4. userTable에 key를 user_id 로 user 등록
	 */
	
	public String login(HttpSession session, User user) throws Exception
	{
		int user_id = user.getUser_id();
		String msg = "";
		
		//회원 가입
		if(loginManager.isValid(user_id) == false) 
		{
			if(user.isStreamer())
				loginManager.new_streamer(session,user);
			else
				loginManager.new_user(session, user);
		
			msg =  "회원 가입에 성공했습니다." ;
		}
		else
		{
			//로그인
			if(loginManager.isUsing(user_id) == false)
			{	
				if(user.isStreamer())
					loginManager.streamer_login(session, user);
				else
					loginManager.user_login(session, user);
				
				msg = "로그인에 성공하였습니다.";
			}
			else
			{
				return "이미 로그인 중입니다";
			}
		}
		this.setSessionUserID(session, user_id);
		userTable.put(String.format("%d", user_id), user);
		
		session.setAttribute("display_name", user.getUser_display_name());
		
		return msg;
	}
	
	public void new_user(HttpSession session, User user) throws SQLException
	{
		user_insert_pstmt.setInt(1, user.getUser_id());
		user_insert_pstmt.setString(2, user.getUser_name());
		user_insert_pstmt.setString(3, user.getUser_display_name());
		if(user.getUser_logo() == null)
			user_insert_pstmt.setString(4, "");
		user_insert_pstmt.setString(4, user.getUser_logo());
		user_insert_pstmt.executeUpdate();
	}

	public void new_streamer(HttpSession session, User user) throws SQLException
	{
		streamer_insert_pstmt.setInt(1, user.getUser_id());
		streamer_insert_pstmt.setString(2, user.getUser_name());
		streamer_insert_pstmt.setString(3, user.getUser_display_name());
		if(user.getUser_logo() == null)
			streamer_insert_pstmt.setString(4, "");
		streamer_insert_pstmt.setString(4, user.getUser_logo());
		streamer_insert_pstmt.executeUpdate();
	}
	
	public void user_login(HttpSession session, User user) throws SQLException
	{
		//no-op
	}

	public void streamer_login(HttpSession session, User user) throws SQLException
	{
		streamer_update_pstmt.setInt(1, user.getUser_id());
		streamer_update_pstmt.executeUpdate();
	}
	
	//------------------- 로그아웃 관련 메서드들  ------------------------------
	
	public void removeSession(int user_id)
	{
		Enumeration<HttpSession> e = loginSessions.keys();
		HttpSession session = null;
		String user_id_str = String.format("%d", user_id);
		while(e.hasMoreElements())
		{
			session = e.nextElement();
			if(loginSessions.get(session).equals(user_id_str))
			{
				session.invalidate();
				break;
			}
		}
	}
	//--------------------------------------------------------------
	/*
	 *  유저에 엑세스 할 때는 반드시 UUID 체크를 해야한다
	 *  유저 엑세스 방식 : 
	 *  session -- loginSessions --> user_id
	 *  user_id -- userTable --> user
	 *  
	 *  세션의 auth 체크 후 
	 */
	
	public User getUser(HttpSession session)
	{
		String user_id_str = loginSessions.get(session);
		User user = userTable.get(user_id_str);

		return user;
	}
	
	public String getUserList()
	{	
		return userTable.toString();
	}
	public String getSessionList()
	{
		return loginSessions.toString();
	}

}
