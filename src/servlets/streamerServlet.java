package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tpacePackage.LoginManager;
import tpacePackage.SystemMethod;
import tpacePackage.User;
import tpacePackage.UserManager;

/**
 * Servlet implementation class streamerServlet
 */
@WebServlet("/streamerServlet")
public class streamerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public streamerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		ServletContext context = this.getServletContext();
		
		response.setContentType("text/html");  
		
		String pathinfo = request.getPathInfo();
		String pathinfoClone = new String(pathinfo);
		String streamerName = pathinfoClone.substring(1);

		try {
			UserManager userManager = UserManager.getInstance();
			User streamer = userManager.getStreamer(streamerName);
			if(streamer == null)
			{
				response.getWriter().append("404" + pathinfoClone);
			}
			else
			{
				request.setAttribute("streamer_name", streamer.getUser_name());
				request.setAttribute("streamer_display_name", streamer.getUser_display_name());
				request.setAttribute("streamer_logo", streamer.getUser_logo());
				
				RequestDispatcher dispatcher = context.getRequestDispatcher("/streamer.jsp");
				dispatcher.forward(request, response);
			}
			
		} catch (Exception e) { 
			e.printStackTrace();
			response.getWriter().println(SystemMethod.printStackTraceToString(e));
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
