package site.metacoding.red.util;

public class Script {

	// new 안해도 호출할 수 있게 static 붙임
	public static String back(String msg) { //String msg 메세지 띄워주기
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('"+msg+"');");
		sb.append("history.back();");	// history.back(); = 뒤로가기
		sb.append("</script>");
		return sb.toString();
	}
	
	public static String href(String url) {	// 메세지없이 이동만 하는것
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("location.href='"+url+"';");
		sb.append("</script>");
		return sb.toString();
	}
	
	public static String href(String url, String msg) {	// 경고창이 뜨고 해당주소로 이동
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('"+msg+"');");
		sb.append("location.href='"+url+"';");
		sb.append("</script>");
		return sb.toString();
	}
}
