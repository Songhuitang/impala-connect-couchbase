package com.softtek.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.PersistTo;
import com.couchbase.client.java.document.Document;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.oracle.javafx.jmx.json.JSONDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by simon.song on 2017/6/27.
 */
public class CouchbaseUtil {
    private static Logger logger = LoggerFactory.getLogger(CouchbaseUtil.class);

    public static N1qlQueryResult getQueryResult( String bucket, String query){
        N1qlQueryResult result = null;

        // Connect to the bucket and open it
        Bucket couchBucket = CouchbaseFactory.getCouchbaseCluster().openBucket(bucket);
        // Create a N1QL Primary Index (but ignore if it exists)
        couchBucket.bucketManager().createN1qlPrimaryIndex(true,false);
        // Perform a N1QL Query
       result = couchBucket.query(
                N1qlQuery.simple(query)
        );

        closeBucket(couchBucket);

        return result;
    }

    public static N1qlQueryResult getQueryResult(String bucket, String query, String Condition){
        N1qlQueryResult result = null;

        // Connect to the bucket and open it
        Bucket couchBucket = CouchbaseFactory.getCouchbaseCluster().openBucket(bucket);
        // Create a N1QL Primary Index (but ignore if it exists)
        couchBucket.bucketManager().createN1qlPrimaryIndex(true,false);
        // Perform a N1QL Query
        result = couchBucket.query(
                N1qlQuery.parameterized(query, JsonArray.from(Condition))
        );

        closeBucket(couchBucket);

        return result;
    }

    public static void upsertDocument(String bucket, String documentId, JsonObject json){
        Bucket upsertBucket = CouchbaseFactory.getCouchbaseCluster().openBucket(bucket);
        upsertBucket.upsert(JsonDocument.create(documentId,json));

        logger.info("document created with ID: {}, in bucket: {}", documentId, bucket);

        closeBucket(upsertBucket);
    }

    public static void deleteDocument(String bucket, String documentId){
        Bucket delBucket = CouchbaseFactory.getCouchbaseCluster().openBucket(bucket);
        delBucket.remove(documentId);

        logger.info("document deleted with ID: {}, in bucket: {}", documentId, bucket);

        closeBucket(delBucket);

    }

    public static JsonDocument getDocumentById(String bucket, String documentId){
        Bucket getBucket = CouchbaseFactory.getCouchbaseCluster().openBucket(bucket);
        JsonDocument document = getBucket.get(documentId);

        closeBucket(getBucket);

        return document;
    }

    private static void closeBucket(Bucket bucket){
        bucket.close();
    }

}
