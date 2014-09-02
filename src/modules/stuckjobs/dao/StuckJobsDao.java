package modules.stuckjobs.dao;

import java.sql.SQLException;

public interface StuckJobsDao {

	String getStuckJobs()throws SQLException;
	String getWorkflowStuckJobs()throws SQLException;
	
}