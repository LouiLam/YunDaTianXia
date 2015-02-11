/***********************************************************************
 * Module:  MemberAccountAPI.java
 * Author:  hyq
 * Purpose: Defines the Interface MemberAccountAPI
 ***********************************************************************/
package com.net;

/**
 * 类型描述: 企业货主帐号相关的API
 * </br>创建时期: 2014年12月22日
 * @author hyq
 */
public interface ShipperAccountApi {
	/**企业货主超级管理员*/
	byte TYPE_SHIPPER=10;
	/**司机会员 */
	byte TYPE_DRIVER=2;
	/**企业货主管理员*/
	byte TYPE_MANAGER=11;
	/**收货单位会员 */
	byte TYPE_RECIEVER=3;
	
	/**未激活*/
	byte STATUS_NOACTIVED=0;
	/**已激法 */
	byte  STATUS_ACTIVED=1;
	/**锁定*/
	byte  STATUS_LOCKED=2;

	
   /** 
    * 会员登陆:一个帐号同时只能在一台终端上登陆；
    * @return 返回一个会员帐号+会员信息数据的组合JSON对象ShipperLoginDTO,格式如下:
    * {
    *   resultCode:1 :正确|-1:操作失败,0:用户名或密码有误
    * 	data:{
    *  		token:令牌
    *  		member:{
    *  			memberId:12,
    *      		phone:"会员手机号"
    *      		loginName:"登陆名"
    *      		creditGrad:(int)信誉等级
    *      		balance:(float)帐户余额
    *  		}, 
    *  		shipper:{
    *     		shipperId:(int)主键
    *     		head:"头像url"
    *     		auditStatus:(int)认证状态
    *     		locationCode :"所在地CODE"
    *     		simpleName:"企业简称"
    *     		fullName:"企业全称"
    *     		detailAddress:"详细地址"
    *     		contact:"联系人"
    *     		phone:"联系电话"
    *     		introduce:"企业简介"
    *     		cargoType:"主要运送货品"
    *     		qrCode:"二维码明片url"
    * 		} 
    * 	}
    * }
    * @param name 登陆名
    * @param pwd 密码
    * @param ct 客户端类型(0:pc,1:android,2:ios,3:winphone,4:其他)
    */
   String login(String name, String pwd,byte ct);
   
   
   /**
    * 
    * 企业会员注册
    * @param simpleName 企业简称
    * @param location :企业所在地code
    * @param pwd 密码
    * @return 返回一个json对象,格式如下;
    * {
    *   resultCode:1 :注册成功| -1:操作失败,
    * 	data:{
    * 		token:"会话令牌"
    * 		memberId:77
    *	} 
    * }
    */
   String regist(String simpleName,String loaction,String password);
   
   /**
    * 企业会员注册
    * @param simpleName 企业简称
    * @param location :企业所在地code
    * @param pwd 密码
    * @return 返回一个json对象,格式如下;
    * {
    *   resultCode:1 :注册成功| -1:操作失败,
    * 	data:{
    * 		token:"会话令牌"
    *	} 
    * }
    */
   String registpc(String simpleName,String loaction,String password,String phone,String activecode);
   
   /** 
    * 锁住该管理员帐号；暂停登陆
    * @param managerId 管理员ID
    * @return {resultCode:1锁定成功， -2:会话超时,需要重新登陆, -1：操作失败 }
    */
   String lockManager(int managerId);
   
   /**
    * 修改企业管理员
    * @param mid 会员ID
    * @param phone 手机号
    * @return {resultCode:1:修改成功 , -2:会话超时,需要重新登陆,-1:操作失败}
    */
   String updateManager(int mid,String phone);
  
   /** 
    * 新增企业管理员
    * @param sid 货主ID
    * @param name 姓名,会作为登陆名用
    * @paramp hone 手机号
    * @param pwd 密码
    * @return {resultCode:1:新增成功，2：该帐号已经存在， -2:会话超时,需要重新登陆,-1操作失败}
    */
   String addManager(int sid, String name,String phone,String pwd);
 
    /**
     * 完善企业货主资料
     * @param sid 企业ID
     * @param FullName 全名称
     * @param DetailAddress 详细地址
     * @param Contact 联系人
     * @param Introduce 企业简介
     * @param CargoType 主要运输货类
     * @return  {resultCode:1:完成功, -2:会话超时,需要重新登陆,-1:操作失败 }
     */
   String completeData(int sid, String fullName, String detailAddress,
			String contact,  String introduce, byte cargoType);
   
   
   /**
    * 上传营业执照料图
    * @param sid 企业ID
    * @param imageUrl 图片服务器中该图片的URL
    * @return {resultCode:平台图片的主键(memberImageId):成功, -2:会话超时,需要重新登陆,-1:操作失败 }
    */
   String uploadBusinessLicence(int sid,String imageUrl);
   
   /**
    * 上传营组织机构图
    * @param sid 企业ID
    * @param imageUrl 图片服务器中该图片的URL
    * @return {resultCode:平台图片的主键(memberImageId):成功, -2:会话超时,需要重新登陆,-1:操作失败 }
    */
   String uploadOrganizationNo(int sid,String imageUrl);
   
   /**
    * 上传税务登记号图
    * @param sid 企业IDID
    * @param imageUrl 图片服务器中该图片的URL
    * @return {resultCode:平台图片的主键(memberImageId):成功, -2:会话超时,需要重新登陆,-1:操作失败 }
    */
   String uploadTaxRegistNo(int sid,String imageUrl);

   
   
   /**
    * 重新上传营业执照料图
    * @param memberImageId 平台图片数据ID
    * @param imageUrl 图片服务器中该图片的URL
    * @return {resultCode:1:成功, -2:会话超时,需要重新登陆,-1:操作失败 }
    */
   String reuploadBusinessLicence(int memberImageId,String imageUrl);
   
   /**
    *  重新上传营组织机构图
    * @param memberImageId 平台图片数据ID
    * @param imageUrl 图片服务器中该图片的URL
    * @return {resultCode:1:成功, -2:会话超时,需要重新登陆,-1:操作失败 }
    */
   String reuploadOrganizationNo(int memberImageId,String imageUrl);
   
   /**
    *  重新上传税务登记号图
    * @param memberImageId 平台图片数据ID
    * @param imageUrl 图片服务器中该图片的URL
    * @return {resultCode:1:成功, -2:会话超时,需要重新登陆,-1:操作失败 }
    */
   String reuploadTaxRegistNo(int memberImageId,String imageUrl);
   
   
   /**
    *  上传及重新上传企业头像图
    * @param sid 企业ID
    * @param imageUrl 图片服务器中该图片的URL
    * @return {resultCode:1:成功, -2:会话超时,需要重新登陆,-1:操作失败 }
    */
   String reuploadHead(int sid,String imageUrl);  
   
   
   /**
    *  上传及重新上传企业头像图
    * @param sid 企业ID
    * @param imageUrl 图片服务器中该图片的URL
    * @return {resultCode:1:成功, -2:会话超时,需要重新登陆,-1:操作失败 }
    */
   String uploadHead(int sid,String imageUrl);  
   
   /** 
    * 重置密码
    * @param mid 会员帐号ID
    * @param newPassword 新密码 
    * @return  {resultCode:1:操作成功,2:手机号未注册, -2:会话超时,需要重新登陆,-1:操作失败 }
    */
   String resetPwd(int mid,String newPassword);
   
   /** 
    * 取企业详情
    * @param sid 企业货主
    * @return 返回企业货主详情json对象ShipperDTO,格式如下:
    * {
    *   resultCode:1 :正确 | 0:会话超时 | -1:操作失败,
    * 	data:{字段格式如表tp_Shipper:
    *   businesslicence:{},
    *   organizationno:{},
    *   taxregistno:{}
    * }
    * }{"data":
    * {"simpleName":"wow1","phone":null,"website":null,"taxregistno":null,"introduce":null,"qrCode":null,"contact":null,"detailAddress":null,"locationCode":"湖南省 长沙市 岳麓区","auditStatus":0,"organizationno":null,"cargoType":0,"serialVersionUID":1,"fullName":null,"head":null,"businesslicence":null,"shipperId":65},
    * "resultCode":1}

    */
   String getShipper(int sid);

   /**
    * 新增收货方
    * @param sid 企业货主ID
    * @param name 收货单位名称
    * @param addr 收货单位详细地址
    * @param contact 联系人
    * @param phone 联系电话
    * @param log 经度
    * @param lat 纬度
    * @return {resultCode: 1:新增成功，-2:会话超时,需要重新登陆,-1：新增失败 }
    */
   String addReciever(int sid, String name, String addr, String contact, String phone,double lon,double lat);
   
   /**
    * 删除收货方
    * @param rid 收货ID
    * @return{resultCode: 1：删除成功,-2:会话超时,需要重新登陆,-1：删除失败}
    */
   String delReciever(int rid);
   
   /** 
    * 修改收货方
    * @param rid 收货记录ID
    * @param name 收货单位名称
    * @param addr 收货单位详细地址
    * @param contact 联系人
    * @param phone 联系电话
    * @param log 经度
    * @param lat 纬度
    * @return {resultCode:1：删除成功,-2:会话超时,需要重新登陆,-1：删除失败 }
    */   
   String updateReciever(int rid, String name, String addr, String contact, String phone,double lon,double lat);
   
   /**
    * 修改密码时,验证原密码正确否 
    * @param mid 会员id
    * @param pwd 密码
    * @return {resultCode:1:正确,0:不正确,-2:会话超时,需要重新登陆,-1:操作失败} */
   String checkedPwd(int mid, String pwd);
   
   
   
   /** 
    * 企业货主帐号手机号验证
    * @param phone 手机号
    * @param captcha 验证码
    * @return  
    * {
    *   resultCode:1:验证成功，0：该帐号未注册,2:验证码不正确,-1:发生服务器异常 ,
    * }
    */
   String checkShipperPhonePass(int sid,String phone,String captcha);
   
   /** 
    * 企业货主帐号手机号验证
    * @param phone 手机号
    * @param captcha 验证码
    * @return  
    * {
    *   resultCode:1:验证成功，0：该帐号未注册,2:验证码不正确,-1:发生服务器异常 ,
    * }
    */
   String checkPhonePass(String phone,String captcha);
   
   /**
    * 根据图片ID获得所有url
    * @param imageId
    * @return
    * {
    *   resultCode:1：操作成功，0：超时,-1:发生服务器异常 
    *   data: url
    * }
    */
   String getImgUrlById(int imageId);
   
   /**
    * 根据图片ID获得所有url
    * @param imageId
    * @return
    * {
    *   resultCode:1：操作成功，0：超时,-1:发生服务器异常 
    *   data: [
    *   	{url:'url',id:13,type:'12(营业执照码)'},
    *   	...
    *   	}]
    * }
    */
   String getAllImgUrl(int sid);
   
}