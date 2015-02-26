package com.tlz.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;

public class WaybillNews implements Parcelable {
	public static final int TYPE_SYS=-1;
	public static final int TYPE_USER=0;
	/**
	 * 名字
	 */
	public String name="王尼玛";
	/**
	 * 头像
	 */
	public String icon;
	
	/**
	 * 消息内容
	 */
	public String content="这是一个很深奥的问题";
	
	/**
	 * 消息时间
	 */
	public String time="今天 11:30";

	/**
	 * 开往
	 */
	public String target="往：长沙";
	/**
	 * 消息类型
	 */
	public int type=0;
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(icon);
		dest.writeString(content);
		dest.writeString(time);
		dest.writeString(target);
	}
	
	public static final Parcelable.Creator<WaybillNews> CREATOR = ParcelableCompat
			.newCreator(new ParcelableCompatCreatorCallbacks<WaybillNews>() {

				@Override
				public WaybillNews createFromParcel(Parcel in, ClassLoader loader) {
					WaybillNews result = new WaybillNews();
					result.name = in.readString();
					result.icon = in.readString();
					result.content = in.readString();
					result.time = in.readString();
					result.target= in.readString();
					return result;
				}

				@Override
				public WaybillNews[] newArray(int size) {
					return new WaybillNews[size];
				}
	});
}
