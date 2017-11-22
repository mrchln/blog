package com.flchy.blog.inlets.enums;

public enum Keys {
	
	IS_SEND_MAIL_ADMIN("IS_SEND_MAIL_ADMIN","评论后是否发送邮件给管理员"),
	ADMIN_MAIL("ADMIN_MAIL","管理员邮件"),
	COMMENT_MAIL_NOTIFICATION_CONTENT("COMMENT_MAIL_NOTIFICATION_CONTENT","评论邮件通知内容"),
	COMMENT_MAIL_NOTIFICATION_TITLE("COMMENT_MAIL_NOTIFICATION_TITLE","评论邮件通知标题"),
	COMMENT_MAIL_NOTIFICATION_ADMIN_CONTENT("COMMENT_MAIL_NOTIFICATION_ADMIN_CONTENT","评论邮件通知管理员内容"),
	COMMENT_MAIL_NOTIFICATION_ADMIN_TITLE("COMMENT_MAIL_NOTIFICATION_ADMIN_TITLE","评论邮件通知标题"),
	WEBSITE_URL("WEBSITE_URL","网站地址"),
	WEBSITE_NAME("WEBSITE_NAME","网站名称"),
	IS_COMMENT_VERIFY("IS_COMMENT_VERIFY","是否开启评论审核"),
	;
	
	private String key;
	
	private String configName;
	
	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	private Keys(String key, String configName) {
		this.key = key;
		this.configName = configName;
	}

	
	
}
