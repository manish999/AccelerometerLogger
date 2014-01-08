package com.rampgreen.acceldatacollector;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;


/**
 * A customrequest class is used to create get and post data (Map or List<BasicNameValuePair> ) request 
 * to the network. 
 * 
 * @author Manish Pathak
 *
 * 
 */
public class CustomRequest extends Request<JSONObject> {

	private Listener<JSONObject> listener;
	private Map<String, String> params;
	private List<BasicNameValuePair> listParams; 

	public CustomRequest(String url, Map<String, String> params, 
			Listener<JSONObject> reponseListener, ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.listener = reponseListener;
		this.params = params;
	}

	public CustomRequest(int requestMethod,String url, Map<String, String> params, 
			Listener<JSONObject> reponseListener, ErrorListener errorListener){
		super(requestMethod, url, errorListener);
		this.listener = reponseListener;
		this.params = params;
	}

	public CustomRequest(int requestMethod, String url, List<BasicNameValuePair> params,
			Listener<JSONObject> responseListener, ErrorListener errorListener) {

		super(requestMethod, url, errorListener);
		this.listParams = params;
		this.listener = responseListener;
	}

	public CustomRequest(String url, List<BasicNameValuePair> params,
			Listener<JSONObject> responseListener, ErrorListener errorListener) {

		super(Method.GET, url, errorListener);
		this.listParams = params;
		this.listener = responseListener;
	}

	protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
		// 
		if(listParams != null) {
			params = new HashMap<String, String>();
			// Iterate through the params and add them to our HashMap
			for (BasicNameValuePair pair : listParams) {
				params.put(pair.getName(), pair.getValue());
			}
		}
		return params;

	};

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		// TODO Auto-generated method stub
		listener.onResponse(response);
	}

	/*Usage:
  Create Request Queue
 RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

	// Create List of BasicNameValuePairs
	ArrayList params = new ArrayList();
	params.add(new BasicNameValuePair("param1", "param1value"));
params.add(new BasicNameValuePair("param2", "param2value"));

// Create Request
CustomRequest request = new CustomRequest(Request.Method.POST, "http://example.com/api/resource", params,
                                              new Response.Listener() {
    @Override
    public void onResponse(JSONObject response) {
        // JSON Parsing
    }
}, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
        // Error handling
    }
});*/
}
