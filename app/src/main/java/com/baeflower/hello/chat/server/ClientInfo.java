package com.baeflower.hello.chat.server;

import java.io.DataOutput;
import java.io.Serializable;

public class ClientInfo implements Serializable {
	private String nickName;
	//private DataOutput output;
	
	public ClientInfo(String nickName, DataOutput output) {
		this.nickName = nickName;
		//this.output = output;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
//	public DataOutput getOutput() {
//		return output;
//	}
//	public void setOutput(DataOutput output) {
//		this.output = output;
//	}
}
