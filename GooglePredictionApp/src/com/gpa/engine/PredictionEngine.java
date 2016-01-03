package com.gpa.engine;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.prediction.Prediction;
import com.google.api.services.prediction.PredictionScopes;
import com.google.api.services.prediction.model.Input;
import com.google.api.services.prediction.model.Input.InputInput;
import com.google.api.services.prediction.model.Insert;
import com.google.api.services.prediction.model.Insert2;
import com.google.api.services.prediction.model.Insert2.ModelInfo;
import com.google.api.services.prediction.model.Output;

import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;

import com.gpa.common.*;

import com.google.api.client.http.HttpResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PredictionEngine {

	private static PredictionEngine predEngine = null;
	
	private PredictionEngine() {

	}

	public static PredictionEngine getInstance() {
		if (predEngine == null) {
			predEngine = new PredictionEngine();
		}
		return predEngine;
	}

	/** Global instance of the JSON factory. */
	private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private HttpTransport httpTransport;

	private Prediction client;
	
	public Prediction authenticate() {
		JsonFactory JSON_FACTORY = new JacksonFactory();

		Set<String> scopes = new HashSet<String>();
		scopes.add(PredictionScopes.DEVSTORAGE_FULL_CONTROL);
		scopes.add(PredictionScopes.DEVSTORAGE_READ_ONLY);
		scopes.add(PredictionScopes.DEVSTORAGE_READ_WRITE);
		scopes.add(PredictionScopes.PREDICTION);

		GoogleCredential credential = null;
		try {
			// initialize the transport
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();

			credential = new GoogleCredential.Builder()
					.setTransport(httpTransport)
					.setJsonFactory(JSON_FACTORY)
					.setServiceAccountId(Constants.GCP_ACCOUNT_ID)
					.setServiceAccountScopes(scopes)
					.setServiceAccountPrivateKeyFromP12File(new File(Constants.GCP_ACCOUNT_KEY))
					.build();
			// set up global Prediction instance
			client = new Prediction.Builder(httpTransport, JSON_FACTORY, credential)
					.setApplicationName(Constants.APPLICATION_NAME).build();
		} catch (Exception x) {
			x.printStackTrace();
		}
		return client;
	}
	
	public String processTraining(String modelId) throws IOException, GeneralSecurityException {
		System.out.println("Model Training Starts.....");
		Insert insert = new Insert();
		insert.setFactory(JSON_FACTORY);
		insert.setId(modelId);
		insert.setStorageDataLocation(Constants.GCP_STORAGE_TRAINING_URL);
		client.trainedmodels().insert(Constants.PROJECT_NAME, insert).execute();
		System.out.println("Model Training of "+ modelId +" is being processed... ");
		return modelId;
	}
	
	public String processStatusCheck(String modelId) throws IOException, GeneralSecurityException { 
		HttpResponse httpResponse = client.trainedmodels().get(Constants.PROJECT_NAME, modelId).executeUnparsed();
		int status = httpResponse.getStatusCode();
		System.out.println("HTTP Status : " + status);
		String trainingStatus = null;
		if (status == 200) {
			Insert2 res = responseToObject(httpResponse.parseAsString());
			trainingStatus = res.getTrainingStatus();
			System.out.println("Training Status : " + trainingStatus);
			if (trainingStatus.compareTo("DONE") == 0) {
				System.out.println("training complete");
				System.out.println(res.getModelInfo());
			}
		} else {
			httpResponse.ignore();
		}
		return trainingStatus;		
	}
	
	public String processPrediction(String modelId) throws IOException, GeneralSecurityException { 		
		if (!processStatusCheck(modelId).equals("DONE")) {
			System.out.println("Prediction Model not ready.... ");
			return null;
		}
			
		InputStream is = readStorageFile(Constants.GCP_STORAGE_TESTING_FILENAME);

		Input input = new Input();
		InputInput inputInput = new InputInput();		
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		String partitionToken = ",";
		String tokens[];
		String results = "";
		while ((line = br.readLine()) != null) {			
			tokens = line.split(partitionToken);	
			List<Object> features = new ArrayList<Object>();
			for (int i = 1; i < tokens.length; i++ ) {				
				features.add(tokens[i]);
			}
			inputInput.setCsvInstance(features);
			input.setInput(inputInput);
			Output output = client.trainedmodels().predict(Constants.PROJECT_NAME, modelId, input).execute();
			String outputStr = output.getOutputLabel();
			System.out.println("Input : " + line);
			System.out.println("Output : " + outputStr);
			results += "Input : " + line + "	Output :" + outputStr + "<br>";
		}
		return results;
	}

	public InputStream readStorageFile(String file) throws IOException, GeneralSecurityException {
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();

		Set<String> scopes = new HashSet<String>();
		scopes.add(StorageScopes.DEVSTORAGE_FULL_CONTROL);
		scopes.add(StorageScopes.DEVSTORAGE_READ_ONLY);
		scopes.add(StorageScopes.DEVSTORAGE_READ_WRITE);

		GoogleCredential credential = null;
		try {
			credential = new GoogleCredential.Builder()
					.setTransport(httpTransport)
					.setJsonFactory(JSON_FACTORY)
					.setServiceAccountId(Constants.GCP_ACCOUNT_ID)
					.setServiceAccountScopes(scopes)
					.setServiceAccountPrivateKeyFromP12File(new File(Constants.GCP_ACCOUNT_KEY))
					.build();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}

		Storage storage = new Storage.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(Constants.APPLICATION_NAME).build();

		Storage.Objects.Get get = storage.objects().get(Constants.GCP_STORAGE_BUCKET, file);
		// OutputStream stream = new OutputStream(file);
		InputStream is = get.executeMediaAsInputStream();
		return is;
	}
	
	public Insert2 responseToObject(String jsonString) {
		Insert2 res = new Insert2();
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parser.parse(jsonString);
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			res.setCreated(new DateTime(formatter.parse((String) obj.get("created"))));
			res.setId((String) obj.get("id"));
			res.setKind((String) obj.get("kind"));
			res.setSelfLink((String) obj.get("selfLink"));
			res.setTrainingStatus((String) obj.get("trainingStatus"));

			if (obj.get("trainingComplete") != null) {

				res.setTrainingComplete(new DateTime(formatter.parse((String) obj.get("trainingComplete"))));
				JSONObject ml = (JSONObject) obj.get("modelInfo");
				Insert2.ModelInfo modelInfo = new ModelInfo();
				modelInfo.setNumberInstances(Long.parseLong((String) ml.get("numberInstances")));
				modelInfo.setModelType((String) ml.get("modelType"));
				modelInfo.setNumberLabels(Long.parseLong((String) ml.get("numberLabels")));
				modelInfo.setClassificationAccuracy((String) ml.get("classificationAccuracy"));
				res.setModelInfo(modelInfo);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			res = null;
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			res = null;
		}
		return res;
	}
}
