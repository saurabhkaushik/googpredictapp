package com.gpa.common;

public class Constants { 
	public static String APPLICATION_NAME = "googpredictapp";
	public static String PROJECT_NAME = "googpredictapp";
	
	public static String GCP_ACCOUNT_KEY = "appdata/googpredictapp-4aeb82a0eaf6.p12";	
	public static String GCP_ACCOUNT_ID = "superskpredict@googpredictapp.iam.gserviceaccount.com";

	public static String GCP_STORAGE_BUCKET = "predictapp";
	public static String GCP_STORAGE_TRAINING_FILENAME = "train.csv";
	public static String GCP_STORAGE_TESTING_FILENAME = "test.csv";
	
	public static String GCP_STORAGE_TRAINING_URL = GCP_STORAGE_BUCKET + "/" + GCP_STORAGE_TRAINING_FILENAME; 
	public static String GCP_STORAGE_TESTING_URL  = GCP_STORAGE_BUCKET + "/" + GCP_STORAGE_TESTING_FILENAME;
}
