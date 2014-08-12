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
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLEditorKit;

public class Main {
	
	public static void main(String[] args) {
		count(readUrl("urls.txt"), "Gaza", "-w", "-c");
// 		try { 			
// 			String[] arr = Arrays.copyOfRange(args, 2, args.length);
// 			count(readUrl(args[0]), args[1], arr);
// 		} catch (IllegalArgumentException e) { 			
// 		
// 		}
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
	
	public static void count(ArrayList<String> urls, String wordToFind, String... commands) {
		int totalCount = 0;
		int totalCharNumber = 0;
		boolean printOccurrence = false;
		boolean printCharNumber = false;
		for (String command : commands) {
			switch (command) {
			case "-w":
				printOccurrence = true;
				break;
			case "-c":
				printCharNumber = true;
				break;
			case "-e":
				break;
			}
		}
		
		for (String url : readUrl("urls.txt")) {
			try (StringReader r = new StringReader(getURLContent(new URL(url)))) {
				MyCallBack callBack = new MyCallBack();
				
				HTMLEditorKit.Parser parser = new HTMLParse().getParser();
				parser.parse(r, callBack, true);
				
				try (Writer writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("filename.txt"), "utf-8"))) {
					writer.write(callBack.getStr());
				} catch (IOException ex) {
					System.out.println("1");
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
				printInfo(url, wordToFind, count, str.length(), printOccurrence, printCharNumber);
			} catch (IOException ioex) {
				System.out.println("IOException in count");
			}
		}
		printInfo("TOTAL", wordToFind, totalCount, totalCharNumber, printOccurrence, printCharNumber);
	}
	
	public static void printInfo(String title
							   , String word
							   , int wordCount
							   , int charNumber
							   , boolean printOccurrence
							   , boolean printCharNumber) {
		StringBuilder sb = new StringBuilder();
		System.out.println(title);
		if (printOccurrence) {			
			sb.append("word " + word +" occurrences: " + wordCount + " ");
		}
		if (printCharNumber) {			
			sb.append("total number of characters " + charNumber);
		}
		System.out.println(sb);
	}
}
