package modules.stuckjobs.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.CommandExecutor;
import modules.stuckjobs.dao.StuckJobsDao;
import modules.stuckjobs.dao.StuckJobsDaoImpl;

public class StuckJobsController extends HttpServlet {
    private static final long serialVersionUID = 6414357894800947121L;
	private StuckJobsDao stuckJobsDao;
    
    public void init() {		
    	stuckJobsDao = new StuckJobsDaoImpl();
	}
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		doGet(req, resp);
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException ,IOException{
    	System.out.println("StuckJobsController:---->doGet()");
		System.out.println("The current uri is:"+req.getRequestURI());
		
		if(req.getRequestURI().toString().equals("/IFarmMonitor/getStuckJobs.htm")){
			getStuckJobs(req,resp);
		} else if (req.getRequestURI().toString().equals("/IFarmMonitor/getWorkflowStuckJobs.htm")){
			getWorkflowStuckJobs(req,resp);
		} else if (req.getRequestURI().toString().equals("/IFarmMonitor/getStuckJobInfo.htm")){
			getStuckJobInfo(req,resp);
		}
    }	

	private void getStuckJobs(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("StuckJobsController:---->getStuckJobs()");
		
		try {						
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
	        resp.getWriter().write(stuckJobsDao.getStuckJobs());
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	private void getWorkflowStuckJobs(HttpServletRequest req,HttpServletResponse resp) {
		System.out.println("StuckJobsController:---->getWorkflowStuckJobs()");
		
		try {						
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
	        resp.getWriter().write(stuckJobsDao.getWorkflowStuckJobs());
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
	}
	
	private void getStuckJobInfo(HttpServletRequest req,HttpServletResponse resp) {
		System.out.println("StuckJobsController:---->getStuckJobInfo()");
		
		try {
			StringBuffer responseJson = new StringBuffer();
			resp.setContentType("text/html");
			if(req.getParameter("type").equals("gcoutput"))			
				CommandExecutor.executeUnixCommand(System.getenv().get("IFARMMONITORING_GCOUTPUT_COMMAND")+" "+req.getParameter("jobId"),responseJson);
			else if(req.getParameter("type").equals("checkmyjob"))			
				CommandExecutor.executeUnixCommand(System.getenv().get("IFARMMONITORING_CHECKMYJOB_COMMAND")+" "+req.getParameter("jobId"),responseJson);
			
	        resp.getWriter().write(responseJson.toString());        
		} catch (Exception e) {			
			e.printStackTrace();
		}		
	}	
}