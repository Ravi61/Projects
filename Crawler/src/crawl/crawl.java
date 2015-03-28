package crawl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.opencsv.CSVWriter;

public class crawl {

	public static void main(String[] args) throws IOException {
    	try{
    		
  	      	String google = "https://www.google.co.in/search?q=";
  		    // Enter your search string here
  	    	String search = "Your search string goes here";
  	    	String charset = "UTF-8";
  	    	String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
  	    	
  	    	int count = 1;
  	    	String[] result = new String[4];
  	    	String csv = "Results.csv";    //This csv file will contain your results
  	    	
  	    	boolean alreadyExists = new File(csv).exists();
  	    	
  		    CSVWriter writer = new CSVWriter(new FileWriter(csv,true), ',');
  		    if (!alreadyExists)
  			{
  		    	result[0] = "Title";
  		    	result[1] = "URL";
  		    	result[2] = "Host";
  		    	result[3] = "Description";
  				writer.writeNext(result);
  				result[0] = "";
  				result[1] = "";
  				result[2] = "";
  				result[3] = "";
  			}
  	    	
  	    	for(int i=0; i<5; i++)
  	    	{
  	    		String start = "&start="+(i*10);
  	    		Document doc = Jsoup.connect(google + URLEncoder.encode(search, charset) + start).userAgent(userAgent).get();
  	       
  	        	Elements lists = doc.select("li.g");
  	        	for(Element links : lists)
  	        	{
  	        		Elements abc = links.select("div.rc>h3.r>a");
  	        		
  	        		for (Element link : abc) {
  	        			System.out.println("Count: " + count);
  	        			String title = link.text();
  	        			String url = link.attr("href");
  	        			URI uri = new URI(url);
  	     	            String domain = uri.getHost();
  	        	   
  	     	            System.out.println("Title: " + title);
  	        			System.out.println("URL: " + url);
  	        			result[0] = title;
  	        			result[1] = url;
  	        			result[2] = domain;
  	        			Elements snip = links.select("div.s>div>span.st");
  	        			for(Element desc : snip)
  	        			{
  	        				String text = desc.text();
  	        				System.out.println("Snip : " + text);
  	        				result[3] = text;
  	        			}
  	        			count++;
  	        			writer.writeNext(result);
  	        			result[0] = "";
  	        			result[1] = "";
  	        			result[2] = "";
  	        			result[3] = "";
  	        		}
  	        	}

  	        	
  	    	}
  	    	
  	    	writer.close();
  	      	
  	      	}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}	
	}
	
}
