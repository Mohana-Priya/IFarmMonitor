package modules.resourceusage.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modules.resourceusage.dao.ResourceUsageDao;
import modules.resourceusage.dao.ResourceUsageDaoImpl;

public class ResourceUsageController extends HttpServlet{	
	private static final long serialVersionUID = 5145442772987174426L;
	private ResourceUsageDao resourseUsageDao;
    
    public void init() {    	
    	resourseUsageDao = new ResourceUsageDaoImpl();
	}
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		doGet(req, resp);
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException ,IOException{
    	System.out.println("ResourceUsageController:---->doGet()");
		System.out.println("The current uri is:"+req.getRequestURI());
		
		if(req.getRequestURI().toString().equals("/IFarmMonitor/getResources.htm")){
			getResources(req,resp);
		}else if(req.getRequestURI().toString().equals("/IFarmMonitor/getPlatforms.htm")){
			getPlatforms(req,resp);
		}
    }    

	private void getResources(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("ResourceUsageController:---->getResources()");
		
		try {						
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
	        resp.getWriter().write(resourseUsageDao.getResources(req.getParameter("noOfDays"),req.getParameter("platform")));
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	private void getPlatforms(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("ResourceUsageController:---->getPlatforms()");
		
		try {						
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
	        resp.getWriter().write(resourseUsageDao.getPlatforms());
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
}