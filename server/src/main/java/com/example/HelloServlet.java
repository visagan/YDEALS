package com.example;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.net.*;
import java.io.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;







import com.thoughtworks.xstream.XStream;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;






import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;

public class HelloServlet extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        ServletOutputStream out = resp.getOutputStream();
        
        Map map = new HashMap();  
       // XMLSerializer serializer = new XMLSerializer(); 
        XStream xstream = new XStream();
        
        String uid = req.getParameter("uid");
        String zip = req.getParameter("zip");
		String query = req.getParameter("query");
		
		map.put("ZIP", zip);
		map.put("UID", uid);
		map.put("QUERY", query);
        
        //out.write("Hello".getBytes());
        
       
        //zip= "61802" ;
        //query = "pizza";
        String encoding = "UTF-8";
        String hostname = "http://query.yahooapis.com/v1/public/yql?q=";
		String urlstring = "select * from local.search where zip='"+zip+"'and query='"+query+"'";
		URL yahoo = new URL(hostname + URLEncoder.encode(urlstring,encoding)+"&format=json");
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
		
		
        String yqlData = null,        inputLine;
        
        while ((inputLine = in.readLine()) != null)
        	yqlData += inputLine;
           // System.out.println(inputLine);
        in.close();
		//System.out.println(yqlData);

		yqlData = yqlData.replaceFirst("null", "");
		//System.out.println(yqlData);
		JSONObject json = (JSONObject) JSONSerializer.toJSON( yqlData );
		JSONArray localData = (JSONArray)json.getJSONObject("query").getJSONObject("results").getJSONArray("Result");
		
		//JSONObject jsonObject = JSONObject.fromObject( map);  
		//JSON json = JSONSerializer.toJSON( jsonObject.toString() ); 
	
		
		//String xml = xstream.toXML( jsonObject.toString() );  	
		
		
		
		
		String root = "YDEALS";
		DocumentBuilderFactory documentBuilderFactory =  DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		Document document = null;
		try {
					documentBuilder = documentBuilderFactory.newDocumentBuilder();
				
				  document = documentBuilder.newDocument();
				  Element rootElement = document.createElement(root);
				  document.appendChild(rootElement);
       // out.write(xml.getBytes());
				  
				  for (int i = 1; i <= 5; i++){
					 /* System.out.print("Enter the element: ");
					  String element = uid;
					  System.out.print("Enter the data: ");
					  String data = query;*/
					  Element em = document.createElement("DEALS");
					  rootElement.appendChild(em);
					  Element em1 = document.createElement("DEALNAME");
					  em1.appendChild(document.createTextNode("Dummy deal"));
					  em.appendChild(em1);
					  
					  Element em2 = document.createElement("COMPANYNAME");
					  em2.appendChild(document.createTextNode(localData.getJSONObject(i).getString("Title")));
					  em.appendChild(em2);
					  
					  Element em3 = document.createElement("RATING");
					  em3.appendChild(document.createTextNode(localData.getJSONObject(i).getJSONObject("Rating").getInt("AverageRating")+""));
					  em.appendChild(em3);
					  
					  Element em4 = document.createElement("ADDRESS");
					  em4.appendChild(document.createTextNode(localData.getJSONObject(i).getString("Address")));
					  em.appendChild(em4);
					  
					  Element em5 = document.createElement("CITY");
					  em5.appendChild(document.createTextNode(localData.getJSONObject(i).getString("City")));
					  em.appendChild(em5);
					  
					  Element em6 = document.createElement("STATE");
					  em6.appendChild(document.createTextNode(localData.getJSONObject(i).getString("State")));
					  em.appendChild(em6);
					  
					  Element em7 = document.createElement("PHONE");
					  em7.appendChild(document.createTextNode(localData.getJSONObject(i).getString("Phone")));
					  em.appendChild(em7);
					  
					  
					  Element em8 = document.createElement("LAT");
					  em8.appendChild(document.createTextNode(localData.getJSONObject(i).getString("Latitude")));
					  em.appendChild(em8);
					  
					  Element em9 = document.createElement("LONG");
					  em9.appendChild(document.createTextNode(localData.getJSONObject(i).getString("Longitude")));
					  em.appendChild(em9);
					  
					  Element em10 = document.createElement("IMAGE_URL");
					  String ImageUrl = getImageURL(localData.getJSONObject(i).getString("Url"));
					  em10.appendChild(document.createTextNode(ImageUrl));
					  em.appendChild(em10);
					  
					  Element em11 = document.createElement("DISTANCE");
					  em11.appendChild(document.createTextNode(localData.getJSONObject(i).getString("Distance")));
					  em.appendChild(em11);
					  
					  
					  
					  //em.appendChild(document.createTextNode(localData.getJSONObject(i).getString("Title")));
					  //em.appendChild(document.createTextNode(localData.getJSONObject(i).getString("Title")));
					  
					  }
					  TransformerFactory transformerFactory = 
					  TransformerFactory.newInstance();
					  Transformer transformer = transformerFactory.newTransformer();
					  DOMSource source = new DOMSource(document);
					  StreamResult result =  new StreamResult(out);
					  transformer.transform(source, result); 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
        //out.write(document.toString().getBytes());
        out.flush();
        out.close();
    }

	private String getImageURL(String url) {
		
		//System.out.println(url);
		
		String encoding = "UTF-8";
        String hostname = "http://query.yahooapis.com/v1/public/yql?q=";
		String urlstring = "select content from html where url=\""+ url + "\" and xpath='//span[@id=\"HDN_photodata\"]'";
		URL yahoo;
		try {
			yahoo = new URL(hostname + URLEncoder.encode(urlstring,encoding)+"&format=json");
		
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
		
		
        String yqlData = null,        inputLine;
        
        while ((inputLine = in.readLine()) != null)
        	yqlData += inputLine;
           // System.out.println(inputLine);
        in.close(); 
		//System.out.println(yqlData);
		
		yqlData = yqlData.replaceFirst("null", "");
		//System.out.println(yqlData);
		JSONObject json = (JSONObject) JSONSerializer.toJSON( yqlData );
		
		String span = json.getJSONObject("query").getJSONObject("results").getString("span");
		
		span = URLDecoder.decode(span,encoding);
		
		System.out.println( span);
		span = "{\"" + span.substring(3);
		
		System.out.println( span);
		span = span.substring(0,span.length()-1);
		
		//	span.replace(span.substring(span.length() -1 ), "");
		
		
		System.out.println( span);
		json = (JSONObject) JSONSerializer.toJSON( span );
		
		
		url = json.getJSONObject("results").getJSONArray("result").getJSONObject(0).getString("reg_img_url");
	
		//System.out.println(url);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			url = "http://www.eastsidefamilyhealth.com/images/stories/graphics/icons/yahoo.jpg";
		}
		return url;
		
		
	}
    
}
