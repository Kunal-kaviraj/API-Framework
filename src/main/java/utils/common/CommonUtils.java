package utils.common;

import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * It will contain all basic common utilities.
 * 
 * @author  Kunal
 */
public class CommonUtils
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

	/**
	 * @Method_Name: createDirectory
	 * @Functionality: It will create a directory on provided path
	 * @param: directoryPath
	 * @author: Kunal
	 */
	public static void createDirectory(String directoryPath)
	{
		LOGGER.info("Creating directory : {}", directoryPath);
		File directory = new File(directoryPath);
		if(!directory.exists())
			directory.mkdirs();
	}

	/**
	 * @Method_Name: readFile
	 * @Functionality: It will read a file using provided inputStream
	 * @param: inputStream
	 * @return converted String
	 * @author: Kunal
	 */
	public static String readFile(InputStream inputStream)
	{
		System.out.println("CommonUtils >> [readFile]");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder out = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		String line;
		try
		{
			while ((line = reader.readLine()) != null)
			{
				out.append(line);
				out.append(newLine);
			}
		}
		catch (IOException e)
		{
			System.out.println("Exception occurred : " + e.getMessage());
			e.printStackTrace();
		}

		return out.toString();
	}

	/**
	 * @Method Name : getReqRawData
	 * @Functionality: This method use to retrevie the body from request file.
	 * @return String
	 * @param filePath
	 * @author Kunal
	 */
	public static String getReqRawData(String filePath)
	{
		StringBuilder out = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		String fileName = System.getProperty("user.dir") + filePath;
		try (FileInputStream fis = new FileInputStream(fileName))
		{
			int i = 0;
			do
			{
				byte[] buf = new byte[1024];
				i = fis.read(buf);
				String reqBody = new String(buf, StandardCharsets.UTF_8);
				out.append(reqBody);
				out.append(newLine);
			} while (i != -1);
			return out.toString();
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred while parsing or reading file : {}", e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public static void hardAssertion(boolean status, String message)
	{
		if(status)
		{
			ExtentReporter.reportStep(message, "Pass");
		}
		else
		{
			ExtentReporter.reportStep(message, "Fail");
			Assert.assertTrue(status);
		}
	}

	public static void assertEquals(String Expected, String Actual, String message)
	{
		if(Expected.trim().equalsIgnoreCase(Actual.trim()))
		{
			ExtentReporter.reportStep(message, "Pass");
			ExtentReporter.reportStep("Expected Text - " + Expected.trim() + " | Actual Text - " + Actual.trim(), "Pass");
		}
		else
		{
			ExtentReporter.reportStep("Expected Text - " + Expected.trim() + " | Actual Text - " + Actual.trim(), "fail");
			ExtentReporter.reportStep(message, "Fail");
//			Assert.assertTrue(false);
		}
	}


	public static void assertEquals(int value1, int value2, String message)
	{
		if(value1 == value2)
		{
			ExtentReporter.reportStep(message, "Pass");
			ExtentReporter.reportStep("Expected Text - " + value1 + " | Actual Text - " + value2, "Pass");
		}
		else
		{
			ExtentReporter.reportStep("Expected Text - " + value1 + " | Actual Text - " + value2, "fail");
			ExtentReporter.reportStep(message, "Fail");
//			Assert.assertTrue(false);
		}
	}

	/**
	 * @Method Name : encryptPassword
	 * @Functionality: Encrypts the Password string.
	 * @return String
	 * @param password
	 * @param secret
	 * @author Kunal
	 */
	public static String encryptPassword(String password, String secret)
	{
		try
		{
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
			SecretKeySpec key = new SecretKeySpec(secret.getBytes("UTF-8"), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(secret.getBytes("UTF-8")));
			byte[] encrBytes = cipher.doFinal(password.getBytes("UTF-8"));
			byte[] encoded = Base64.encodeBase64(encrBytes);
			return new String(encoded);
		}
		catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e)
		{
			System.err.println("Failure to encrypt password caused by:");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @Method Name : decryptPassword
	 * @Functionality: Decrypts the encrypted Password string.
	 * @return String
	 * @param password
	 * @param secret
	 * @author Kunal Kaviraj
	 */
	public static String decryptPassword(String password, String secret)
	{
		try
		{
			byte[] decoded = Base64.decodeBase64(password);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
			SecretKeySpec key1 = new SecretKeySpec(secret.getBytes("UTF-8"), "AES");
			cipher.init(Cipher.DECRYPT_MODE, key1, new IvParameterSpec(secret.getBytes("UTF-8")));
			return new String(cipher.doFinal(decoded), "UTF-8");
		}
		catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | UnsupportedEncodingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e)
		{
			System.err.println("Failure to decrypt password caused by:");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @Method Name : assertContains
	 * @Functionality: Asserts whether string contains a substring.
	 * @param : string1, message, subString
	 * @author : Kunal Kaviraj
	 */
	public static void assertContains(String string1, String subString, String message)
	{
		if(string1.trim().contains(subString.trim()))
		{
			ExtentReporter.reportStep("Actual Text - " + string1.trim() + " | Contains Text - " + subString.trim(), "Pass");
			ExtentReporter.reportStep(message, "Pass");
		}
		else
		{
			ExtentReporter.reportStep("Actual Text - " + string1.trim() + " | does not Contains Text - " + subString.trim(), "fail");
			ExtentReporter.reportStep(message, "Fail");
//			Assert.assertTrue(false);
		}
	}

	/**
	 * @Method Name : returnTimestamp
	 * @Functionality: return the current time stamp in est.
	 * @author Kunal Kaviraj
	 */
	public static String returnTimestamp()
	{
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH");
		sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		String timeStamp = sdf.format(date);
		return timeStamp;
	}

	/**
	 * @Method_Name : validateMapping
	 * @Functionality : return the current time stamp in est.
	 * @author : Kunal Kaviraj
	 * @param : response1, response2, jsonpath1, jsonPath2
	 */
	public static void validateMapping(Response response1, Response response2, String jsonpath1, String jsonPath2)
	{
		String[] value1 = jsonpath1.split("~");
		String[] value2 = jsonPath2.split("~");
		for (int i = 0; i < value1.length; i++)
		{
			String expected1 = response1.getBody().jsonPath().getString(value1[i]);
			String expected2 = response2.jsonPath().getString(value2[i]);
			assertEquals(expected1, expected2, "Verify mapping of value for attribute:" + value1[i] + " from Abstraction API is" + expected1 + " and attribute:" + value2[i] + " from  Product Integration API is " + expected2);
		}
	}

	/**
	 * @Method Name : validateSchema
	 * @Functionality: Validate Json/XML schema for a given response
	 * @author: Kunal Kaviraj
	 * @param: response, filepath
	 */
	public static void validateSchema(Response response, String filepath)
	{
		try
		{
			String responseBody = response.body().asString();
			if(filepath.substring(filepath.length() - 4, filepath.length()).contains("json"))
				assertThat(responseBody, JsonSchemaValidator.matchesJsonSchemaInClasspath(filepath));
			else
				assertThat(responseBody, RestAssuredMatchers.matchesXsdInClasspath(filepath));

			ExtentReporter.InfoStep("Validated Schema for :", responseBody);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ExtentReporter.InfoStep("Failed to validate Schema for the given response :", response.body().asString());
		}
	}
	
	
	/**
	 * @Method Name : verifyLink
	 * @Functionality: verify if a url/link is reachable or broken
	 * @author: Kunal Kaviraj
	 * @param: FileUrl 
	 */
	public static boolean verifyLink(String linkUrl)
	{
		Boolean broken = true;
		try
		{
			URL url = new URL(linkUrl);
			HttpURLConnection httpURLConnect = (HttpURLConnection) url.openConnection();
			httpURLConnect.setConnectTimeout(5000);
			httpURLConnect.connect();
			if(httpURLConnect.getResponseCode() >= 400)
			{
				System.out.println("HTTP STATUS - " + httpURLConnect.getResponseMessage() + "is a broken link");
			}
			else
			{
				System.out.println("HTTP STATUS - " + httpURLConnect.getResponseMessage());
				broken = false;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return broken;
	}
}
