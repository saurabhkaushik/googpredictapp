package com.gpa.common;

public class Constants { 
	public static String APPLICATION_NAME = "";
	public static String PROJECT_NAME = "";
	
	public static String GCP_ACCOUNT_KEY = "";	
	public static String GCP_ACCOUNT_ID = "";

	public static String GCP_STORAGE_BUCKET = "";
	public static String GCP_STORAGE_TRAINING_FILENAME = "";
	public static String GCP_STORAGE_TESTING_FILENAME = "";
	
	public static String GCP_STORAGE_TRAINING_URL = GCP_STORAGE_BUCKET + "/" + GCP_STORAGE_TRAINING_FILENAME; 
	public static String GCP_STORAGE_TESTING_URL  = GCP_STORAGE_BUCKET + "/" + GCP_STORAGE_TESTING_FILENAME;
}
