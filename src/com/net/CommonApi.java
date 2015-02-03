/***********************************************************************
 * Module:  CommonApi.java
 * Author:  hyq
 * Purpose: Defines the Interface CommonApi
 ***********************************************************************/
package com.net;

import java.util.Date;

/**
 * 类型描述:一些公共的功能接口定义
 * </br>创建时期: 2014年12月22日
 * @author hyq
 */
public interface CommonApi {
   static final int DRIVER = 1;
   static final int SHIPPER = 0;
   
   /**司机,货主帐号手机验证*/
	 byte CAPTCHA_TYPE_PHONE_CHECK=1;
   
   /**
    * 取服务器当前时间
    * @return 返回格式
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    *   data:{ct:'2012-12-09 23:23:13'}
    * }
    */
   String getSystemCurrentTime();
   
   /**
    * 取得所有省份数据 
    * @return 返回一个json数组
    *{
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:[{
    *   	codeId:主键
    *   	areaName:名称   
    *   	fullCode:全编码 
    *   	fullName:全名称
    *       areaLevel:区域级别
    *       parentCode:上级编码
    *       areaCode:电话区号
    *       licenseHead:车牌字头
    *       longitude:经度
    *       latitude:纬度
    * 	},...]
    */
   String getAllProvince();
   
  /**
   * 取指定省份的所有地区市
   * @return 返回一个json数组{
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:[ {
    *   	codeId:主键
    *   	areaName:名称   
    *   	fullCode:全编码 
    *   	fullName:全名称
    *       areaLevel:区域级别
    *       parentCode:上级编码
    *       areaCode:电话区号
    *       licenseHead:车牌字头
    *       longitude:经度
    *       latitude:纬度
    * 	},...]
    *  
    */		    
   String getAllCity(String provinceId);
   
   /**
    * 取指定市的所有区县
    * @return 返回一个json数组
    *{
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:[{
    *   	codeId:主键
    *   	areaName:名称   
    *   	fullCode:全编码 
    *   	fullName:全名称
    *       areaLevel:区域级别
    *       parentCode:上级编码
    *       areaCode:电话区号
    *       licenseHead:车牌字头
    *       longitude:经度
    *       latitude:纬度
    * 	},...]
    *  
    */
   String getAllArea(String cityId);
   
   /**
    * 保存通话记录
    * @param callerNum  呼出号
    * @param recieverNum  接听号
    * @param callStatus 通话状态
    * @param callDate  通话时间
    * @param callLength 通话时长(单位秒)
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:{}
    * }
    */
   String saveCall(String callerNum, String recieverNum, boolean callStatus, Date callDate, int callLength);
  
   /** 
    * 查询通话记录
    * @param callNum 呼出号
    * @param stime 开始时间
    * @param etime 结束时间
    * @return 一个通话对象()的JSON数组
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:[{
    *    callNum:呼出方电话
    *    recieverNum:接听方电话
    *    callTime:通话时间
    *    callLength:通话时长(秒)
    *    callStatus:是否接通
    *    recordId:ID主键
    *    }，...]
    */
   String getCallList(String callNum, Date stime, Date etime);
  
   /**
    * 保险(未定)
    * @return
    */
   String insurance();
   
   /** 
    * 身份证认证
    * @param code 身份证号
    * @param name 姓名
    * @return 返回: 
    * {
    *   resultCode: 1:不存在, 2:相符, 3：不相符,0:会话超时,需重新登陆,-1:操作失败 
    * 	data:{ }
    * }
    * 
    * 
    */
   String identityCheck(String code, String name);
   
   /** 
    * 发送手机验证码 到客户端
    * @param phoneNum 手机号 
   	* @return
   	* {
    *   resultCode: 1:发送成功,0:会话超时,需重新登陆,-1:操作失败 
    * 	data:{激活码}
    * }
    */
   String  sendCaptcha(String phoneNum);
   /**
    * 验证手机码正确否
    * @param phoneNum  手机号
    * @param captcha  手机验证码
    * @param type 验证类型
    * @return @return json对象,格式如下 返回: 
    * {
    *   resultCode:1:不正确, 2:正确, 0:会话超时,需重新登陆,-1,操作失败
    * 	data:{}
    * }
    */
   String checkCaptcha(String phoneNum, String captcha, int type);
   
   /**
    *  获取当前最新版本号,客户端程序升级前调用
    * @param editioin 
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:{version:"版本号"}
    * }
    */
   String getEdition(String editioin);
   
   /**
    * 查取系统服务通知,返回最新的消息对象的json集合 
    * @return json对象,格式如下 
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:[{
    * 	messageId:消息ID
    * 	messageTitle:消息标题
    * 	messageContent:消息内容
    * 	messageTime:消息时间
    * 	messageWeight:消息权重
    * 	messageType:消息类型
    * 	messageCreator:消息创建者
   	*	},...]
    * }
    */
   String getServieMessages();
   
   /**
    * 图片上传
    * @param pic 图片
    * 平台所有图片的上传，都是调用该方法，
    * 该方法实现上传图片到服务器，并保存到数据，返回一个该图片在表中的主键
    * @return 返回该图片在表tp_MemberImages的ID值 
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:{picId:整型id值}
    * }
    */
   String uploadPic2Id(byte[] pic);
   
   /** 
    * 该方法不同一uploadPic2Id,他在表tp_MemberImages中不存在记录
    * @param pic 图片
    * @return 返回该图片的URL
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:{picUrl:'url'}
    * }
    */
   String uploadPic2Url(byte[] pic);
   
   /** 
    * 查得司机实时位置信息 
    * @param sid 货主ID
    * @param did 司机ID
    * 格式如：
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:{
    * 		driverId:司机ID,
    * 		lng:23.2323,
    * 		lat:43.343434
    *   } 
    *}
    */
   String whereAreYou(int sid, int did);

}