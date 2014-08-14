import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLEditorKit;

public class Main {
	private static long timeScraping;
	private static long totalTimeScraping;
	private static long totalTimeProcessing;
	
	public static void main(String[] args) {
		InputDataParser entryDataParser = new InputDataParser(args);
		if (entryDataParser.chekInputData()) {
			count(entryDataParser);
		} else {
			System.out.println("invalid input");
		}
	}
	
	public static String getURLContent(URL url) throws IOException {
		long start = System.currentTimeMillis();
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
		timeScraping = System.currentTimeMillis() - start;
		return sb.toString();
	}
	
	public static void count(InputDataParser entryDataParser) {
		int totalCount = 0;
		int totalCharNumber = 0;
		
		String wordsToFindPattern = entryDataParser.getSearchWords().replace(", ", "|");
		Pattern wordPattern = Pattern.compile("[a-zA-Z0-9]+");
		Matcher wordMatcher = wordPattern.matcher(wordsToFindPattern);
		while (wordMatcher.find()){
			wordsToFindPattern = wordsToFindPattern.toString().replace(wordMatcher.group(), "\\b" + wordMatcher.group() + "\\b");
		}
		
		Pattern searchWordPattern = Pattern.compile(wordsToFindPattern, Pattern.CASE_INSENSITIVE);
		
		for (String url : entryDataParser.getUrls()) {
			System.out.println(url);
			try (StringReader r = new StringReader(getURLContent(new URL(url)))) {
				MyCallBack callBack = new MyCallBack();
				
				HTMLEditorKit.Parser parser = new HTMLParse().getParser();
				parser.parse(r, callBack.measureTime(), true);
				
				String stringFromUrl = callBack.getStr();
				
				if (entryDataParser.applyWCommand()) {
					Matcher searchWordMatcher = searchWordPattern.matcher(stringFromUrl);
					int count = 0;
					while (searchWordMatcher.find()){
						count++;
					}
					totalCount += count;
					System.out.println("number of words " + entryDataParser.getSearchWords() + ": " + count);
				}
				
				if (entryDataParser.applyCCommand()) {					
					totalCharNumber += stringFromUrl.length();
					System.out.println("total char number: " + stringFromUrl.length());
				}
				
				if (entryDataParser.applyVCommand()) {
					totalTimeScraping += timeScraping;
					totalTimeProcessing += callBack.getTimeProcessing();
					System.out.print("time spent scraping: " 
				                    + (double)(timeScraping/1000) 
				                    + " time spent processing: " 
				                    + (double)(callBack.getTimeProcessing()/1000));
				}
			} catch (IOException ioex) {
				System.out.println("IOException in count");
			}
			System.out.println();
		}
		System.out.println("TOTAL");
		if (entryDataParser.applyWCommand()) {
			System.out.println("number of words " + entryDataParser.getSearchWords() + ": " + totalCount);
		}
		if (entryDataParser.applyCCommand()) {
			System.out.println("total char number: " + totalCharNumber);
		}
		if (entryDataParser.applyVCommand()) {
			System.out.print("time spent scraping: " 
		                    + (double)(totalTimeScraping/1000) 
		                    + " time spent processing: " 
		                    + (double)(totalTimeProcessing/1000));
		}
	}
}
