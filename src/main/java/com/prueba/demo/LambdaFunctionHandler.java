package com.prueba.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class LambdaFunctionHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	@Override
	public APIGatewayV2HTTPResponse  handleRequest(APIGatewayV2HTTPEvent event, Context context)
	{
		LambdaLogger logger = context.getLogger();
		APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();
	    response.setIsBase64Encoded(false);
	    response.setStatusCode(200);
		JsonObject sizesJson = new JsonObject();
		//String body = "";
		String jsonGroups = null;
		List<Integer> groups = null;
        try {
		    try {
		    	String body = event.getBody();
		    	JsonParser parser = new JsonParser();
				JsonObject o = parser.parse(body).getAsJsonObject();
				jsonGroups = o.get("groups").getAsString();				
				groups = Arrays.stream(jsonGroups.split(",")).map(el -> {	
					try {
						return Integer.parseInt(el);
					}catch(NumberFormatException e){
						throw new NumberFormatException("{"+el+"}"+" No es un número entero");
					}						
				}).collect(Collectors.toList());
				List<Integer> sizes = new ArrayList<>();
				Integer maxGroupSize = Collections.max(groups);
				Integer sumGroups = groups.stream()
						  .mapToInt(Integer::intValue)
						  .sum();
				int i = maxGroupSize;			
				while(true) {					
					Integer sum = 0;
					for(Integer group:groups) {
						sum += group;
						if(sum == i) {
							sum = 0;
						}else if(sum > i) {
							break;
						}
					}
					if(sum == 0) {
						sizes.add(i);
					}else if(sum < i && i > sumGroups){					
						break;
					}
					i++;
				}	    
				sizesJson.addProperty("sizes", sizes.stream().map(Object::toString).collect(Collectors.joining(",")));			
				}catch(Exception ex) {
					sizesJson.addProperty("Error",ex.getMessage());				
				}
			    HashMap<String, String> headers = new HashMap<String, String>();
			    headers.put("Content-Type", "text/html");
			    response.setHeaders(headers);
			    response.setBody(gson.toJson(sizesJson));			    
        }catch (IllegalStateException | JsonSyntaxException exception){
              logger.log(exception.toString());
        }
        return response;
    }
}
