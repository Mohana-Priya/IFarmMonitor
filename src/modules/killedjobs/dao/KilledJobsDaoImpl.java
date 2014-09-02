package modules.killedjobs.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.CommandExecutor;
import util.DataCon;

public class KilledJobsDaoImpl implements KilledJobsDao {

	@Override
	public String getKilledJobs() throws SQLException {
		System.out.println("KilledJobsDaoImpl:---->getKilledJobs()");
		
		Connection con = null;
		Statement st = null;
		
		try {			
			con = DataCon.getIFarmRWSchemaConnnection();
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			StringBuffer sb = new StringBuffer("[");
			System.out.println("Query:----->  select intg_label_stat.label_name,intg_label_stat.task_name,intg_label_stat.task_report_date,NVL(to_char(intg_task_timeouts.task_timeout),'36(default)') from " +
					"intg_label_stat left outer join intg_task_timeouts on REGEXP_REPLACE(intg_label_stat.label_name,'(.*)_(.*)_(.*)', '\\1_\\2') = intg_task_timeouts.label_series " +
					"and intg_label_stat.task_name = intg_task_timeouts.task_name where task_status = '555' and task_report_date >= trunc(sysdate-1) order by intg_label_stat.task_report_date");
			
			ResultSet rs=st.executeQuery("select intg_label_stat.label_name,intg_label_stat.task_name,intg_label_stat.task_report_date,NVL(to_char(intg_task_timeouts.task_timeout),'36(default)') from " +
					"intg_label_stat left outer join intg_task_timeouts on REGEXP_REPLACE(intg_label_stat.label_name,'(.*)_(.*)_(.*)', '\\1_\\2') = intg_task_timeouts.label_series " +
					"and intg_label_stat.task_name = intg_task_timeouts.task_name where task_status = '555' and task_report_date >= trunc(sysdate-1) order by intg_label_stat.task_report_date");

			while(rs.next()) {
				sb.append("{");
				sb.append("\"labelName\":\""+rs.getString(1)+"\",");
				sb.append("\"taskName\":\""+rs.getString(2)+"\",");
				sb.append("\"taskReportDate\":\""+rs.getString(3)+"\",");
				sb.append("\"taskTimeout\":\""+rs.getString(4)+"\"");
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
	public String getRunningJobs() throws SQLException {
		System.out.println("KilledJobsDaoImpl:---->getRunningJobs()");
		
		Connection con = null;
		Statement st = null;
		
		try {			
			con = DataCon.getIFarmRWSchemaConnnection();
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			StringBuffer sb = new StringBuffer("[");
			System.out.println("Query:----->  select intg_label_stat.label_name,intg_label_stat.task_name,intg_label_stat.task_report_date,(sysdate-intg_label_stat.task_report_date)*24 Hours," +
					"NVL(to_char(intg_task_timeouts.task_timeout),'36(default)') from intg_label_stat left outer join intg_task_timeouts on REGEXP_REPLACE(intg_label_stat.label_name,'(.*)_(.*)_(.*)', '\\1_\\2') = " +
					"intg_task_timeouts.label_series and intg_label_stat.task_name = intg_task_timeouts.task_name where task_status = '999' and task_report_date >= trunc(sysdate-2) " +
					"order by task_report_date");
			
			ResultSet rs=st.executeQuery("select intg_label_stat.label_name,intg_label_stat.task_name,intg_label_stat.task_report_date,(sysdate-intg_label_stat.task_report_date)*24 Hours," +
					"NVL(to_char(intg_task_timeouts.task_timeout),'36(default)') from intg_label_stat left outer join intg_task_timeouts on REGEXP_REPLACE(intg_label_stat.label_name,'(.*)_(.*)_(.*)', '\\1_\\2') = " +
					"intg_task_timeouts.label_series and intg_label_stat.task_name = intg_task_timeouts.task_name where task_status = '999' and task_report_date >= trunc(sysdate-2) " +
					"order by task_report_date");

			while(rs.next()) {
				sb.append("{");
				sb.append("\"labelName\":\""+rs.getString(1)+"\",");
				sb.append("\"taskName\":\""+rs.getString(2)+"\",");
				sb.append("\"taskReportDate\":\""+rs.getString(3)+"\",");
				sb.append("\"taskRunningTime\":\""+rs.getFloat(4)+"\",");
				sb.append("\"taskTimeout\":\""+rs.getString(5)+"\"");
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
	public String getJobOwner(String labelName) throws SQLException {
		System.out.println("KilledJobsDaoImpl:---->getJobOwner()");
		
		Connection con = null;
		Statement st = null;
		
		try {			
			con = DataCon.getIFarmRWSchemaConnnection();
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			
			System.out.println("Query:----->  select  NVL(label_current_owner,label_integrator) from intg_label_master where label_name='"+labelName+"'");
			
			ResultSet rs=st.executeQuery("select  NVL(label_current_owner,label_integrator) from intg_label_master where label_name='"+labelName+"'");

			if(rs.next()) {
				String jobOwner = rs.getString(1);//rs.getString() returns null if value in db is null
				if(jobOwner!= null && !jobOwner.equals("")){
					StringBuffer sb = new StringBuffer("");
					CommandExecutor.executeUnixCommand("/net/adc2121213/scratch/rmukul/ifarm_bug18758517_vmd/test_email "+jobOwner, sb);
					return sb.toString().trim();
				}
			}
			
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if(con!=null)
				con.close();
			if(st!=null)
				st.close();
		}		
	}
}