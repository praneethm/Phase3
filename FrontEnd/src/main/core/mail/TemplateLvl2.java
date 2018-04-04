package main.core.mail;

import java.util.Date;

public class TemplateLvl2 implements INotificationTemplate {

	String template = "<html><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body>\n"
			+ "\n" + "This is an automatic notification of the MIP Connector&#39;s alert system.<br>\n"
			+ "There seems to be a problem while attempting to post transactions from<br> \n"
			+ "Abila MIP into Amplifund.<br>\n" + "<br>\n" + "The error message given by Amplifund was:<br>\n"
			+ "------------------------------<wbr>------------------------------<wbr>--------------<br>\n"
			+ "{{amplfndErr}}\n<br>"
			+ "------------------------------<wbr>------------------------------<wbr>--------------<br>\n" + "<br>\n"
			+ "These transactions will be re-attempted the next time the MIP Connector <br>\n"
			+ "runs. There is <strong>no immediate action required</strong> at this point. However, we<br>\n"
			+ "are listing the failed transaction below for your records.<br>\n" + "<br>\n"
			+ "Failed transactions:<br>\n"
			+ "------------------------------<wbr>------------------------------<wbr>--------------<br>\n"
			+ "<table style=\"width:100%\">\n"
			+ "<tr><th>Description</th><th>Grant Code</th><th>GL Code</th><th>Debit</th><th>Effective Date</th></tr>\n"
			+ "{{transactions}}\n" + "</table>\n"
			+ "------------------------------<wbr>------------------------------<wbr>--------------<br>\n" + "<br>\n"
			+ "Again, there is no immediate action required from you at this point; <br>\n"
			+ "however, if you believe this needs immediate attention, please do not<br>\n"
			+ "hesitate in contacting your assigned consultant for support on this<br>\n" + "issue.<br>\n" + "<br>\n"
			+ "Automated message by the MIP Connector.<br>\n" + "Sent on {{systmDt}}<br>\n" + "<br>\n"
			+ "== End of Message ==\n" + "\n" + "</body></html>";
	String transactions = "<tr><td>{{dsc}}</td><td>{{grnt}}</td><td>{{glc}}</td><td>{{deb}}</td><td>{{date}}</td></tr>\n";
	String subject = "[ MIP Connector ] | {{subject}}\n";

	public boolean hasTransactions = false;

	private TemplateLvl2 addDate() {
		template = template.replace("{{systmDt}}", new Date().toString());
		return this;
	}

	public TemplateLvl2 subject(String subject) {

		this.subject = this.subject.replace("{{subject}}", subject);
		return this;
	}

	public static TemplateLvl2 build() {
		return new TemplateLvl2().addDate();
	}

	public String getSubject() {
		return subject;
	};

	public String getTemplate() {
		return template.replace("{{transactions}}", "");
	};

	public TemplateLvl2 addError(String error) {
		template = template.replace("{{amplfndErr}}", error);
		return this;
	}

	public TemplateLvl2 addTransaction(String dsc, String GC, String GL, String amount, String date) {
		String holder = transactions.replace("{{dsc}}", dsc);
		holder = holder.replace("{{grnt}}", GC);
		holder = holder.replace("{{glc}}", GL);
		holder = holder.replace("{{deb}}", amount);
		holder = holder.replace("{{date}}", date);
		holder = holder + "{{transactions}}";
		template = template.replace("{{transactions}}", holder);
		return this;
	}

}
