package api.http.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiHttpClient {

	private static Logger logger = LoggerFactory.getLogger(ApiHttpClient.class);

	public static String reqPostMethod(NameValuePair[] nvp, String urlPath) throws Exception {

		return reqPostMethod(nvp, urlPath, 3000, 3000, 2, false);
	}

	public static String reqPostMethod(NameValuePair[] nvp, String urlPath, int soTime, int connectionTime, int retryCount, boolean blnRetry) throws Exception {

		PostMethod method = null;
		InputStream inStream = null;
		// Object obj = null;
		String strResultMethod = null;

		// Send
		try {
			// Set HttpClient
			HttpClient client = new HttpClient();
			// Set socket Time out
			client.getHttpConnectionManager().getParams().setSoTimeout(soTime);
			// Set connect Time out
			client.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTime);
			// Set URL
			method = new PostMethod(urlPath);

			// Set the Parameters.
			if(nvp != null) {
				method.addParameters(nvp);				
			}

			int statusCode = client.executeMethod(method);

			if (statusCode >= 400 && statusCode < 500) {
				logger.error(urlPath + " 400 resultCode over = " + statusCode);
			} else if (statusCode >= 500) {
				logger.error(urlPath + " 500 resultCode over = " + statusCode);
			} else {
				strResultMethod = method.getResponseBodyAsString();
			}

		} catch (ConnectException we) {
			logger.error(urlPath + " API ConnectException:", we);
		} catch (SocketTimeoutException we) {
			logger.error(urlPath + " API SocketTimeoutException:", we);
		} catch (ConnectTimeoutException we) {
			logger.error(urlPath + " API SocketTimeoutException:", we);
		} catch (UnknownHostException we) {
			logger.error(urlPath + " API UnknownHostException:", we);
		} catch (NoRouteToHostException we) {
			logger.error(urlPath + " API NoRouteToHostException:", we);
		} catch (NullPointerException we) {
			logger.error(urlPath + " API NullPointerException:", we);
		} catch (Exception e) {
			logger.error(urlPath + " API Exception: ekey=", e);
		} finally {
			if (inStream != null) {
				inStream.close();
			}
			if (method != null) {
				method.releaseConnection();
			}
		}

		return strResultMethod;
	}

	public static byte[] reqPostInBodyStringJsonMethod(String body, String urlPath, Header[] header) throws Exception {
		return reqPostInBodyMethod(body, urlPath, null, "application/json; charset=utf-8", "utf-8", 5000, 5000, 2, false);
	}

	public static byte[] reqPostInBodByteyJsonMethod(byte[] body, String urlPath, Header[] header) throws Exception {
		return reqPostInBodyMethod(body, urlPath, null, "application/json; charset=utf-8", null, 5000, 5000, 2, false);
	}

	public static byte[] reqPostInBodyStringJsonMethod(String body, String urlPath, Header[] header, int soTime, int connectionTime) throws Exception {
		return reqPostInBodyMethod(body, urlPath, null, "application/json; charset=utf-8", "utf-8", soTime, connectionTime, 2, false);
	}

	public static byte[] reqPostInBodyMethod(Object body, String urlPath, Header[] headers, String contentType, String charSet, int soTime, int connectionTime, int retryCount, boolean blnRetry) throws Exception {

		PostMethod method = null;
		InputStream inStream = null;
		// Object obj = null;
		byte[] strResultMethod = null;

		// Send
		try {
			// Set HttpClient
			HttpClient client = new HttpClient();
			// Set socket Time out
			client.getHttpConnectionManager().getParams().setSoTimeout(soTime);
			// Set connect Time out
			client.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTime);
			// Set URL
			method = new PostMethod(urlPath);

			if(headers != null) {
				for(int i=0; i < headers.length; i++) {
					method.addRequestHeader(headers[i]);	
				}
			}

			if (body instanceof byte[]) {
				method.setRequestEntity(new ByteArrayRequestEntity((byte[]) body, contentType));
		    } else if (body instanceof String) {
				method.setRequestEntity(new StringRequestEntity((String) body, contentType, charSet));
		    } else {
		        final byte[] buffer = SerializationUtils.serialize((Serializable) body);
		        method.setRequestEntity(new ByteArrayRequestEntity(buffer));
		    }

			int statusCode = client.executeMethod(method);

			if (statusCode >= 400 && statusCode < 500) {
				logger.error(urlPath + " 400 resultCode over = " + statusCode);
			} else if (statusCode >= 500) {
				logger.error(urlPath + " 500 resultCode over = " + statusCode);
			} else {
				strResultMethod = method.getResponseBody();
			}

		} catch (ConnectException we) {
			logger.error(urlPath + " API ConnectException:", we);
		} catch (SocketTimeoutException we) {
			logger.error(urlPath + " API SocketTimeoutException:", we);
		} catch (ConnectTimeoutException we) {
			logger.error(urlPath + " API SocketTimeoutException:", we);
		} catch (UnknownHostException we) {
			logger.error(urlPath + " API UnknownHostException:", we);
		} catch (NoRouteToHostException we) {
			logger.error(urlPath + " API NoRouteToHostException:", we);
		} catch (NullPointerException we) {
			logger.error(urlPath + " API NullPointerException:", we);
		} catch (Exception e) {
			logger.error(urlPath + " API Exception: e=", e);
		} finally {
			if (inStream != null) {
				inStream.close();
			}
			if (method != null) {
				method.releaseConnection();
			}
		}

		return strResultMethod;
	}
	public static String reqHeaderPostMethod(NameValuePair[] nvp, Header header, String urlPath) throws Exception {

		return reqHeaderPostMethod(nvp, header, urlPath, 3000, 3000, 2, false);
	}

	public static String reqHeaderPostMethod(NameValuePair[] nvp, Header header, String urlPath, int soTime, int connectionTime, int retryCount, boolean blnRetry) throws Exception {

		PostMethod method = null;
		InputStream inStream = null;
		// Object obj = null;
		String strResultMethod = null;

		// Send
		try {
			// Set HttpClient
			HttpClient client = new HttpClient();
			// Set socket Time out
			client.getHttpConnectionManager().getParams().setSoTimeout(soTime);
			// Set connect Time out
			client.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTime);
			// Set URL
			method = new PostMethod(urlPath);
			if(header != null) {
				method.setRequestHeader(header);				
			}
			// Set the Parameters.
			if(nvp != null) {
				method.addParameters(nvp);				
			}

			int statusCode = client.executeMethod(method);

			if (statusCode >= 400 && statusCode < 500) {
				logger.error(urlPath + " 400 resultCode over = " + statusCode);
				strResultMethod = method.getResponseBodyAsString();
			} else if (statusCode >= 500) {
				logger.error(urlPath + " 500 resultCode over = " + statusCode);
				strResultMethod = method.getResponseBodyAsString();
			} else {
				strResultMethod = method.getResponseBodyAsString();
			}

		} catch (ConnectException we) {
			logger.error(urlPath + " API ConnectException:", we);
		} catch (SocketTimeoutException we) {
			logger.error(urlPath + " API SocketTimeoutException:", we);
		} catch (ConnectTimeoutException we) {
			logger.error(urlPath + " API SocketTimeoutException:", we);
		} catch (UnknownHostException we) {
			logger.error(urlPath + " API UnknownHostException:", we);
		} catch (NoRouteToHostException we) {
			logger.error(urlPath + " API NoRouteToHostException:", we);
		} catch (NullPointerException we) {
			logger.error(urlPath + " API NullPointerException:", we);
		} catch (Exception e) {
			logger.error(urlPath + " API Exception: ekey=", e);
		} finally {
			if (inStream != null) {
				inStream.close();
			}
			if (method != null) {
				method.releaseConnection();
			}
		}

		return strResultMethod;
	}

	public static String reqHeadersPostMethodRpc(String jsonBody, Map<String, String> requestHeaders, String urlPath) throws Exception {

		return reqHeadersPostMethodRpc(jsonBody, requestHeaders, urlPath, 3000, 3000, 2, false);
	}

	public static String reqHeadersPostMethodRpc(String jsonBody, Map<String, String> requestHeaders, String urlPath, int soTime, int connectionTime, int retryCount, boolean blnRetry) throws Exception {

		PostMethod method = null;
		InputStream inStream = null;
		// Object obj = null;
		String strResultMethod = null;

		// Send
		try {
			// Set HttpClient
			HttpClient client = new HttpClient();
			// Set socket Time out
			client.getHttpConnectionManager().getParams().setSoTimeout(soTime);
			// Set connect Time out
			client.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTime);
			// Set URL
			method = new PostMethod(urlPath);
			// Set headers
			for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
				method.addRequestHeader(entry.getKey(), entry.getValue());
			}
			// Set the XML body
			RequestEntity requestEntity = new StringRequestEntity(jsonBody.toString(), "application/json; charset=utf-8", "utf-8");

			method.setRequestEntity(requestEntity);

			int statusCode = client.executeMethod(method);

			if (statusCode >= 400 && statusCode < 500) {
				logger.error(urlPath + " 400 resultCode over = " + statusCode);
				strResultMethod = method.getResponseBodyAsString();
			} else if (statusCode >= 500) {
				logger.error(urlPath + " 500 resultCode over = " + statusCode);
				strResultMethod = method.getResponseBodyAsString();
			} else {
				strResultMethod = method.getResponseBodyAsString();
			}

		} catch (ConnectException we) {
			logger.error(urlPath + " API ConnectException:", we);
		} catch (SocketTimeoutException we) {
			logger.error(urlPath + " API SocketTimeoutException:", we);
		} catch (ConnectTimeoutException we) {
			logger.error(urlPath + " API SocketTimeoutException:", we);
		} catch (UnknownHostException we) {
			logger.error(urlPath + " API UnknownHostException:", we);
		} catch (NoRouteToHostException we) {
			logger.error(urlPath + " API NoRouteToHostException:", we);
		} catch (NullPointerException we) {
			logger.error(urlPath + " API NullPointerException:", we);
			System.out.println(urlPath + " API NullPointerException:"+we);
		} catch (Exception e) {
			logger.error(urlPath + " API Exception: ekey=", e);
			System.out.println(urlPath + " API Exception:"+e);
		} finally {
			if (inStream != null) {
				inStream.close();
			}
			if (method != null) {
				method.releaseConnection();
			}
		}

		return strResultMethod;
	}
	
	public static String reqGetMethod(NameValuePair[] nvp, String urlPath) throws Exception {

		return reqGetMethod(nvp, urlPath, 3000, 3000, 2, false);
	}

	public static String reqGetMethod(NameValuePair[] nvp, String urlPath, int soTime, int connectionTime) throws Exception {

		return reqGetMethod(nvp, urlPath, soTime, connectionTime, 2, false);
	}

	public static String reqGetMethod(NameValuePair[] nvp, String urlPath, int soTime, int connectionTime, int retryCount, boolean blnRetry) throws Exception {

		GetMethod method = null;
		InputStream inStream = null;
		// Object obj = null;
		String strResultMethod = null;

		// Send
		try {
			// Set HttpClient
			HttpClient client = new HttpClient();
			// Set socket Time out
			client.getHttpConnectionManager().getParams().setSoTimeout(soTime);
			// Set connect Time out
			client.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTime);
			// Set URL
			method = new GetMethod(urlPath);
			// Set Head
			method.setRequestHeader("Content-Type", "text/xml;charset=utf-8");
			method.addRequestHeader("Connection", "close");

			// Set Cookie Policy
			method.getParams().setCookiePolicy(CookiePolicy.RFC_2109);

			// Retry to set, default is false.
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(retryCount, blnRetry));

			if (nvp != null) {
				// Set param
				method.setQueryString(nvp);				
			}

			int statusCode = client.executeMethod(method);

			if (statusCode >= 400 && statusCode < 500) {
				logger.error(urlPath + " 400 resultCode over = " + statusCode);
			} else if (statusCode >= 500) {
				logger.error(urlPath + " 500 resultCode over = " + statusCode);
			} else {
				
				strResultMethod = method.getResponseBodyAsString();

			}

		} catch (ConnectException we) {
			logger.error(urlPath + " API ConnectException:", we);
		} catch (SocketTimeoutException we) {
			logger.error(urlPath + " API SocketTimeoutException:", we);
		} catch (ConnectTimeoutException we) {
			logger.error(urlPath + " API SocketTimeoutException:", we);
		} catch (UnknownHostException we) {
			logger.error(urlPath + " API UnknownHostException:", we);
		} catch (NoRouteToHostException we) {
			logger.error(urlPath + " API NoRouteToHostException:", we);
		} catch (NullPointerException we) {
			logger.error(urlPath + " API NullPointerException:", we);
		} catch (Exception e) {
			logger.error(urlPath + " API Exception: ekey=", e);
		} finally {
			if (inStream != null) {
				inStream.close();
			}
			if (method != null) {
				method.releaseConnection();
			}
		}

		return strResultMethod;
	}

	public static String reqGetMethodAsStream(NameValuePair[] nvp, String urlPath) throws Exception {
		return reqGetMethodAsStream(nvp, urlPath, 3000, 3000, 2, false);
	}

	public static String reqGetMethodAsStream(NameValuePair[] nvp, String urlPath, int soTime, int connectionTime, int retryCount, boolean blnRetry) throws Exception {

		GetMethod method = null;
		InputStream inStream = null;
		// Object obj = null;
		String strResultMethod = null;

		// Send
		try {
			// Set HttpClient
			HttpClient client = new HttpClient();
			// Set socket Time out
			client.getHttpConnectionManager().getParams().setSoTimeout(soTime);
			// Set connect Time out
			client.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTime);
			// Set URL
			method = new GetMethod(urlPath);
			// Set Head
			method.setRequestHeader("Content-Type", "text/json;charset=utf-8");
			method.addRequestHeader("Connection", "close");

			// Set Cookie Policy
			method.getParams().setCookiePolicy(CookiePolicy.RFC_2109);

			// Retry to set, default is false.
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(retryCount, blnRetry));

			if (nvp != null) {
				// Set param
				method.setQueryString(nvp);
			}

			int statusCode = client.executeMethod(method);

			if (statusCode >= 400 && statusCode < 500) {
				logger.error(urlPath + " 400 resultCode over = " + statusCode);
			} else if (statusCode >= 500) {
				logger.error(urlPath + " 500 resultCode over = " + statusCode);
			} else {
				inStream = method.getResponseBodyAsStream();
				Writer writer = new StringWriter();
				char[] buffer = new char[1024];
				try {
					Reader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));

			        int n = -1;
			        while((n = reader.read(buffer)) != -1) {
			        	writer.write(buffer, 0, n);
			        }
	            } finally {
	            	inStream.close();
	            }
		        strResultMethod = writer.toString();
			}

		} catch (Exception e) {
			logger.error(urlPath + " API Exception: ekey=", e);
		} finally {

			if (method != null) {
				method.releaseConnection();
			}
		}

		return strResultMethod;
	}

    /**
     * Authondicate a Access Token.
     * 
     * @param  NameValuePair
     *         Parameters for the Oauth Api.
     * @param  String
     *         The Oauth Api's URL
     * @param  String
     *         accessToken
     * @throws  Exception
     *          If a executed error.
     * @return  String
     * 			a Ameba Id
     * 
     * @since  1.6
     */
	public static String reqOauthGetMethod(NameValuePair[] nvp, String urlPath, String accessToken) throws Exception {
		return reqOauthGetMethod(nvp, urlPath, accessToken , 3000, 3000, 3, false);
	}

	public static String reqOauthGetMethod(NameValuePair[] nvp, String urlPath, String accessToken, int soTime, int connectionTime, int retryCount, boolean blnRetry) throws Exception {

		GetMethod method = null;
		InputStream inStream = null;
		// Object obj = null;
		String strResultMethod = null;

		// Send
		try {
			// Set HttpClient
			HttpClient client = new HttpClient();
			// Set socket Time out
			client.getHttpConnectionManager().getParams().setSoTimeout(soTime);
			// Set connect Time out
			client.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTime);
			// Set URL
			method = new GetMethod(urlPath);
			// Set Head
			// method.setRequestHeader("Content-Type", "text/xml;charset=utf-8");
			method.setRequestHeader("Authorization", accessToken);

			// Set Cookie Policy
			// method.getParams().setCookiePolicy(CookiePolicy.RFC_2109);

			// Retry to set, default is false.
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(retryCount, blnRetry));

			// Set param
			method.setQueryString(nvp);

			int statusCode = client.executeMethod(method);

			if (statusCode >= 400 && statusCode < 500) {
				logger.error(urlPath + " 400 resultCode over = " + statusCode);
				throw new Exception();
			} else if (statusCode >= 500) {
				logger.error(urlPath + " 500 resultCode over = " + statusCode);
				throw new Exception();
			} else {
				strResultMethod = method.getResponseBodyAsString();
			}

		} catch (ConnectException we) {
			logger.error(urlPath + " API ConnectException:", we);
		} catch (SocketTimeoutException we) {
			logger.error(urlPath + " API SocketTimeoutException:", we);
		} catch (ConnectTimeoutException we) {
			logger.error(urlPath + " API SocketTimeoutException:", we);
		} catch (UnknownHostException we) {
			logger.error(urlPath + " API UnknownHostException:", we);
		} catch (NoRouteToHostException we) {
			logger.error(urlPath + " API NoRouteToHostException:", we);
		} catch (NullPointerException we) {
			logger.error(urlPath + " API NullPointerException:", we);
		} finally {
			if (inStream != null) {
				inStream.close();
			}
			if (method != null) {
				method.releaseConnection();
			}
		}

		return strResultMethod;
	}

	
}
