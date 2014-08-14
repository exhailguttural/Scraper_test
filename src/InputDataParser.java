import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class InputDataParser {
	private String   urls;
	private String   searchWords;
	private String[] commands;
	private boolean  applyWCommand = false;
	private boolean  applyCCommand = false;
	private boolean  applyVCommand = false;
	private boolean  applyECommand = false;
	
	public InputDataParser(String[] inputData) {
		urls = inputData.length > 0 ? inputData[0] : null;
		searchWords = inputData.length > 1 ? inputData[1] : null;
		if (inputData.length > 2) {
			commands = Arrays.copyOfRange(inputData, 2, inputData.length);
			for (String command : commands) {
				if (!command.equals("-w") && !command.equals("-c") && !command.equals("-v") && !command.equals("-e")) {
					commands = null;
					break;
				}
			}
		} else {
			commands =null;
		}
		
		if (commands != null) {
			ArrayList<String> commandsList = new ArrayList<String>(Arrays.asList(commands));
			applyWCommand = commandsList.contains("-w");
			applyCCommand = commandsList.contains("-c");
			applyVCommand = commandsList.contains("-v");
			applyECommand = commandsList.contains("-e");
		}
	}
	
	public String[] getUrls() {
		ArrayList<String> urlList = new ArrayList<>();
		File urlFile = new File(urls);
		if (urlFile.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(urls)))
			{
				String currentUrl;
	 
				while ((currentUrl = br.readLine()) != null) {
					urlList.add(currentUrl);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		} else {
			urlList.add(urls);
		}
		return urlList.toArray(new String[urlList.size()]);
	}
	
	public String getSearchWords() {
		return searchWords;
	}
	
	public boolean applyWCommand() {
		return applyWCommand;
	}
	
	public boolean applyCCommand() {
		return applyCCommand;
	}
	
	public boolean applyVCommand() {
		return applyVCommand;
	}
	
	public boolean applyECommand() {
		return applyECommand;
	}
	
	public boolean chekInputData() {
		if (urls != null && searchWords != null && commands != null) {
			return true;
		} else {
			return false;
		}
	}
}
