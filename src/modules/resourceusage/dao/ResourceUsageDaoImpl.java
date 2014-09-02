package modules.resourceusage.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import util.DataCon;

public class ResourceUsageDaoImpl implements ResourceUsageDao {

	@Override
	public String getResources(String noOfDays,String platform) throws SQLException {
		System.out.println("ResourceUsageDaoImpl:---->getResources()");
		
		Connection con = null;
		Statement st = null;
		
		try {			
			con = DataCon.getIFarmROSchemaConnnection();
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			StringBuffer sb = new StringBuffer("[");
			System.out.println("Query:-----> " +
					"select platform,resource_name," +
					"NVL(sum(CASE WHEN status = 'RUNNING' THEN CASE WHEN sysdate-start_time <= "+noOfDays+" THEN SYSDATE-start_time ELSE "+noOfDays+" END " +
					"ELSE CASE WHEN SYSDATE-start_time <= "+noOfDays+" then end_time-start_time else end_time-(sysdate-"+noOfDays+") END END)*24,0) as usage_time," +
					"count(job_Id) as no_of_jobs,resource_status,no_of_vcpus,architecture,zone_name from (select resources.opsys_flavor as platform," +
					" resources.resource_name as resource_name,jobs.status as status,jobs.start_time as start_time,jobs.end_time as end_time," +
					"jobs.job_Id as job_id,resources.status as resource_status,resources.total_vcpus as no_of_vcpus,resources.architecture as architecture," +
					" resources.zone_name as zone_name from aimesys.aime_resource resources left join aimesys.aime_job jobs on jobs.resource_name = resources.resource_name" +
					" and jobs.belongs_to = resources.belongs_to and jobs.status in('RUNNING','COMPLETED') and " +
					"CASE WHEN jobs.status = 'RUNNING' THEN 0 ELSE sysdate-jobs.end_time END <="+noOfDays+"  where resources.belongs_to='IFARM')" +
					" group by platform,resource_name,resource_status,no_of_vcpus,architecture,zone_name having platform='"+platform+"'order by usage_time desc");
			
			ResultSet rs=st.executeQuery("select platform,resource_name," +
					"NVL(sum(CASE WHEN status = 'RUNNING' THEN CASE WHEN sysdate-start_time <= "+noOfDays+" THEN SYSDATE-start_time ELSE "+noOfDays+" END " +
					"ELSE CASE WHEN SYSDATE-start_time <= "+noOfDays+" then end_time-start_time else end_time-(sysdate-"+noOfDays+") END END)*24,0) as usage_time," +
					"count(job_Id) as no_of_jobs,resource_status,no_of_vcpus,architecture,zone_name from (select resources.opsys_flavor as platform," +
					" resources.resource_name as resource_name,jobs.status as status,jobs.start_time as start_time,jobs.end_time as end_time," +
					"jobs.job_Id as job_id,resources.status as resource_status,resources.total_vcpus as no_of_vcpus,resources.architecture as architecture," +
					" resources.zone_name as zone_name from aimesys.aime_resource resources left join aimesys.aime_job jobs on jobs.resource_name = resources.resource_name" +
					" and jobs.belongs_to = resources.belongs_to and jobs.status in('RUNNING','COMPLETED') and " +
					"CASE WHEN jobs.status = 'RUNNING' THEN 0 ELSE sysdate-jobs.end_time END <="+noOfDays+"  where resources.belongs_to='IFARM')" +
					" group by platform,resource_name,resource_status,no_of_vcpus,architecture,zone_name having platform='"+platform+"'order by usage_time desc");

			while(rs.next()) {
				sb.append("{");
				sb.append("\"platform\":\""+rs.getString(1)+"\",");
				sb.append("\"resourceName\":\""+rs.getString(2)+"\",");
				sb.append("\"usageTime\":\""+rs.getFloat(3)+"\",");
				sb.append("\"noOfJobs\":\""+rs.getInt(4)+"\",");
				sb.append("\"resourceStaus\":\""+rs.getString(5)+"\",");
				sb.append("\"noOfVCPUs\":\""+rs.getInt(6)+"\",");
				sb.append("\"architecture\":\""+rs.getString(7)+"\",");
				sb.append("\"zoneName\":\""+rs.getString(8)+"\"");
				sb.append("},");
			}
			
			if(sb.lastIndexOf(",")!= -1)
				sb.deleteCharAt(sb.lastIndexOf(","));
			
			sb.append("]");
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(con!=null)
				con.close();
			if(st!=null)
				st.close();
		}
		
		return null;
	}

	@Override
	public String getPlatforms() throws SQLException {
		System.out.println("ResourceUsageDaoImpl:---->getPlatforms()");
		
		Connection con = null;
		Statement st = null;
		
		try {			
			con = DataCon.getIFarmROSchemaConnnection();
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			StringBuffer sb = new StringBuffer("[");
			System.out.println("Query:----->  select distinct(opsys_flavor) from aimesys.aime_resource where belongs_to='IFARM' order by opsys_flavor");
			
			ResultSet rs=st.executeQuery("select distinct(opsys_flavor) from aimesys.aime_resource where belongs_to='IFARM' order by opsys_flavor");

			while(rs.next()) {
				sb.append("{");
				sb.append("\"platform\":\""+rs.getString(1)+"\"");				
				sb.append("},");
			}
			
			if(sb.lastIndexOf(",")!= -1)
				sb.deleteCharAt(sb.lastIndexOf(","));
			
			sb.append("]");
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(con!=null)
				con.close();
			if(st!=null)
				st.close();
		}
		
		return null;
	}	
}