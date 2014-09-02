package modules.space.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.DataCon;

public class SpaceDaoImpl implements SpaceDao {

	@Override
	public String getFilerList() throws SQLException {
		System.out.println("SpaceDaoImpl:--->getFilerList()");
		
		StringBuffer spaceFilersSB = new StringBuffer("<root>");		
		Connection con = null;
		Statement st = null;
		
		try {			
			con = DataCon.getConnnection();
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);			
			
			System.out.println("Query:----->"+"select distinct(server) from space order by server");
			ResultSet rs=st.executeQuery("select distinct(server) from space order by server");
			while(rs.next()) {				
				spaceFilersSB.append("<list>"+rs.getString(1)+"</list>");		
			}
			spaceFilersSB.append("</root>");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getting the filers list");
		} finally {			
			con.close();
			st.close();					
		}
		return spaceFilersSB.toString();
	}
	
	@Override
	public List<Integer> getSpaceUtilisations(String serverName) throws SQLException {
		System.out.println("SpaceDaoImpl:--->getSpaceUtilisations()");
		List<Integer> spacesList = new ArrayList<Integer>();
		
		Connection con = null;
		Statement st = null;
		
		try {			
			con = DataCon.getConnnection();
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);			
			
			System.out.println("Query:----->"+"select spaceutilisation from(select spaceutilisation from space where server='"+serverName+"' order by time desc) spaces where rownum<=1440 order by rownum desc");
			ResultSet rs=st.executeQuery("select spaceutilisation from(select spaceutilisation from space where server='"+serverName+"' order by time desc) spaces where rownum<=1440 order by rownum desc");
			while(rs.next()) {				
				spacesList.add(rs.getInt(1));				
			}			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getting the spaces for server:"+serverName);
		} finally {			
			con.close();
			st.close();					
		}
		return spacesList;
	}

	@Override
	public String getstartTime(String serverName) throws SQLException {
		System.out.println("SpaceDaoImpl:--->getstartTime()");
		String startTime = "";
		Connection con = null;
		Statement st = null;
		
		try {			
			con = DataCon.getConnnection();
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			
			System.out.println("Query:"+"select to_char(ADD_MONTHS(min(time),-1),'YYYY,MM,DD,HH24,MI,SS') as timeHM from (select time from space where server='"+serverName+"' order by time desc)spaces where rownum<=1440");
			ResultSet rs=st.executeQuery("select to_char(ADD_MONTHS(min(time),-1),'YYYY,MM,DD,HH24,MI,SS') as timeHM from (select time from space where server='"+serverName+"' order by time desc)spaces where rownum<=1440");
			if(rs.next()) {				
				startTime = rs.getString(1);				
			}			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getting the spaces for server:"+serverName);
		} finally {
			con.close();
			st.close();
		}
		return startTime;
	}	
}