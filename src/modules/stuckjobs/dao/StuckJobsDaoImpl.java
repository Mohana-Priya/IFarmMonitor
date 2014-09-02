package modules.stuckjobs.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import util.DataCon;

public class StuckJobsDaoImpl implements StuckJobsDao {

	@Override
	public String getStuckJobs() throws SQLException {
		System.out.println("StuckJobsDaoImpl:---->getStuckJobs()");
		
		Connection con = null;
		Statement st = null;
		
		try {			
			con = DataCon.getIFarmROSchemaConnnection();
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			StringBuffer sb = new StringBuffer("[");
			System.out.println("Query:----->  select j.job_owner, j.job_name, j.label_id, (sysdate-j.submit_time)*24, j.job_id,j.wf_id from aimesys.aime_job j,aimesys.aime_job_custom_attributes a where " +
					"(j.status = 'READY_TO_RUN' or j.status = 'SUBMITTED' or j.status = 'MATCHED' ) and (sysdate-j.submit_time)*24 > 0.5 and j.wf_id=0 and j.job_id=a.job_id and " +
					"(a.attr_name like '%IFARM%' or a.attr_name like '%COBOL%' or a.attr_name like '%ENCRYPTION%' or  a.attr_name like '%RESTRICTED%' or a.attr_name like '%SHVARAM%' or a.attr_name " +
					"like '%PLUS' or a.attr_name like '%\\_HAS%' or a.attr_name like '%NETWORK%' or a.attr_name like '%FORMS%' or a.attr_name like '%SNA%'  or a.attr_name like '%WORKLOADTEST' or " +
					"a.attr_name like 'ORCL_RDBMS_BUILD%') and j.label_id not like '%^_T%' escape '^' order by j.job_id asc, j.label_id");
			
			ResultSet rs=st.executeQuery("select j.job_owner, j.job_name, j.label_id, (sysdate-j.submit_time)*24, j.job_id,j.wf_id from aimesys.aime_job j,aimesys.aime_job_custom_attributes a where " +
					"(j.status = 'READY_TO_RUN' or j.status = 'SUBMITTED' or j.status = 'MATCHED' ) and (sysdate-j.submit_time)*24 > 0.5 and j.wf_id=0 and j.job_id=a.job_id and " +
					"(a.attr_name like '%IFARM%' or a.attr_name like '%COBOL%' or a.attr_name like '%ENCRYPTION%' or  a.attr_name like '%RESTRICTED%' or a.attr_name like '%SHVARAM%' or a.attr_name " +
					"like '%PLUS' or a.attr_name like '%\\_HAS%' or a.attr_name like '%NETWORK%' or a.attr_name like '%FORMS%' or a.attr_name like '%SNA%'  or a.attr_name like '%WORKLOADTEST' or " +
					"a.attr_name like 'ORCL_RDBMS_BUILD%') and j.label_id not like '%^_T%' escape '^' order by j.job_id asc, j.label_id");

			while(rs.next()) {
				sb.append("{");
				sb.append("\"owner\":\""+rs.getString(1)+"\",");
				sb.append("\"name\":\""+rs.getString(2)+"\",");
				sb.append("\"labelId\":\""+rs.getString(3)+"\",");
				sb.append("\"stuckTime\":\""+rs.getString(4)+"\",");
				sb.append("\"Id\":\""+rs.getString(5)+"\",");
				sb.append("\"workflowId\":\""+rs.getString(6)+"\"");
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
	public String getWorkflowStuckJobs() throws SQLException {
		System.out.println("StuckJobsDaoImpl:---->getWorkflowStuckJobs()");
		
		Connection con = null;
		Statement st = null;
		
		try {			
			con = DataCon.getIFarmROSchemaConnnection();
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			StringBuffer sb = new StringBuffer("[");
			System.out.println("Query:----->  select j.job_owner, j.job_name, j.label_id, (sysdate-j.submit_time)*24, j.job_id,j.wf_id from aimesys.aime_job j," +
					"aimesys.aime_job_custom_attributes a where (j.status = 'READY_TO_RUN' or j.status = 'SUBMITTED' or j.status = 'MATCHED' ) and " +
					"(sysdate-j.submit_time)*24 > 0.5 and j.wf_id!=0 and j.job_id=a.job_id and a.attr_name like '%IFARM%' and j.label_id not like '%^_T%' " +
					"escape '^' order by j.job_id asc, j.label_id");
			
			ResultSet rs=st.executeQuery("select j.job_owner, j.job_name, j.label_id, (sysdate-j.submit_time)*24, j.job_id,j.wf_id from aimesys.aime_job j," +
					"aimesys.aime_job_custom_attributes a where (j.status = 'READY_TO_RUN' or j.status = 'SUBMITTED' or j.status = 'MATCHED' ) and " +
					"(sysdate-j.submit_time)*24 > 0.5 and j.wf_id!=0 and j.job_id=a.job_id and a.attr_name like '%IFARM%' and j.label_id not like '%^_T%' " +
					"escape '^' order by j.job_id asc, j.label_id");

			while(rs.next()) {
				Statement st2 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
				ResultSet rs2 = st2.executeQuery("select job_id from aimesys.aime_job where wf_id="+rs.getString(6)+" and status='RUNNING'");				
				if(!rs2.next()){
					sb.append("{");
					sb.append("\"owner\":\""+rs.getString(1)+"\",");
					sb.append("\"name\":\""+rs.getString(2)+"\",");
					sb.append("\"labelId\":\""+rs.getString(3)+"\",");
					sb.append("\"stuckTime\":\""+rs.getString(4)+"\",");
					sb.append("\"Id\":\""+rs.getString(5)+"\",");
					sb.append("\"workflowId\":\""+rs.getString(6)+"\"");
					sb.append("},");
				}				
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