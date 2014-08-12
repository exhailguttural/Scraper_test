import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.text.html.HTMLEditorKit;

//import javax.swing.text.html.parser.Parser;


public class Main {

	public static void main(String[] args) {
		try {
			URL ur = new URL("http://www.cnn.com");
			
//			Parser prsr = new Parser()
			  
			Writer writer = null;
			try {
			    writer = new BufferedWriter(new OutputStreamWriter(
			    		new FileOutputStream("filename.txt"), "utf-8"));
			    writer.append(getURLContent(ur));
			    writer.write("");
			    } catch (IOException ex) {
			    	//catch
			    	} finally {
			    		try {writer.close();} catch (Exception ex) {}
			    	}
			} catch (IOException ioex) {
				//catch
			}
	}
	
	public static /*CharSequence*/String getURLContent(URL url) throws IOException {
		URLConnection conn = url.openConnection();
		String encoding = conn.getContentEncoding();
		if (encoding == null) {
			encoding = "ISO-8859-1";
		}
		BufferedReader br = new BufferedReader(new
				InputStreamReader(conn.getInputStream(), encoding));
		StringBuilder sb = new StringBuilder(16384);
		try {
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
		        sb.append('\n');
		    }
		} finally {
			br.close();
		}
		return sb.toString();
		//return sb;
		}
	
	class HTMLParse extends HTMLEditorKit
	{
	  public HTMLEditorKit.Parser getParser()
	  {
	    return super.getParser();
	  }
	}
}
