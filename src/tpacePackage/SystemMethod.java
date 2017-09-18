package tpacePackage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class SystemMethod {
	
	public static String printStackTraceToString(Exception e)
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		  PrintStream printStream = new PrintStream(bout);
		e.printStackTrace(printStream);
		
		return bout.toString();
	}
	
	public static void popupAndRedirect(HttpServletResponse response, String str, String redircetURL)
	{
		PrintWriter out;
		
		try {
			
			out = response.getWriter();
			response.setContentType("text/html");  
			out = out.append("<script type=\"text/javascript\" charset=\"utf-8\">");  
			out = out.append("alert('" + str +"')" + ";window.location='" + redircetURL + "';");  
			out = out.append("</script>");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	public static boolean check_login(HttpSession session)
	{
		return session.getAttribute("auth") != null;
	}
}
