import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLEditorKit;

public class Main {
	
	public static void main(String[] args) {
		count(readUrl("urls.txt"), "Gaza");
	}
	
	public static String getURLContent(URL url) throws IOException {
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
	}
	
	public static ArrayList<String> readUrl(String str) {
		ArrayList<String> urlList = new ArrayList<>();
		if (str.endsWith(".txt")) {
			try (BufferedReader br = new BufferedReader(new FileReader(str)))
			{
				String sCurrentLine;
	 
				while ((sCurrentLine = br.readLine()) != null) {
					urlList.add(sCurrentLine);
				}
	 
			} catch (IOException e) {
				e.printStackTrace();
			} 
		} else {
			urlList.add(str);
		}
		return urlList;
	}
	
	public static void count(ArrayList<String> urls, String wordToFind) {
		int totalCount = 0;
		int totalCharNumber = 0;
		
		for (String url : readUrl("urls.txt")) {
			StringReader r = null;
			try {
				URL ur = new URL(url);
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
				Pattern p = Pattern.compile(wordToFind);
				Matcher m = p.matcher(str);
				int count = 0;
				while (m.find()){
					count++;
				}
				totalCount += count;
				totalCharNumber += str.length();
				printInfo(url, wordToFind, count, str.length(), true, true);
			} catch (IOException ioex) {
				System.out.println("IOException in count");
			} finally {
				if (r != null) {				
					r.close();
				}
			}
		}
		printInfo("TOTAL", wordToFind, totalCount, totalCharNumber, true, true);
	}
	
	public static void printInfo(String title
							   , String word
							   , int wordCount
							   , int charNumber
							   , boolean printOccurrence
							   , boolean printCharNumber) {
		System.out.println(title);
		if (printOccurrence) {			
			System.out.print("word " + word +" occurrences: " + wordCount + " ");
		}
		if (printCharNumber) {			
			System.out.println("total number of characters " + charNumber);
		}
	}
}
