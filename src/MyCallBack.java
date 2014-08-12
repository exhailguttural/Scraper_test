import javax.swing.text.html.HTMLEditorKit;


public class MyCallBack extends HTMLEditorKit.ParserCallback {
	private String str = "";
	
	public void handleText(char[] data,int pos) {
		String b = new String(data);
//		str.concat(b);
		str += b;
//		System.out.println(str);
	}
	
	public String getStr() {
		return str;
	}
}
