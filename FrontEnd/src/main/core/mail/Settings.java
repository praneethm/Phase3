package main.core.mail;

import main.core.MasterBean;

public class Settings {

	public static String getPort() {
		return port;
	}

	public static void setPort(String port) {
		Settings.port = port;
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		Settings.host = host;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		Settings.user = user;
	}

	public static String getPass() {
		return pass;
	}

	public static void setPass(String pass) {
		Settings.pass = pass;
	}

	public static String getLVL1_NOTIFICATION() {
		return LVL1_NOTIFICATION;
	}

	public static void setLVL1_NOTIFICATION(String lVL1_NOTIFICATION) {
		LVL1_NOTIFICATION = lVL1_NOTIFICATION;
	}

	public static String getLVL2_NOTIFICATION() {
		return LVL2_NOTIFICATION;
	}

	public static void setLVL2_NOTIFICATION(String lVL2_NOTIFICATION) {
		LVL2_NOTIFICATION = lVL2_NOTIFICATION;
	}

	public static String getFrom() {
		return from;
	}

	public static void setFrom(String from) {
		Settings.from = from;
	}

	private static String port;
	private static String host;
	private static String user;
	private static String pass;
	private static String LVL1_NOTIFICATION;
	private static String LVL2_NOTIFICATION;
	private static String from;

	public static void updateSettings() {
		
		MasterBean.getSettings();
		port=MasterBean.getSettingsHolder().get("MAILING").get("PORT").getValue();
		host=MasterBean.getSettingsHolder().get("MAILING").get("HOST").getValue();
		user=MasterBean.getSettingsHolder().get("MAILING").get("USER").getValue();
		pass=MasterBean.getSettingsHolder().get("MAILING").get("PASSWORD").getValue();
		LVL1_NOTIFICATION=MasterBean.getSettingsHolder().get("MAILING").get("LVL1_NOTIFICATION").getValue();
		LVL2_NOTIFICATION=MasterBean.getSettingsHolder().get("MAILING").get("LVL2_NOTIFICATION").getValue();
		from=MasterBean.getSettingsHolder().get("MAILING").get("FROM").getValue();
		System.out.println(MasterBean.getSettingsHolder().get("MAILING").get("HOST").getValue());

	}

}
