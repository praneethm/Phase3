package main.core.mail;

import java.util.Date;

public class TemplateLvl1 implements INotificationTemplate {

	String template = "<html><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body>"
			+ "\n" + "\n" + "\n" + "This is an automatic notification of the MIP Connector&#39;s alert system.<br>\n"
			+ "There seems to be a problem while attempting to post transactions from<br> \n"
			+ "Abila MIP into Amplifund.<br>\n" + "<br>\n" + "The error message given by Amplifund was:<br>\n"
			+ "------------------------------<wbr>------------------------------<wbr>--------------<br>\n"
			+ "{{amplfndErr}}\n"
			+ "------------------------------<wbr>------------------------------<wbr>--------------<br>\n" + "<br>\n"
			+ "These transactions will be re-attempted the next time the MIP Connector <br>\n"
			+ "runs. There is _no immediate action required_ at this point. However, we<br>\n"
			+ "are listing the failed transaction below for your records.<br>\n" + "<br>\n"
			+ "Failed transactions:<br>\n"
			+ "------------------------------<wbr>------------------------------<wbr>--------------<br>\n"
			+ "<table style=\"width:100%\">\n"
			+ "<tr><th>Description</th><th>Grant Code</th><th>GL Code</th><th>Debit</th><th>Effective Date</th></tr>\n"
			+ "{{transactions}}" + "</table>\n"
			+ "------------------------------<wbr>------------------------------<wbr>--------------<br>\n" + "<br>\n"
			+ "Again, there is no immediate action required from you at this point; <br>\n"
			+ "however, if you believe this needs immediate attention, please do not<br>\n"
			+ "hesitate in contacting your assigned consultant for support on this<br>\n" + "issue.<br>\n" + "<br>\n"
			+ "Automated message by the MIP Connector.<br>\n" + "Sent on {{systmDt}}<br>\n" + "<br>\n"
			+ "== End of Message ==\n" + "\n" + "</body></html>";
	String transactions = "<tr><td>{{dsc}}</td><td>{{grnt}}</td><td>{{glc}}</td><td>{{deb}}</td><td>{{date}}</td></tr>\n";
	String subject = "[ MIP Connector ] | {{subject}} \n";

	private TemplateLvl1 addDate() {
		template = template.replace("{{date}}", new Date().toString());
		return this;
	}
	
	public TemplateLvl1 subject(String subject) {
	
		subject=subject.replace("{{subject}}", subject);
		return this;
	}

	public TemplateLvl1 addError(String error) {
		template = template.replace("{{amplfndErr}}", error);
		return this;
	}

	public TemplateLvl1 addTransaction(String dsc, String GC, String GL, String amount, String date) {
		String holder = transactions.replace("{{dsc}}", dsc);
		holder = holder.replace("{{grnt}", GC);
		holder = holder.replace("{{glc}}", GL);
		holder = holder.replace("{{deb}}", amount);
		holder = holder.replace("{{date}}", date);
		holder = holder + "{{transactions}}";
		template = template.replace("{{transactions}}", holder);
		return this;
	}

	public String getTemplate() {
		return template.replace("{{transactions}}", "");
	}

	public String getSubject() {
		return subject;
	}

	public static TemplateLvl1 build() {
		return new TemplateLvl1().addDate();
	}

}
