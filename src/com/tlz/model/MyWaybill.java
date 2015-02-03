package com.tlz.model;

import com.tlz.utils.Flog;
import com.tlz.utils.TimeUtils;


/**
 * 我的运单
 * 
 */
public class MyWaybill  {
	/**
	 * 运单类型->日期  同年
	 */
	public static final  int TYPE_DATE_YEAR = 0;
	/**
	 * 运单类型->日期  同年月日
	 */
	public static final  int TYPE_DATE_SAME_DAY = 2;
	/**
	 * 运单类型->类容
	 */
	public static final int TYPE_CONTENT = 1;

	/**
	 * 当前类型
	 * 
	 * @see MyWaybill#TYPE_CONTENT
	 * @see MyWaybill#TYPE_DATE
	 */
	public int curType;
	/**
	 * 车牌
	 */
	public String licensePlate="湘A-VH016";
	/**
	 * 司机名字
	 */
	public String name = "张安满师傅";
	/**
	 * 司机头像
	 */
	public String icon;

	/**
	 * 发往地区 ->省
	 */
	public String toProvince = "湖南";
	/**
	 * 发往地区 ->市
	 */
	public String toCity = "长沙";
	/**
	 * 发往地区 ->区/县
	 */
	public String toDistrict = "天心区";

	/**
	 * 发货时间 年 
	 */
	public int year = 2014;
	/**
	 * 发货时间  月 
	 */
	public int month = 12;
	/**
	 * 发货时间 日
	 */
	public int day = 30;
	/**
	 * 装货城市
	 */
	public String fromCity = "长沙";

	/**
	 * 内容
	 */
	public String content="24吨，运费12000元，收货单位：太原神通";
	/**
	 * 时间戳/秒
	 */
	public long time=0;
	/**
	 * 自转日期  yyyy-MM-dd
	 */
	public String date;
	
	public MyWaybill(long time,int curType)
	{
		this.time=time;
		date=TimeUtils.getDateInString(time*1000);
		Flog.e(date);
		year=Integer.parseInt(date.substring(0, 4));
		month=Integer.parseInt(date.substring(4, 6));
		day=Integer.parseInt(date.substring(6, 8));
		this.curType=curType;
	}

}
