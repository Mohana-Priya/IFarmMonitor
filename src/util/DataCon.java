package util;
import java.sql.*;

public class DataCon
{
	public static void loadDriver(){
		try{
			Class.forName("oracle.jdbc.OracleDriver");						
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}							
	}
	
	public static Connection getConnnection() throws SQLException{
		return DriverManager.getConnection(System.getenv().get("IFARMMONITORING_DB_URL"),System.getenv().get("IFARMMONITORING_DB_UNAME"),System.getenv().get("IFARMMONITORING_DB_PWD"));
	}
	
	public static Connection getIFarmROSchemaConnnection() throws SQLException{
		return DriverManager.getConnection(System.getenv().get("IFARMRO_SCHEMA_URL"),System.getenv().get("IFARMRO_SCHEMA_UNAME"),System.getenv().get("IFARMRO_SCHEMA_PWD"));
	}

	public static Connection getIFarmRWSchemaConnnection() throws SQLException{
		return DriverManager.getConnection(System.getenv().get("IFARMRW_SCHEMA_URL"),System.getenv().get("IFARMRW_SCHEMA_UNAME"),System.getenv().get("IFARMRW_SCHEMA_PWD"));
	}	
}