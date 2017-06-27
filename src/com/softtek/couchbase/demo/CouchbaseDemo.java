package com.softtek.couchbase.demo;

import com.couchbase.client.java.*;
import com.couchbase.client.java.document.*;
import com.couchbase.client.java.document.json.*;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.query.*;
import com.softtek.couchbase.CouchbaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 *
 * Created by simon.song on 2017/6/22.
 */
public class CouchbaseDemo {

    private static Logger logger = LoggerFactory.getLogger(CouchbaseDemo.class);

    private static final String BUCKET_TRAVEL = "travel-sample";
    private static final String SQL_TRAVEL_TOP_5 = "select * from `travel-sample` limit 5";

    private static final String BUCKET_STARBUCKS = "starbucks";
    private static final String SQL_PARAM_STARBUCKS = "SELECT * FROM starbucks WHERE $1 IN interests";

    public static void main(String args[]) {

        N1qlQueryResult result = CouchbaseUtil.getQueryResult(BUCKET_TRAVEL,SQL_TRAVEL_TOP_5);

        for (N1qlQueryRow row : result) {
            logger.info(">>>>>>>>>>>>>>>>>>>>> Retrieve {} data: \t\n {}", BUCKET_TRAVEL, row);
        }

        // Create a JSON Document
        JsonObject arthur = JsonObject.create()
                .put("name", "Author")
                .put("email", "kingarthur@xxxx.com")
                .put("interests", JsonArray.from("Holy Grail", "African Swallows"));

        CouchbaseUtil.upsertDocument(BUCKET_STARBUCKS,"u:king_arthur",arthur);

        N1qlQueryResult startResult = CouchbaseUtil.getQueryResult(BUCKET_STARBUCKS,SQL_PARAM_STARBUCKS,"African Swallows");

        for (N1qlQueryRow row : startResult) {
            logger.info(">>>>>>>>>>>>>>>>>>>>> Retrieve {} data: \t\n {}", BUCKET_STARBUCKS, row);
        }

        System.exit(0);
    }

    public static void test(){
        List<String> nodeList = new ArrayList<String>();
        nodeList.add("172.16.98.195");
        nodeList.add("172.16.98.196");
        nodeList.add("172.16.98.197");
        /*nodeList.add("172.16.97.141");*/
        //Disable bootstrapCarrier, Enable bootstrapHttp,  set your http_port
        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                .bootstrapCarrierEnabled(false)
                .bootstrapHttpDirectPort(8091)
                .socketConnectTimeout((int) TimeUnit.SECONDS.toMillis(45))
                .connectTimeout(TimeUnit.SECONDS.toMillis(60)).bootstrapHttpEnabled(true).build();
        // Create a cluster reference
        CouchbaseCluster cluster = CouchbaseCluster.create(env, nodeList);


        // Connect to the bucket and open it
        Bucket travelSampleBucket = cluster.openBucket("travel-sample");
        // Perform a N1QL Query
        N1qlQueryResult result = travelSampleBucket.query(
                N1qlQuery.simple("select * from `travel-sample` limit 5")
        );
        // Print each found Row
        for (N1qlQueryRow row : result) {
            System.out.println(row);
        }
        travelSampleBucket.close();


        Bucket starbucksBucket = cluster.openBucket("starbucks");
        // Create a JSON Document
        JsonObject arthur = JsonObject.create()
                .put("name", "Author")
                .put("email", "kingarthur@couchbase.com")
                .put("interests", JsonArray.from("Holy Grail", "African Swallows"));

        // Store the Document
        starbucksBucket.upsert(JsonDocument.create("u:king_arthur", arthur));
        System.out.println(starbucksBucket.get("u:king_arthur"));
        // Create a N1QL Primary Index (but ignore if it exists)
        starbucksBucket.bucketManager().createN1qlPrimaryIndex(true,false);
        N1qlQueryResult starResult = starbucksBucket.query(
                N1qlQuery.parameterized("SELECT * FROM starbucks WHERE $1 IN interests",JsonArray.from("African Swallows"))
        );
        for (N1qlQueryRow row : starResult) {
            System.out.println(row);
        }
//        starbucksBucket.remove("u:king_arthur");
        starbucksBucket.close();

        // Disconnect and close all buckets
        cluster.disconnect();

        System.out.println();

        System.exit(0);
    }
}
