package com.softtek.impala;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * Created by simon.song on 2017/6/27.
 */
public class ImpalaUtil {

    private static Logger logger = LoggerFactory.getLogger(ImpalaUtil.class);

    public static ResultSet getQueryResult(String query){
        ResultSet resultSet = null;
        try{
            Statement statement = ImpalaFactory.getConnection().createStatement();
            resultSet = statement.executeQuery(query);
        }catch(SQLException e){
            logger.error("Error occured when getQueryResult...");
            e.printStackTrace();
        }

        return resultSet;
    }

    public static void closeResultSet(ResultSet rs){
        try{
            rs.close();
        }catch(SQLException e){
            logger.error("Error occured when closeResultSet...");
            e.printStackTrace();
        }

    }
}
