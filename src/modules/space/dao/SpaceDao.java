package modules.space.dao;

import java.sql.SQLException;
import java.util.List;

public interface SpaceDao {

	List<Integer> getSpaceUtilisations(String serverName)throws SQLException;
	String getstartTime(String serverName)throws SQLException;
	String getFilerList()throws SQLException;

}
