package com.boc.cloud.test;

import java.io.IOException;

import net.sf.json.JSONObject;
import okhttp3.*;

public class CallApplicationAuthAPI {
	
	public static void main(String[] args) {
		String clientID = "d25e28ec-ee87-48c8-881c-838ff6dbecb4";
		String clientSecret = "B2pE1aN0yR3qY0iU6aX6fV2nG1nG7tF4pO8lR3yD0uV8fJ8yO3";
		String tokenURL = "https://api.au.apiconnect.ibmcloud.com/hkboc-hackathon/dev/oauth2/token";
		try {
			OkHttpClient client = new OkHttpClient();
			// get token
			MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
			StringBuffer json = new StringBuffer();
			json.append("grant_type=").append("client_credentials")
				.append("&scope=").append("all")
				.append("&client_id=").append(clientID)
				.append("&client_secret=").append(clientSecret);
			RequestBody body = RequestBody.create(mediaType, json.toString());
			Request tRequest = new Request.Builder()
					  .url(tokenURL)
					  .post(body)
					  .addHeader("content-type", "application/x-www-form-urlencoded")
					  .addHeader("accept", "application/json")
					  .build();
			Response tResponse = client.newCall(tRequest).execute();
			String reponseBody = tResponse.body().string();
			System.out.println(reponseBody);
			JSONObject jsonObj = JSONObject.fromObject(reponseBody);
			String access_token = jsonObj.getString("access_token");
			// call API
			Request request = new Request.Builder()
			  .url("https://api.au.apiconnect.ibmcloud.com/hkboc-hackathon/dev/api/bank-info/atms")
			  .get()
			  .addHeader("authorization", "Bearer " + access_token)
			  .addHeader("accept", "application/json")
			  .build();
			Response response = client.newCall(request).execute();
			System.out.println(response.body().string());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
