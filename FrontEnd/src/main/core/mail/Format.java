package main.core.mail;

import java.util.Date;

public class Format implements INotificationTemplate{

	String template = "<br>\n" + "This is an automatic notification of the MIP Connector&#39;s alert system.<br>\n"
			+ "There has been an event rasing an error in the system that has been <br>\n"
			+ "categorized as a <strong>technical level</strong> error.<br>\n" + "<br>\n"
			+ "The error ocurred on: {{date}} <br>\n"
			+ "Module: {{system}} <br>\n" + "<br>\n" + "Here is the error message:<br>\n"
			+ "------------------------------<wbr>------------------------------<wbr>--------------<br>\n" + "{{error}}<br>\n"
			+ "------------------------------<wbr>------------------------------<wbr>--------------<br>\n" + "<br>\n"
			+ "The development team has been notified of this error. If you think this is<br>\n"
			+ "something you can fix, please notify the development team that you are <br>\n"
			+ "working to solve this.\n" + "<br>\n"
			+ "If you have no idea what is causing this error, an this is happening at a <br>\n"
			+ "critical time, please get in touch with the development team immediately <br>\n"
			+ "to get technical support.<br>\n" + "<br>\n" + "For your reference, here is the stack trace:<br>\n"
			+ "------------------------------<wbr>------------------------------<wbr>--------------<br>\n" + "<pre>\n"
			+ "{{stack}}\n" + "</pre>\n"
			+ "------------------------------<wbr>------------------------------<wbr>--------------<br>\n" + "<br>\n"
			+ "Automated message by the MIP Connector.<br>\n" + "<br>\n" + "== End of Message ==\n" + "\n"
			+ "</body></html>";
	
	String subject="[ MIP Connector ] | Error - {{error}}";
	
	private Format() {
		
	}

	public String getTemplate() {
		return this.template;
	}


	public Format addSystem(String system) {
		template =template.replace("{{system}}", system);
		return this;
	}
	
	public Format addError(String error) {
		template =template.replace("{{error}}", error);
		return this;
	}
	public Format addStack(String stack) {
		template =template.replace("{{stack}}", stack);
		return this;
	}
	public Format addsubject(String error) {
		subject =subject.replace("{{error}}", error);
		return this;
	}
	
	private Format addDate() {
		template =template.replace("{{date}}", new Date().toString());
		return this;
	}
	

	public String getSubject() {
		return subject;
	}





	public static Format build() {
		return new Format().addDate();
	}

}
