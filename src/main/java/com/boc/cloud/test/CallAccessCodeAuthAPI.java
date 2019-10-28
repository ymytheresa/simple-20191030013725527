package com.boc.cloud.test;

import net.sf.json.JSONObject;
import okhttp3.*;

import java.io.IOException;

public class CallAccessCodeAuthAPI {
	public static void main(String[] args) {
		String clientID = "d25e28ec-ee87-48c8-881c-838ff6dbecb4";
		String clientSecret = "B2pE1aN0yR3qY0iU6aX6fV2nG1nG7tF4pO8lR3yD0uV8fJ8yO3";
		String authURL = " https://api.au.apiconnect.ibmcloud.com/hkboc-hackathon/dev/oauth2/authorize";
		String tokenURL = "https://api.au.apiconnect.ibmcloud.com/hkboc-hackathon/dev/oauth2/token";
		String redirectURL = "www.test.com";
		try {
			OkHttpClient client = new OkHttpClient().newBuilder().followRedirects(false).build();
			// authorize get access code
			MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
			StringBuffer json = new StringBuffer();
			json.append("response_type=").append("code")
			.append("&scope=").append("all")
			.append("&username=").append("cust3101")
			.append("&password=").append("bochk")
			.append("&apim-source=").append("html-login")
			.append("&login=").append("true")
			.append("&client_id=").append(clientID)
			.append("&redirect_uri=").append(redirectURL);
			RequestBody body = RequestBody.create(mediaType, json.toString());
			Request aRequest = new Request.Builder()
			  .url(authURL)
			  .post(body)
			  .addHeader("content-type", "application/x-www-form-urlencoded")
			  .addHeader("accept", "text/html")
			  .build();

			Response aResponse = client.newCall(aRequest).execute();
			String location = aResponse.headers("Location").toString();
			System.out.println(location);
			String accessCode = location.substring(location.indexOf("code=") + 5, location.length()-1);
			System.out.println(accessCode);
			// get token
			json = new StringBuffer();
			json.append("grant_type=").append("authorization_code")
				.append("&code=").append(accessCode)
				.append("&scope=").append("all")
				.append("&client_id=").append(clientID)
				.append("&client_secret=").append(clientSecret)
				.append("&redirect_uri=").append(redirectURL);
			RequestBody tBody = RequestBody.create(mediaType, json.toString());
			Request tRequest = new Request.Builder()
					  .url(tokenURL)
					  .post(tBody)
					  .addHeader("content-type", "application/x-www-form-urlencoded")
					  .addHeader("accept", "application/json")
					  //.addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientID + ":" + clientSecret).getBytes()))
					  .build();
			Response tResponse = client.newCall(tRequest).execute();
			String reponseBody = tResponse.body().string();
			System.out.println(reponseBody);
			JSONObject jsonObj = JSONObject.fromObject(reponseBody);
			String access_token = jsonObj.getString("access_token");
			
			// call api
			Request apiRequest = new Request.Builder()
			  .url("https://api.au.apiconnect.ibmcloud.com/hkboc-hackathon/dev/api/credit-cards")
			  .get()
			  .addHeader("authorization", "Bearer " + access_token)
			  .addHeader("accept", "application/json")
			  .build();
			Response apiReponse = client.newCall(apiRequest).execute();
			System.out.println(apiReponse.body().string());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
