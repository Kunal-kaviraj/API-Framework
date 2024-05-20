package utils.http;

import com.google.gson.Gson;
import io.restassured.response.Response;

public class RestResponse<T> implements IRestResponse<T>
{
	private T data;
	private Response response;
	private Exception e;

	@SuppressWarnings({ "unchecked" })
	public RestResponse(Class<?> t, Response response)
	{
		this.response = response;
		try
		{
			this.data = (T) t.getConstructor().newInstance();;
		}
		catch (Exception e)
		{
			System.out.println(e.getStackTrace());
			throw new RuntimeException("No default constructor present in the Response POJO, please add a default one.");
		}
	}

	public String getContent()
	{
		return response.getBody().asString();
	}

	public int getStatusCode()
	{
		return response.getStatusCode();
	}

	public String getStatusDescription()
	{
		return response.getStatusLine();
	}

	public Response getResponse()
	{
		return response;
	}

	@SuppressWarnings("unchecked")
	public T getBody()
	{
		try
		{
			data = (T) response.getBody().as(data.getClass());
		}
		catch (Exception e)
		{
			System.out.println("Exception while modelling :" + e.getStackTrace());
			this.e = e;
		}
		return data;
	}

	@SuppressWarnings("unchecked")
	public T getResponseObject()
	{
		try
		{
			data = (T) new Gson().fromJson(response.toString().trim(), data.getClass());
		}
		catch (Exception e)
		{
			System.out.println("Exception while modelling :" + e.getStackTrace());
			this.e = e;
		}
		return data;
	}

	public Exception getException()
	{
		return e;
	}

}