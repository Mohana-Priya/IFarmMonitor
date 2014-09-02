package modules.resourceusage.dao;

import java.sql.SQLException;

public interface ResourceUsageDao {
	String getResources(String noOfDays, String platform)throws SQLException;
	String getPlatforms()throws SQLException;	
}