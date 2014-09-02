package modules.space.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.DataCon;
import modules.space.dao.SpaceDao;
import modules.space.dao.SpaceDaoImpl;

public class SpaceController extends HttpServlet{
	private static final long serialVersionUID = 6187398762839895516L;
	private SpaceDao spaceDao;
	
	public void init() {
		DataCon.loadDriver();//no init parameters as of now		
		spaceDao = new SpaceDaoImpl();
	}	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException ,IOException{
		System.out.println("SpaceController:---->doGet()");
		System.out.println("The current uri is:"+req.getRequestURI());
		
		if(req.getRequestURI().toString().equals("/IFarmMonitor/getFilerList.htm")){
			this.getFilerList(req,resp);
		}
		
		if(req.getRequestURI().toString().equals("/IFarmMonitor/getCategoryList.htm")){
			this.getCategoryList(req,resp);
		}
		
		if(req.getRequestURI().toString().equals("/IFarmMonitor/getSpaceUtilisations.htm")){
			this.getSpaceUtilisations(req,resp);
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
			doGet(req, resp);
	}

	private void getFilerList(HttpServletRequest req, HttpServletResponse resp) throws IOException {		
		try {
			writeOutput(resp,"text/xml",spaceDao.getFilerList());
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}

	private void getCategoryList(HttpServletRequest req,HttpServletResponse resp) throws IOException {		
		writeOutput(resp,"text/xml","<root><list>Space</list><list>StuckJobs</list><list>Terminated Jobs</list><list>Resources Usage</list></root>");
	}	

	private void getSpaceUtilisations(HttpServletRequest req,HttpServletResponse resp) throws IOException{		
		System.out.println("SpaceController---->getSpaceUtilisations()");
		System.out.println("filer="+req.getParameter("filer"));
		
		List<Integer> spacesList;
		StringBuffer spacesXml = new StringBuffer("<root>");
		try {
			spacesList = spaceDao.getSpaceUtilisations(req.getParameter("filer"));
					
			for(Integer space:spacesList){
				spacesXml.append("<list>");
				spacesXml.append(space);
				spacesXml.append("</list>");
			}
			
			spacesXml.append("<startTime>"+spaceDao.getstartTime(req.getParameter("filer"))+"</startTime>");		
			spacesXml.append("<filer>"+req.getParameter("filer")+"</filer>");
			
			spacesXml.append("</root>");
		
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		writeOutput(resp, "text/xml",spacesXml.toString());		
	}
	
	private void writeOutput(HttpServletResponse resp,String contentType,String output) throws IOException{
		resp.setContentType(contentType);
		resp.setHeader("Cache-Control", "no-cache");
		resp.getWriter().write(output);
	}
}
