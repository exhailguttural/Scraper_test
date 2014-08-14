import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;


public class MyCallBack extends HTMLEditorKit.ParserCallback {
	private String str = "";
	private long timeProcessing;
	private long startTimeProcessing;
	
	public MyCallBack measureTime() {
		startTimeProcessing = System.currentTimeMillis();
		return this;
	}
	
	public void handleText(char[] data,int pos) {
		String string = new String(data);
		str += string;
	}
	
	public String getStr() {
		return str;
	}
	
	public void handleEndTag(HTML.Tag t,int pos) {
		if (t.equals(HTML.Tag.HTML)) {
			timeProcessing = System.currentTimeMillis() - startTimeProcessing;
		}
	}
	
	public long getTimeProcessing() {
		return timeProcessing;
	}
}
