import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLEditorKit;

public class Main {
	
	public static void main(String[] args) {
		String[] a = {"Gaza", "who"};
		String[] b = {"-w", "-c"};
		count(readUrl("urls.txt"), a, b);
		
//		int firstCommandIndex = 2; //2 because first parameter is url and at least one parameter (second) is word to find
//		for (int i = firstCommandIndex; i < args.length; i++) {
//			if (args[i].startsWith("-")) {
//				firstCommandIndex = i;
//				break;
//			}
//		}
//		
//		try {			
//			String[] soughtWordsArray = Arrays.copyOfRange(args, 1, firstCommandIndex - 1);
//			String[] comandArray = Arrays.copyOfRange(args, firstCommandIndex, args.length);
//			count(readUrl(args[0]), soughtWordsArray, comandArray);
//		} catch (IndexOutOfBoundsException e) {
//			
//		}
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
	
	public static String[] readUrl(String str) {
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
		return urlList.toArray(new String[urlList.size()]);
	}
	
	public static void count(String[] urls, String[] wordsToFind, String[] commands) {
		int totalCount = 0;
		int totalCharNumber = 0;
		boolean applyWCommand = Arrays.asList(commands).contains("-w");
		boolean applyCCommand = Arrays.asList(commands).contains("-c");
		boolean applyVCommand = Arrays.asList(commands).contains("-v");
		boolean applyECommand = Arrays.asList(commands).contains("-e");
		
		StringBuilder words = new StringBuilder();
		for (int i = 0; i < wordsToFind.length; i++) {
			words.append(wordsToFind[i]);
			if (i != wordsToFind.length - 1) {
				words.append(" | ");
			}
		}
		
		for (String url : urls) {
			System.out.println(url);
			try (StringReader r = new StringReader(getURLContent(new URL(url)))) {
				MyCallBack callBack = new MyCallBack();
				
				HTMLEditorKit.Parser parser = new HTMLParse().getParser();
				parser.parse(r, callBack, true);
				
				String str = callBack.getStr();
				
				if (applyWCommand) {					
					Pattern p = Pattern.compile(words.toString());
					Matcher m = p.matcher(str);
					int count = 0;
					while (m.find()){
						count++;
					}
					totalCount += count;
					words.toString().replace(" | ", ", ");
					System.out.println("number of words " + words.toString().replace(" | ", ", ") + ": " + count);
				}
				
				if (applyCCommand) {					
					totalCharNumber += str.length();
					System.out.print("total char number: " + str.length());
				}
			} catch (IOException ioex) {
				System.out.println("IOException in count");
			}
			System.out.println();
		}
		System.out.println("TOTAL");
		if (applyWCommand) {
			System.out.println("number of words " + words.toString().replace(" | ", ", ") + ": " + totalCount);
		}
		if (applyWCommand) {
			System.out.print("total char number: " + totalCharNumber);
		}
	}
}
