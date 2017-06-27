package com.softtek;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.softtek.couchbase.CouchbaseUtil;
import com.softtek.impala.ImpalaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;

/**
 *
 * Created by simon.song on 2017/6/27.
 */
public class MainDemo {
    private static Logger logger = LoggerFactory.getLogger(MainDemo.class);

    private static final String SQL_QUERY_KAISHEN_ALL = "select * from kaishen";

    private static final String BUCKET_STARBUCKS = "starbucks";
    private static final String SQL_PARAM_STARBUCKS = "SELECT * FROM starbucks WHERE $1 IN created";

    public static void main(String args[]){
        ResultSet resultSet = null;
        int count = 0;
        try{
            resultSet = ImpalaUtil.getQueryResult(SQL_QUERY_KAISHEN_ALL);
            while(resultSet.next()){

                JsonObject data = JsonObject.create()
                        .put("Id", resultSet.getInt(1))
                        .put("name", resultSet.getString(2))
                        .put("created", JsonArray.from(1, "IT"));
                logger.info(">>>>>>>>>>>>>>>>>> Id: {}, Name: {}",resultSet.getInt(1),resultSet.getString(2));

                CouchbaseUtil.upsertDocument(BUCKET_STARBUCKS,"u:king_arthur_" + count,data);
                count ++;
            }
        }catch(Exception e){
            logger.error("Error!!!!");
        }


        N1qlQueryResult startResult = CouchbaseUtil.getQueryResult(BUCKET_STARBUCKS,SQL_PARAM_STARBUCKS,"IT");

        for (N1qlQueryRow row : startResult) {
            logger.info(">>>>>>>>>>>>>>>>>>>>> Retrieve {} data: \t\n {}", BUCKET_STARBUCKS, row);
        }


        ImpalaUtil.closeResultSet(resultSet);
    }
}
