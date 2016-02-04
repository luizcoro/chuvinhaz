package com.example.luiz.chuvinhaz.network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by luiz on 12/28/15.
 */
public class HttpHandler {
	private static final String USER_AGENT = "Mozilla/5.0";

	private static final String crlf = "\r\n";
	private static final String twoHyphens = "--";
	private static final String boundary =  "*****";

    	// HTTP GET request
	public static String GET(String url) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

	// HTTP POST request
	public static String POST(String url, String user_id, byte[] file) throws Exception {


		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

		conn.setDoInput(true); // Allow Inputs
        conn.setDoOutput(true); // Allow Outputs
        conn.setUseCaches(false); // Don't use a Cached Copy
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
    //  conn.setRequestProperty("ENCTYPE", "multipart/form-data");
        conn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);


		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

		dos.writeBytes(twoHyphens + boundary + crlf);

        //Adding Parameter user_id

        dos.writeBytes("Content-Disposition: form-data; name=\"user_id\"" + crlf);
        dos.writeBytes(crlf);
        dos.writeBytes(user_id); // mobile_no is String variable
        dos.writeBytes(crlf);

        dos.writeBytes(twoHyphens + boundary + crlf);

		//Adding Parameter media file(audio,video and image)

        dos.writeBytes("Content-Disposition: form-data; name=\"file\"" + crlf);
        dos.writeBytes(crlf);
		dos.write(file);

        // send multipart form data necesssary after file data...
        dos.writeBytes(crlf);
        dos.writeBytes(twoHyphens + boundary + twoHyphens + crlf);

		InputStream responseStream = new BufferedInputStream(conn.getInputStream());

        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

        String line = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        responseStreamReader.close();

        String response = stringBuilder.toString();
        responseStream.close();
        conn.disconnect();
		return response;
	}
}
