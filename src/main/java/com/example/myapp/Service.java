package com.example.myapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.google.gson.Gson;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

public class Service {

    /**
     * 
     * @param bucket
     * @return
     */
    public WidgetRequest getWidgetRequest(String bucket) {
        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client.builder().region(region).build();

        S3Object objFromList = getNextWidgetId(s3, bucket);

        GetObjectRequest _getObjectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(objFromList.key())
            .build();
        ResponseInputStream<GetObjectResponse> s3objectResponse = s3.getObject(_getObjectRequest);
        BufferedReader reader = new BufferedReader(new InputStreamReader(s3objectResponse));
        String s3ObjectString = "";
        
        try {
            s3ObjectString = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        Gson gson = new Gson();

        WidgetRequest s3ObjectJson = gson.fromJson(s3ObjectString, WidgetRequest.class);
        s3ObjectJson.setKey(objFromList.key());
        System.out.println("Getting widgetRequest: widgets/" + s3ObjectJson.getOwner() + "/" + s3ObjectJson.getWidgetId());

        return s3ObjectJson;
    }

    /**
     * 
     * @param bucket
     * @return
     */
    private S3Object getNextWidgetId(S3Client s3, String bucket) {

        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucket)
                    .build();
            ListObjectsResponse res = s3.listObjects(listObjects);
            List<S3Object> objects = res.contents();

            S3Object nextObject = objects.get(0);
            return nextObject;

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
    }

    /**
     * 
     * @param bucket
     * @param widgetRequestId
     */
    public void deleteWidgetRequest(String bucket, String widgetRequestId) {
        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client.builder().region(region).build();

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(bucket)
            .key(widgetRequestId)
            .build();

        System.out.println("Attempting to delete widgetRequest: " + widgetRequestId + "...");
        s3.deleteObject(deleteObjectRequest);
        System.out.println("deleted!");
    }

    /**
     * 
     * @param bucket
     * @param widgetRequest
     */
    public void putWidget(String bucket, WidgetRequest widgetRequest) {
        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client.builder().region(region).build();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(widgetRequest.getKey())
                .build();

        s3.putObject(objectRequest, RequestBody.fromByteBuffer(getRandomByteBuffer(10_000)));
    }

    /**
     * 
     * @param bucket
     */
    public void outputObjectsByBucketName(String bucket) {

        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client.builder().region(region).build();

        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucket)
                    .build();
            ListObjectsResponse res = s3.listObjects(listObjects);
            List<S3Object> objects = res.contents();

            for(S3Object myValue : objects) {
                System.out.print("\n The name of the key is " + myValue.key());
                System.out.print("\n The object is " + calKb(myValue.size()) + " KBs");
                System.out.print("\n The owner is " + myValue.owner());
            }
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

     //convert bytes to kbs.
     private static long calKb(Long val) {
        return val/1024;
    }
}
