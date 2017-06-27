package com.softtek.couchbase;

import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * Created by simon.song on 2017/6/27.
 */
public class CouchbaseFactory {
    private static Logger logger = LoggerFactory.getLogger(CouchbaseFactory.class);

    private static ThreadLocal<CouchbaseCluster> clusters = new ThreadLocal<CouchbaseCluster>();

    private static ResourceBundle rb = ResourceBundle.getBundle("jdbc");
    private static String nodeList = rb.getString("couchbase.nodeList");

    private static CouchbaseCluster createCouchbaseCluster(){

        List<String> nodes = new ArrayList<String>();

        String[] list = nodeList.split(",");
        for(String node: list){
            logger.info(">>>>>>>>>>>>>>> Node list added: {}",node);
            nodes.add(node);
        }

        //Disable bootstrapCarrier, Enable bootstrapHttp,  set your http_port
        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                .bootstrapCarrierEnabled(false)
                .bootstrapHttpDirectPort(8091)
                .socketConnectTimeout((int) TimeUnit.SECONDS.toMillis(45))
                .connectTimeout(TimeUnit.SECONDS.toMillis(60)).bootstrapHttpEnabled(true).build();

        // Create a cluster reference
        CouchbaseCluster cluster = CouchbaseCluster.create(env, nodeList);

        return cluster;
    }

    public static CouchbaseCluster getCouchbaseCluster(){
        CouchbaseCluster cluster = clusters.get();
        if(null == cluster){
            cluster = createCouchbaseCluster();
            clusters.set(cluster);
        }
        return cluster;
    }
}
