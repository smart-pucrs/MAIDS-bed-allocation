package br.pucrs.smart.RV4JaCa;

import java.util.Date;
import java.util.HashMap;

public class MsgSent {

	private Date time;
	private String msgId;
	private String isReply;
	private String performative;
	private String sender;
	private String receiver;
	private HashMap<String, Object> content;
	private String verdict;
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getIsReply() {
		return isReply;
	}
	public void setIsReply(String isReply) {
		this.isReply = isReply;
	}
	public String getPerformative() {
		return performative;
	}
	public void setPerformative(String performative) {
		this.performative = performative;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public HashMap<String, Object> getContent() {
		return content;
	}
	public void setContent(HashMap<String, Object> content) {
		this.content = content;
	}
	public String getVerdict() {
		return verdict;
	}
	public void setVerdict(String verdict) {
		this.verdict = verdict;
	}
}
