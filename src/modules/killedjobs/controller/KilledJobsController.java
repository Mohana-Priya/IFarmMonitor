package modules.killedjobs.controller;

import java.io.IOException;

import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.Mailer;
import modules.killedjobs.dao.KilledJobsDao;
import modules.killedjobs.dao.KilledJobsDaoImpl;

public class KilledJobsController extends HttpServlet{	
	private static final long serialVersionUID = 5145442772987174426L;
	private KilledJobsDao killedJobsDao;
    
    public void init() {		
    	killedJobsDao = new KilledJobsDaoImpl();
	}
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		doGet(req, resp);
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException ,IOException{
    	System.out.println("KilledJobsController:---->doGet()");
		System.out.println("The current uri is:"+req.getRequestURI());
		
		if(req.getRequestURI().toString().equals("/IFarmMonitor/getKilledJobs.htm")){
			getKilledJobs(req,resp);
		}else if(req.getRequestURI().toString().equals("/IFarmMonitor/getRunningJobs.htm")){
			getRunningJobs(req,resp);
		}else if(req.getRequestURI().toString().equals("/IFarmMonitor/sendMail.htm")){
			sendMail(req,resp);
		}else if(req.getRequestURI().toString().equals("/IFarmMonitor/getJobOwner.htm")){
			getJobOwner(req,resp);
		}
    }    

	private void getKilledJobs(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("KilledJobsController:---->getKilledJobs()");
		
		try {						
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
	        resp.getWriter().write(killedJobsDao.getKilledJobs());
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
    
    private void getRunningJobs(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("KilledJobsController:---->getRunningJobs()");
		
		try {						
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
	        resp.getWriter().write(killedJobsDao.getRunningJobs());
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
    
    private void getJobOwner(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("KilledJobsController:---->getJobOwner()");
		
		try {						
			resp.setContentType("text/html");
			resp.setCharacterEncoding("UTF-8");
	        resp.getWriter().write(killedJobsDao.getJobOwner(req.getParameter("labelName")));
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
    
    private void sendMail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	System.out.println("KilledJobsController:---->sendMail()");
		
		try {						
			resp.setContentType("text/html");
			resp.setCharacterEncoding("UTF-8");
			Mailer.sendMail(req.getParameter("fromAddress"), req.getParameter("toAddress"), req.getParameter("subject"), req.getParameter("message"));
	        resp.getWriter().write("sent mail successfully");	
		} catch(AddressException ae){
			ae.printStackTrace();
			resp.getWriter().write("Please enter a valid email addresses for From and To fields.");
		}catch (Exception e) {			
			e.printStackTrace();
			resp.getWriter().write("Failed to send the mail");
		}
		
	}
}