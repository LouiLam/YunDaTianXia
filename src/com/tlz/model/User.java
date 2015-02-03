package com.tlz.model;

public class User {
	private String id;
	private String userName;
	private String nickName;
	private String phoneNumber;
	private long totalTraffic;
	private long totalUsedTraffic;
	private long currentUsedTraffic;
	private long totalTime;
	private long remainingTime;
	private long totalUsedTime;
	private long currentUsedTime;
	private boolean mUserValide = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public long getTotalTraffic() {
		return totalTraffic;
	}

	public void setTotalTraffic(long totalTraffic) {
		this.totalTraffic = totalTraffic;
	}

	public long getTotalUsedTraffic() {
		return totalUsedTraffic;
	}

	public void setTotalUsedTraffic(long totalUsedTraffic) {
		this.totalUsedTraffic = totalUsedTraffic;
	}

	public long getCurrentUsedTraffic() {
		return currentUsedTraffic;
	}

	public void setCurrentUsedTraffic(long currentUsedTraffic) {
		this.currentUsedTraffic = currentUsedTraffic;
	}

	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public long getTotalUsedTime() {
		return totalUsedTime;
	}

	public void setTotalUsedTime(long totalUserdTime) {
		this.totalUsedTime = totalUserdTime;
	}

	public long getCurrentUsedTime() {
		return currentUsedTime;
	}

	public void setCurrentUsedTime(long currentUsedTime) {
		this.currentUsedTime = currentUsedTime;
	}
	
	public boolean isValideUser()
	{
		return mUserValide;
	}
	
	public void setUserValide(boolean valide)
	{
		mUserValide = valide;
	}

	public long getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(long remainingTime) {
		this.remainingTime = remainingTime;
	}

}
