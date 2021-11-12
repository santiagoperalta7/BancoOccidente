package com.prueba.demo;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LambdaFunctionHandlerTest {

    private static APIGatewayV2HTTPEvent groups = new APIGatewayV2HTTPEvent();

    @BeforeClass
    public static void createInput() throws IOException {
        // TODO: set up your sample input object here.
    	//APIGatewayV2HTTPEvent groups = new APIGatewayV2HTTPEvent();
    	groups.setBody("{\"groups\":\"1,2,1,1,1,2,1,3\"}");    	
    	//System.out.println("groups ="+groups);
    		   
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testLambdaFunctionHandler() throws IOException {
        LambdaFunctionHandler handler = new LambdaFunctionHandler();
        Context ctx = createContext();
               
        APIGatewayV2HTTPResponse output = handler.handleRequest(groups, ctx);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser parser = new JsonParser();
		JsonObject sizesJson = parser.parse("{\"sizes\":\"3,4,6,12\"}").getAsJsonObject();      
        System.out.println(gson.toJson(sizesJson)+"    "+output.getBody());
		Assert.assertEquals(gson.toJson(sizesJson),output.getBody());
        
    }
}
