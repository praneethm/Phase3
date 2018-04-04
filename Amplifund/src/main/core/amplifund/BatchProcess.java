package main.core.amplifund;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.main.business.Process;


import main.core.AMPLIFUND;
import main.core.mail.Format;
import main.core.mail.Mailing;
import main.core.mail.TemplateLvl2;

public class BatchProcess {

	public static void process() {
		Connection connection = AMPLIFUND.conn;
		PreparedStatement ps;
		ResultSet rs = null;
		System.out.println("starting batch process");
		StringBuffer message = new StringBuffer();
		boolean pass = true;
		boolean notify = false;
		TemplateLvl2 format =TemplateLvl2.build();
		try {
			//PL 
			ps = connection.prepareStatement("SELECT * FROM public.af_failed_transactions;");
			rs = ps.executeQuery();
			while (rs.next()) {
				String temp = rs.getString(1);
				System.out.println(temp);
				System.out.println(rs.getString(4));
				pass = Process.execute(rs.getString(1),rs.getString(6), rs.getString(7), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(1), true);
				if (!pass) {
					//message.append("\n " + " " + rs.getString(1) +" " + rs.getString(6) +" " + rs.getString(3) +" " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5));
					format.addTransaction(rs.getString(3), rs.getString(6), rs.getString(7), rs.getString(4), rs.getString(5));
					notify=true;
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		format.addError("Amplifund Batch process failed transactions").subject("Transactions Failed - ACTION REQUIRED");
		
		if(notify)
		Mailing.sendMail(format, "1");
		

	}

}
