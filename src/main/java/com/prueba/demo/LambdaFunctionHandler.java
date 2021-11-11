package com.prueba.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LambdaFunctionHandler implements RequestHandler<String, String> {

    @Override
    public String handleRequest(String req, Context context) {
        context.getLogger().log("groups: " + req);

        JsonParser parser = new JsonParser();
		JsonObject o = parser.parse(req).getAsJsonObject();
		String jsonGroups = o.get("groups").getAsString();
		//JsonObject resp = new JsonObject();
		String resp = null;
		try {
			List<Integer> groups = Arrays.stream(jsonGroups.split(",")).map(el -> {	
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
			 
			//resp.addProperty("sizes", sizes.stream().map(Object::toString).collect(Collectors.joining(",")));
			resp = sizes.stream().map(Object::toString).collect(Collectors.joining(","));
		}catch(Exception ex) {
			///resp.addProperty("Error",ex.getMessage());
			resp = ex.getMessage();
		}
        return resp;
    }

}
