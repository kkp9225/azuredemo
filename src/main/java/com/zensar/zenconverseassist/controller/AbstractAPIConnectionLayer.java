package com.zensar.zenconverseassist.controller;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ResourceUtils;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2beta1.DetectIntentResponse;
import com.google.cloud.dialogflow.v2beta1.QueryInput;
import com.google.cloud.dialogflow.v2beta1.SessionName;
import com.google.cloud.dialogflow.v2beta1.SessionsClient;
import com.google.cloud.dialogflow.v2beta1.SessionsSettings;
import com.google.cloud.dialogflow.v2beta1.TextInput;
import com.google.cloud.dialogflow.v2beta1.TextInput.Builder;

public class AbstractAPIConnectionLayer {

	private static Log log = LogFactory.getLog(AbstractAPIConnectionLayer.class);
	
	
	private static SessionsSettings sessionsSettings;	
	private static String projectId;
	
	//Added for dialgoflow v2 settings
	static{
		 FileInputStream fm;
		 try {
			 File file = ResourceUtils.getFile("classpath:newagent-wrci-f7ead8d55dc6.json");
			fm = new FileInputStream(file);
			GoogleCredentials credentials = GoogleCredentials.fromStream(fm);
			projectId = ((ServiceAccountCredentials)credentials).getProjectId();
			System.out.println(projectId);
			SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
			sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
		} catch (Exception e) {	
			log.error("Exception in JSON file path : ");
			e.printStackTrace();
		}		
	}
	
	protected DetectIntentResponse getActionFromNLPV2(String userQuery, String sessionId){	   	 
	   	
	   	DetectIntentResponse response =null;
	   	log.info("Start of getActionFromNLPV2");
		try {			
			try ( SessionsClient sessionsClient = SessionsClient.create(sessionsSettings)) {	        
	   	 			SessionName session = SessionName.of(projectId, sessionId);
	   	 			Builder textInput = TextInput.newBuilder().setText(userQuery).setLanguageCode("en");
	   	 			QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
	   	 			response = sessionsClient.detectIntent(session, queryInput);
	   	 			log.info("Response from dialogflow : " + response);
	   	 			if (response!=null) {
	   	 				log.info("End of getActionFromNLPV2");
	   	 				return response;
	   	 			} else {
	   	 				return null;
	   	 			}
	   	 	}
		} catch (Exception e) {
			log.error("Exception from getting action data from NLP : ");
			e.printStackTrace();
			return null;
		}		
	 }
}
