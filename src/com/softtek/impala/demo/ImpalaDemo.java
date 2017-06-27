package com.softtek.impala.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.cloudera.impala.jdbc4.Driver;
import com.softtek.impala.ImpalaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Created by simon.song on 2017/6/26.
 */
public class ImpalaDemo {

    private static Logger logger = LoggerFactory.getLogger(ImpalaDemo.class);

    private static final String SQL_QUERY_DEPT_ALL = "select * from dept";
    private static final String SQL_QUERY_FAB_ALL = "select * from fab";
    private static final String SQL_QUERY_KAISHEN_ALL = "select * from kaishen";

    public static void main(String[] args) throws SQLException {
        ResultSet resultSet = ImpalaUtil.getQueryResult(SQL_QUERY_KAISHEN_ALL);
        while(resultSet.next()){
            logger.info(">>>>>>>>>>>>>>>>>> Id: {}, Name: {}",resultSet.getInt(1),resultSet.getString(2));
        }

        ImpalaUtil.closeResultSet(resultSet);

    }

    public void test() throws SQLException{
        String JDBC_DRIVER = "com.cloudera.impala.jdbc4.Driver";
        String CONNECTION_URL = "jdbc:impala://172.16.98.195:21050";
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        try
        {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(CONNECTION_URL);
            ps = con.prepareStatement("select * from kaishen");
            rs = ps.executeQuery();
            while (rs.next())
            {
                System.out.println(rs.getInt(1) + "\t\n" + rs.getString(2));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally{
            //TODO Close all the resource with error handling.
            con.close();
            ps.close();
            rs.close();
        }
    }

}
