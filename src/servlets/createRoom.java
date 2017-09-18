package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chatPackage.RoomManager;
import tpacePackage.LoginManager;

/**
 * Servlet implementation class createRoom
 */
@WebServlet("/createroom")
public class createRoom extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public createRoom() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("text/html");  
		
		String pathinfo = request.getPathInfo();
		String pathinfoClone = new String(pathinfo);
		String streamerName = pathinfoClone.substring(1);
		
		try {
			LoginManager loginManager = LoginManager.getInstance();
			
			if((loginManager.getUser(request.getSession()).getUser_name()).equals(streamerName))
			{
				RoomManager roomManager = RoomManager.getInstance();
				
				roomManager.makeRoom(request.getSession());
			}
			else
			{
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
