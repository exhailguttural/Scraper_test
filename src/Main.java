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
	
	public static void main(String[] args) {
//		EntryDataParser entryDataParser = new EntryDataParser(args);
		String[] argsq = {"urls.txt", "Gaza, who", "-w", "-c"};
		InputDataParser entryDataParser = new InputDataParser(argsq);
		if (entryDataParser.chekInputData()) {
			count(entryDataParser);
		} else {
			System.out.println("invalid input");
		}
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
	
	public static void count(InputDataParser entryDataParser) {
		int totalCount = 0;
		int totalCharNumber = 0;
		
		String wordsToFindPattern = entryDataParser.getSearchWords().replace(", ", " | ");
		
		for (String url : entryDataParser.getUrls()) {
			System.out.println(url);
			try (StringReader r = new StringReader(getURLContent(new URL(url)))) {
				MyCallBack callBack = new MyCallBack();
				
				HTMLEditorKit.Parser parser = new HTMLParse().getParser();
				parser.parse(r, callBack, true);
				
				String str = callBack.getStr();
				
				if (entryDataParser.applyWCommand()) {					
					Pattern p = Pattern.compile(wordsToFindPattern);
					Matcher m = p.matcher(str); //составить регулярку чтоб определял слова только окруженные пробелами
					int count = 0;
					while (m.find()){
						count++;
					}
					totalCount += count;
					System.out.println("number of words " + entryDataParser.getSearchWords() + ": " + count);
				}
				
				if (entryDataParser.applyCCommand()) {					
					totalCharNumber += str.length();
					System.out.print("total char number: " + str.length());
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
			System.out.print("total char number: " + totalCharNumber);
		}
	}
}
