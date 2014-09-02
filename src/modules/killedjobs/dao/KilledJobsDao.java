package modules.killedjobs.dao;

import java.sql.SQLException;

public interface KilledJobsDao {
	String getKilledJobs()throws SQLException;
	String getRunningJobs()throws SQLException;
	String getJobOwner(String labelName)throws SQLException;	
}