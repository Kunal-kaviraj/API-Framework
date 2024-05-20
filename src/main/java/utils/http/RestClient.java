package utils.http;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.common.ExtentReporter;

import java.util.Map;

import static utils.common.CommonUtils.decryptPassword;

public class RestClient
{
	private static final String REQUEST_FOR = " request for : [";
	private static final String SUBMITTING = "Submitting ";
	private static final String SUBMITTING_REQUEST_FOR = "Submitting request for : [";
	public static FilterableRequestSpecification request;
	private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);
	static
	{
		request = (FilterableRequestSpecification) RestAssured.given();
		request.header("Content-Type", "application/json");
	}
	
	public RestClient()
	{
		request = null;
		request = (FilterableRequestSpecification) RestAssured.given();
		request.header("Content-Type", "application/json");
	}

	/**
	 * This function is responsible for performing http requests
	 * 
	 * @param endpoint    : e.g https://www.reques/us/v1/countries
	 * @param requestType : e.g RequestType.POST, RequestType.PUT, RequestType.GET
	 * @param body        : right now only json String
	 * @return api response
	 * @author: Kunal kaviraj
	 */
	@SuppressWarnings("unchecked")
	public static Response submitRequest(String endpoint, RequestType requestType, Object... body)
	{		
		LOGGER.info(SUBMITTING + requestType + REQUEST_FOR + endpoint + "]");
		Response response = null;

		switch (requestType)
		{
		case DELETE:
			if(body.length > 0)
				request.body((String) body[0]);
			response = request.log().all().delete(endpoint);
			response.then().log().all();
			break;
		case GET:
			if(body.length > 0)
				request.queryParams((Map<String, ?>) body[0]);
			
			response = request.log().all().relaxedHTTPSValidation().get(endpoint);
			response.then().log().all();
			break;
		case POST:
			if(body.length > 0)
				request.body((String) body[0]);
			response = request.log().all().relaxedHTTPSValidation().post(endpoint);
			response.then().log().all();
			break;
		case PUT:
			if(body.length > 0)
				request.body((String) body[0]);
			response = request.log().all().relaxedHTTPSValidation().put(endpoint);
			response.then().log().all();
			break;
		case PATCH:
			if(body.length > 0)
				request.body((String) body[0]);
			response = request.log().all().put(endpoint);
			response.then().log().all();
			break;
		default:
			break;
		}

		ExtentReporter.reportStep(SUBMITTING + requestType + REQUEST_FOR + endpoint + "]", "Info");
		return response;
	}

	/**
	 * This function is responsible for performing http requests for post method
	 * without body
	 * 
	 * @param endpoint    : e.g https://www.reques/us/v1/countries
	 * @param requestType : e.g RequestType.POST, RequestType.PUT, RequestType.GET
	 * @return api response
	 * @author: Kunal Kaviraj
	 */
	public static Response submitRequestWithoutBody(String endpoint, RequestType requestType)
	{
		LOGGER.info(SUBMITTING + requestType + REQUEST_FOR + endpoint + "]");
		Response response = null;

		switch (requestType)
		{
		case DELETE:
			response = request.log().all().relaxedHTTPSValidation().delete(endpoint);
			response.then().log().all();
			break;
		case GET:
			response = request.log().all().relaxedHTTPSValidation().get(endpoint);
			response.then().log().all();
			break;
		case POST:
			response = request.log().all().relaxedHTTPSValidation().post(endpoint);
			response.then().log().all();
			break;
		case PUT:
			response = request.log().all().relaxedHTTPSValidation().put(endpoint);
			response.then().log().all();
			break;
		case PATCH:
			response = request.log().all().relaxedHTTPSValidation().patch(endpoint);
			response.then().log().all();
			break;
		default:
			break;
		}

		ExtentReporter.reportStep(SUBMITTING + requestType + REQUEST_FOR + endpoint + "]", "Info");
		return response;
	}

	/**
	 * This function is responsible for performing http requests
	 * 
	 * @param endpoint    : e.g https://www.reques/us/v1/countries
	 * @param requestType : e.g RequestType.POST, RequestType.PUT, RequestType.GET
	 * @param body        : right now only json String
	 * @return api response
	 * @author: Kunal kaviraj
	 */
	public static Response submitRequestWithQuearyParam(String endpoint, RequestType requestType, Map<String, String> queryParam, Object... body)
	{
		Response response = null;
		request.queryParams(queryParam);

		switch (requestType)
		{
		case DELETE:
			response = request.log().all().delete(endpoint);
			response.then().log().all();
			break;
		case GET:
			response = request.log().all().relaxedHTTPSValidation().get(endpoint);
			response.then().log().all();
			break;
		case POST:
			request.body((String) body[0]);
			response = request.log().all().post(endpoint);
			response.then().log().all();
			break;
		case PUT:
			request.body((String) body[0]);
			response = request.log().all().put(endpoint);
			response.then().log().all();
			break;
		case PATCH:
			request.body((String) body[0]);
			response = request.log().all().put(endpoint);
			response.then().log().all();
			break;
		default:
			break;
		}

		ExtentReporter.reportStep(SUBMITTING + requestType + REQUEST_FOR + request.getURI() + "]", "Info");
		return response;
	}

	/**
	 * This function is responsible for performing http requests
	 *
	 * @param endpoint    : e.g https://www.reques/us/v1/countries
	 * @param requestType : e.g RequestType.POST, RequestType.PUT, RequestType.GET
	 * @param userName    : username for basic auth
	 * @param password    : password for basic auth
	 * @param body        : right now only json String
	 * @return api response
	 * @author: Kunal Kaviraj
	 */
	public static Response submitRequestWithAuth(String endpoint, RequestType requestType, String userName, String password, String secret, Object... body)
	{
		Response response = null;

		if(!(userName.isEmpty() && password.isEmpty()))
		{
			if(!secret.isEmpty())
				request.auth().preemptive().basic(userName, decryptPassword(password, secret));
			else
				request.auth().basic(userName, password);		
		}
		LOGGER.info(SUBMITTING + requestType + REQUEST_FOR + endpoint + "]");

		switch (requestType)
		{
		case DELETE:
			if(body.length > 0)
				request.body((String) body[0]);
			
			response = request.log().all().relaxedHTTPSValidation().delete(endpoint);
			response.then().log().all();
			break;
		case GET:
			if(body.length > 0)
				request.body((String) body[0]);
			
			response = request.log().all().relaxedHTTPSValidation().get(endpoint);
			response.then().log().all();
			break;
		case POST:
			if(body.length > 0)
				request.body((String) body[0]);
			
			response = request.log().all().relaxedHTTPSValidation().post(endpoint);
			response.then().log().all();
			break;
		case PUT:
			if(body.length > 0)
				request.body((String) body[0]);
			
			response = request.log().all().relaxedHTTPSValidation().put(endpoint);
			response.then().log().all();
			break;
		case PATCH:
			if(body.length > 0)
				request.body((String) body[0]);
			
			response = request.log().all().relaxedHTTPSValidation().patch(endpoint);
			response.then().log().all();
			break;
		default:
			break;
		}
		ExtentReporter.reportStep(SUBMITTING + requestType + REQUEST_FOR + request.getURI() + "]", "Info");
		return response;
	}

	/**
	 * This function is responsible for performing http requests
	 *
	 * @param endpoint    : e.g https://www.reques/us/v1/countries
	 * @param requestType : e.g RequestType.POST, RequestType.PUT, RequestType.GET
	 * @param body        : right now only json String
	 * @return api response
	 * @author: Kunal Kaviraj
	 */
	public static Response submitRequestWithToken(String endpoint, RequestType requestType, String password, String secret, Object... body)
	{
		Response response = null;
		if(!password.isEmpty())
		{
			String token = decryptPassword(password, secret);
			request.auth().oauth2(token);
		}
		else
			request.auth().none();
		
		LOGGER.info(SUBMITTING + requestType + REQUEST_FOR + endpoint + "]");

		switch (requestType)
		{
		case DELETE:
			request.body((String) body[0]);
			response = request.delete(endpoint);
			break;
		case GET:
			if(body.length > 0)
				request.body((String) body[0]);
			
			response = request.relaxedHTTPSValidation().get(endpoint);
			break;
		case POST:
			if(body.length > 0)
				request.body((String) body[0]);
			
			response = request.relaxedHTTPSValidation().post(endpoint);
			break;
		case PUT:
			if(body.length > 0)
				request.body((String) body[0]);
			
			response = request.relaxedHTTPSValidation().put(endpoint);
			break;
		case PATCH:
			if(body.length > 0)
				request.body((String) body[0]);
			
			response = request.relaxedHTTPSValidation().patch(endpoint);
			break;
		default:
			break;
		}
		ExtentReporter.reportStep(SUBMITTING + requestType + REQUEST_FOR + endpoint + "]", "Info");
		return response;
	}
}
