package main.core.beans;

import java.util.List;

public class keysSet {
	
	private String value;
	private String type;
	private String onMessage;
	private String offMessage;
	private List<String> toggle;
	private String effectedKey;
	private String url;
	private String options;
	private boolean visible;
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	private String displayName;
	private String toolTip;

	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOnMessage() {
		return onMessage;
	}

	public void setOnMessage(String onMessage) {
		this.onMessage = onMessage;
	}

	public String getOffMessage() {
		return offMessage;
	}

	public void setOffMessage(String offMessage) {
		this.offMessage = offMessage;
	}

	public List<String> getToggle() {
		return toggle;
	}

	public void setToggle(List<String> toggle) {
		this.toggle = toggle;
	}

	public String getEffectedKey() {
		return effectedKey;
	}

	public void setEffectedKey(String effectedKey) {
		this.effectedKey = effectedKey;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}





	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

}
