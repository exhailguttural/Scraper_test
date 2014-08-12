import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLEditorKit;

public class Main {

	public static void main(String[] args) {
		StringReader r = null;
		try {
			URL ur = new URL("http://www.cnn.com");
			MyCallBack callBack = new MyCallBack();

			r = new StringReader(getURLContent(ur));
			HTMLEditorKit.Parser parser = new HTMLParse().getParser();
			parser.parse(r, callBack, true);
			
			Writer writer = null;
			try {
			    writer = new BufferedWriter(new OutputStreamWriter(
			    		new FileOutputStream("filename.txt"), "utf-8"));
			    writer.write(callBack.getStr());
			} catch (IOException ex) {
				System.out.println("1");
			} finally {
				try {writer.close();} catch (Exception ex) {}
			}
			
			String str = callBack.getStr();
		    Pattern p = Pattern.compile("Gaza");
		    Matcher m = p.matcher(str);
		    int count = 0;
		    while (m.find()){
		    	count++;
		    }
		    System.out.println(count);
		} catch (IOException ioex) {
			System.out.println("2");
		} finally {
			r.close();
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
}
