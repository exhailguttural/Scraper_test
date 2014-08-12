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

import javax.swing.text.html.HTMLEditorKit;

//import javax.swing.text.html.parser.Parser;


public class Main {

	public static void main(String[] args) {
		try {
			URL ur = new URL("http://www.cnn.com");
			String str = "";

			StringReader r = new StringReader(getURLContent(ur));
			HTMLEditorKit.Parser parser = new HTMLParse().getParser();
			parser.parse(r, new HTMLEditorKit.ParserCallback() {
				public void handleText(char[] data,int pos) {
					String b = new String(data);
					str.concat(b);
//					System.out.println(b);
				}
			}, true);
			
			Writer writer = null;
			try {
			    writer = new BufferedWriter(new OutputStreamWriter(
			    		new FileOutputStream("filename.txt"), "utf-8"));
//			    writer.append(str);
//			    writer.write("");
			    writer.write(str);
			} catch (IOException ex) {
				System.out.println("1");
			} finally {
				try {writer.close();} catch (Exception ex) {}
			}
		} catch (IOException ioex) {
			System.out.println("2");
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
