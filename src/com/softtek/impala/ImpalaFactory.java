package com.softtek.impala;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by simon.song on 2017/6/27.
 */
public class ImpalaFactory {
    private static Logger logger = LoggerFactory.getLogger(ImpalaFactory.class);

    private static ThreadLocal<Connection> connections = new ThreadLocal<Connection>();
    private static ResourceBundle rb = ResourceBundle.getBundle("jdbc");
    private static String url = rb.getString("impala.url");
    private static String driver = rb.getString("impala.driver");
    /**
     * 创建连接
     * @return Connection
     */
    private static Connection createConnection() {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            logger.error("Error occured when getDriver class...");
            e.printStackTrace();
        } catch (SQLException e) {
            logger.error("Error occured when createConnection...");
            e.printStackTrace();
        } catch(Exception ex){
            ex.printStackTrace();
        }

        return conn;

    }

    /**
     * 获取连接
     * @return Connection
     */
    static Connection getConnection() {
        Connection conn = connections.get();
        if (conn == null) {
            conn = createConnection();
            connections.set(conn);
        }
        return conn;
    }
}
