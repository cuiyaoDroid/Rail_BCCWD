package com.xzh.rail.bccwd.entry;

import java.io.Serializable;

public class JsonDataLogin extends JsonDataObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
