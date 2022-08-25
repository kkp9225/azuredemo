package com.zensar.zenconverseassist.controller;

import java.text.ParseException;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.cloud.dialogflow.v2beta1.DetectIntentResponse;

@Controller
@RequestMapping("getDataV2")
public class ActionInfoControllerV2 extends AbstractAPIConnectionLayer {
	private Log log = LogFactory.getLog(ActionInfoControllerV2.class);


	@PostMapping("actionInfoV2")
	public ResponseEntity<?> getActionInfo()
			throws ParseException, JSONException {
		log.info("Start of getActionInfo");
		String userQuery = "Book ticket";
		
		String sessionId = null;
		if (sessionId == null || sessionId.isEmpty() || sessionId.equals("")) {
			sessionId = UUID.randomUUID().toString();
		}

		DetectIntentResponse response = getActionFromNLPV2(userQuery, sessionId);
		String queryOutput = null;
		
		if (response != null) {
			queryOutput = response.getQueryResult().getFulfillmentText();
		}
		log.info("Query output : " + queryOutput);
		log.info("End of getActionInfo");
		return new ResponseEntity<>(queryOutput, HttpStatus.OK);
	}

}