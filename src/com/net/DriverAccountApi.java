/***********************************************************************
 * Module:  DriverAccountAPI.java
 * Author:  hyq
 * Purpose: Defines the Interface DriverAccountAPI
 ***********************************************************************/
package com.net;

/**
 * 司机帐号相关操作的API接口
 * 类型描述: 包括司机、企业主、企业主子用户，收货方（暂不支持)。
 * </br>创建时期: 2014年12月22日
 * @author hyq
 */
public interface DriverAccountApi {
   /** 
    * 会员登陆:一个帐号同时只能在一台终端上登陆；
    * @return 返回一个JSON对象DriverLoginDTO，格式如：
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:{
    *  		token:令牌
    *  		memeber:{
    *      		phone:会员手机号
    *      		creditGrad 信誉等级
    *      		balance帐户余额
    *           memberId:成员ID
    *  		} 
    *  		driver:{
    *     		driverId主键
    *     		head头像URL
    *    		realName真实姓名
    *    		idCardNumber身份证号码
    *    		multiLocale常跑地
    *    		checked验证状态
    *    		audited审核状态
    *    		credit信誉度
    *    		qrCode二维码明片url
    *    		trucks:[{
    *           	PlateNumber车牌号码
    *           	truckLength车辆长度
    *           	weightCapacity可载吨位
    *           	cubageCapacity可载立方
    *           	truckType车型
    *           	head头像url
    *       	},...] 
    *     	}  
    * } 
    * @param name 
    * @param pwd
    * 登陆成功后，将帐号、司机信息及与之绑定车辆信息，信息完成度组成json返回 
    * 成功示例:{"data":{"token":"DCF042B12B70CA962954945A93A44AB8","driver":{"idCardNumber":"433022198112164315","trucks":[{"cubageCapacity":1.00,"head":"s","plateNumber":"123456","truckLength":1.00,"truckType":1,"weightCapacity":1.00}],"audited":1,"driverId":1,"qrCode":"11","realName":"111","credit":1,"multiLocale":"1","head":"1","checked":1},"memeber":{"balance":0.00,"phone":"18874833569","creditGrad":0}},"resultCode":1}
    * 
    * */
	String login(String name, String pwd);
	
   /** 
    * 司机帐号激活
    * 1，触发该操作的条件是，企业已经将司机及车辆加入平台；
    * 操作过程：  输入手机号，点激活，后跳到密码输入，点设置密码；
    * @param phone
    * @return  
    * {
    *   resultCode:1:发激活码成功，2：该帐号未注册,0:会话超时,需要重新登陆,-1:发送激活码失败 
    * 	data:{} 
    * */
   String active(String phone,String activecode);
   
   /** 
    * 激活时设置密码
    * @param phone 
    * @param pwd 
    * @return 
    * {
    *   resultCode:1:成功,0:会话超时,需要重新登陆,-1:操作失败 
    * 	data:{} 
    * 
    * 示例如：{"resultCode":1}
    * */
   String setPwd(String phone, String pwd);
   
   /** 
    * 会员注册
    * @param phone 电话
    * @param pwd 密码
    * @return 格式如：
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:{
    *   	token:‘会话令牌’
    *   }
    * }
    * 
    *  成功示例如：{"data":{"token":"22C32B961BFA63C282BC4836A60E184D"},"resultCode":1}
    */
   String regist(String phone, String pwd);
   
   /**
    * 完善司机信息资料
    * @param phone 电话 
    * @param driver 司机信息
    * {//字段类型见表tp_Driver
    *  	identifyCode,身份证号
    *  	name,真实姓名
    *  	citys: 常跑城市1,2,3,4
    *  	head:"头像URL“
    *   licenseType:驾照类型
    * }
    * @param truck 车辆信息：
    * {//字段类型见表tp_Truck
    *  	plateNumber，车牌号
    *  	truckType： 车型
    *  	truckLength:车长,
    *  	weightCapacity:可载吨位
    * }
    * @return 
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:{}
    * } 
    */
   String completeDriverInfo(String phone,String identifyCode ,String name ,String citys,
		   String head,String licenseType,String plateNumber,String truckType,String truckLength,String weightCapacity);
   
   /** 
    * 修改密码,
    * @param phone 
    * @return
     * {
    *   resultCode:1 :已经注册| 0:会话超时 | -1:操作失败|2:未注册 
    * 	data:{ }
    * }
    *  */
   String changePwd(int memberId,String oldpwd,String newpwd);
   
   /** 
    * 司机提请认证 ,平台会把验证码发短信到手机,
    * @param phone 手机号
    * @param code 验证码
    * 1:操作成功,0:该司机手机未注册，-1：操作失败 */
   String submitAuthenticated(String phone,String code);
   
   /**
    * 车辆信息注册
    * @param did  司机ID
    * @param plateNum 车牌号
    * @param type 车型
    * @param len 车长(cm)
    * @param ton 吨位
    * @return 
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败
    * 	data:{ }
    * } 
    * 成功示例如下： {"resultCode":1}
    */
   String addTruck(int did, String plateNum, int type, float len, float ton);
   
   /** 
    * 手机注册否 
    * @param phone
    * @return  
    * {
    *   resultCode:1 :已经注册| 0:会话超时 | -1:操作失败|2:未注册 
    * 	data:{ }
    * }
    * 
    * 成功示例 {"resultCode":1}
    * 失败示例：{"error":"该15243658123号码未注册!","resultCode":2}
    */
   String isRegisted(String phone);
   

   
   /**
    * 新增条件筛选器
    * @param mid
    * @param searcherDescript
    * @param searcherName
    * @param searcherType
    * @return 1:新增成功,0:会话超时,需要重新登陆,-1操作失败
    * 成功示例：{"resultCode":1}
    */
   String addSearcher(int mid, String searcherDescript,String searcherName,byte searcherType);
   
   /** 
    * 取得我的所有筛选器
    * @param mid 会员ID
    * @return 返回一个JSON格式筛选器JSON数组，格式如：
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:[{字段描述如tp_Searcher},...]
    * }
    *
    *
    *示例如下：{"data":[{"memberId":1,"searcherDescript":"111111","searcherId":1,"searcherName":"11111111","searcherType":1},{"memberId":1,"searcherDescript":"2222222","searcherId":2,"searcherName":"2222","searcherType":1}],"resultCode":1}
    */
   String getSearcherList(int mid);
   
   /** 
    * 取得筛选器详情
    * @param fid 筛选器ID
    * @return返回一个JSON格式筛选器对象， 格式如：
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:{
    * 	memberId:会员ID
    * 	searcherDescript:描述详情
    * 	searcherId:ID号
    * 	searcherName:查询名字
    * 	searcherType:查询类型
    * }
    * }
    * 
    * 成功示例:{"data":{"memberId":1,"searcherDescript":"111111","searcherId":1,"searcherName":"11111111","searcherType":1},"resultCode":1}
    */
   String getSearcherDetail(int fid);
   
   /** 
    * 取二维护码名片url
    * @param did 司机ID
    * @return 该名片所在的url 格式如：
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:{picUrl:"url"}
    * }
    */
   @Deprecated
   String getQrCode(int did);
   
   /** 
    * 生成二维码名片,据司机，车辆生成一张二维护码名片，存储并保存并反回url；
    * @param did 司机ID
    * @return 该名片所在的url 格式如：
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:{picUrl:"url"}
    * }
    */
   @Deprecated
   String  generateQrCode(int did);
   
   /** 
    * 取司机详情
    * @param did 司机ID
    * @return 返回一个json对象DriverDTO,格式如：
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:{
    *   	driverId:司机ID
    *    	name:真实姓名
    *    	pics:[url,url]证件照料
    *    	head:url 头像
    *    	multiLocale: 长沙,益阳,.. 常跑地
    *    	phone:电话
    *    	qrCode:二维码名片
    *    	truck:[{
    *       	head:url
    *       	len:长
    *      	 	ton:吨位
    *       	type:车型
    *    	}]
    * 	}
    * }
    * 
    * 示例如下：{"data":{"phone":"15243658123","pics":["123"],"truck":[{"ton":1.00,"len":1.00,"type":1,"head":"s"},{"ton":10.00,"len":10.00,"type":1,"head":null}],"name":"111","driverId":1,"qrCode":"11","multiLocale":"1","head":"1"},"resultCode":1}
    */
   String getDriverDetail(int did);
   
   /**
    * 刷企业货主的二维码,申请加入车队
    * @param sid 货主ID
    * @param did 司机ID
    * @return 1:新增成功,0:会话超时,需要重新登陆,-1操作失败  
    * 
    * 成功示例:{"resultCode":1}
    */
   String applyJoinShipper(int sid, int did);
   
   /** 
    * 查询司机加入的车队所有企业的列表；
    * @return 是一个json数组List<ShipperDTO>；格式如：
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:[{
    * memberId:会员帐号主键
    * simpleName:企业简称
    * fullName:企业全称
    * locationCode:所在地ID
    * detailAddress:详细地址
    * contact:联系人
    * phone:联系电话
    * introduce:企业简介
    * website:企业网址
    * cargoType:主要运送货品
    * qrCode:二维码明片
    * head:头像
    * auditStatus:认证状态
    * },...]
    * }
    * @param did 司机ID
    * 
    * 成功示例:{"data":[{"auditStatus":1,"cargoType":1,"contact":"1","detailAddress":"1","fullName":"11111","head":"1","introduce":"1","locationCode":"111","phone":"18874833569","qrCode":"1","shipperId":1,"simpleName":"111111","website":"1"}],"resultCode":1}
    */
   String getMyShipperList(int did);
   
   /** 
    * 取企业货主详情
    * @return 返回一个企业货主详情的JSON对象ShipperDTO；格式如：
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:{
    * memberId:会员帐号主键
    * simpleName:企业简称
    * fullName:企业全称
    * locationCode:所在地ID
    * detailAddress:详细地址
    * contact:联系人
    * phone:联系电话
    * introduce:企业简介
    * website:企业网址
    * cargoType:主要运送货品
    * qrCode:二维码明片
    * head:头像
    * auditStatus:认证状态
    * }
    * }
    * @param 企业货主id 
    * 
    * 成功示例:{"data":{"auditStatus":1,"cargoType":1,"contact":"1","detailAddress":"1","fullName":"11111","head":"1","introduce":"1","locationCode":"111","phone":"18874833569","qrCode":"1","shipperId":1,"simpleName":"111111","website":"1"},"resultCode":1}
    */
   String getShipperDetail(int sid);
   
   /**
    * 邀请好友,向给定的电话发出邀请短信；
    * @param phones 电话
    * @return n:成功邀请人数，0:会话超时,需要重新登陆,-1:操作失败 
    * {
    *   resultCode:n:成功邀请人数，0:会话超时,需要重新登陆,-1:操作失败 
    * 	data:{}
    * }
    * 成功示例:{"resultCode":2}
    */
   String inviteFriend(String phones);
   
   /*
    * 驾照，身份证正、反面，行驶证，车辆
    */
   
   //驾照，身份证正、反面，行驶证，车辆,新加一个接口，给点击证件照片时用
   /**
    * 上传司机的图像url
    * @param url 
    * @return 1:新增成功,0:会话超时,需要重新登陆,-1操作失败  
    * @return 
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * }
    * 成功示例:{"resultCode":1}
    */
   String updateDriverHead(int driverId, String url);
   
   /**
    *  上传司机身份证正面照
    * @param driverId 司机ID
    * @param url 图片url
    * @return 
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:memberImageId 生成的图片ID
    * }
    */
   String uploadDriverIdentityCardPositive(int driverId, String url);
   
   /**
    * 上传身份证反面照
    * @param driverId 司机ID
    * @param url
     * @return 
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:memberImageId 生成的图片ID
    * }
    */
   String uploadDriverIdentityCardOpposite(int driverId, String url);
   
   /**
    * 上传司机的驾照
    * @param driverId 司机ID
    * @param url 图片url
    * @return
    */
   String uploadDriverLicense(int driverId, String url);
   
   /**
    * 上传行使证
    * @param dtrId 司机与货车关联ID
    * @param url 图片url
    * @return 
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:memberImageId 生成的图片ID
    * }
    */
   String uploadDriverExerciseCard(int dtrId, String url);
   
   /**
    * 根据货车关联ID上传货车图片
    * @param dtrId  司机与货车关联ID
    * @param url 图片url
    * @return 
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:memberImageId 生成的图片ID
    * }
    */
   String uploadDriverTruck(int dtrId, String url);
   
   /**
    * 重新上传 司机的身份证正面照
    * @param memberImageId 图片ID
    * @param url
    * @return 
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	
    * }
    */
   String reuploadDriverIdentityCardPositive(int memberImageId, String url);
   
   /**
    * 重新上传司机的反面照
    * @param memberImageId 图片ID
    * @param url 图片url
    * @return 
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	
    * }
    */
   String reuploadDriverIdentityCardOpposite(int memberImageId, String url);
   
   /**
    * 重新上传司机的驾照
    * @param memberImageId 图片ID
    * @param url 图片url
    * @return 
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败
    * }
    */
   String reuploadDriverLicense(int memberImageId, String url);
   
   
   /**
    * 重行上传司机的行驶证照
    * @param memberImageId 图片ID
    * @param url 图片url
    * @return 
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	
    * }
    */
   String reuploadDriverExerciseCard(int memberImageId, String url);
   
   /**
    * 获得司机的证件照片其中包括身份证及驾驶证
    * @param driverId
    * @return
    */
   String getDriverPaperImags(int driverId);
   
   
   /**
    * 重新上传货车图片
    * @param memberImageId  图片ID
    * @param url 图片url
    * @return 
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:memberImageId 生成的图片ID
    * }
    */
   String reuploadDriverTruck(int memberImageId, String url);
   
   /**
    * 获得司机的货车图片及行驶证件照
    * @param driverId
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:{picUrl:"url"}
    * }
    */
   String getDriverTruckImags(int driverId);
   

   /**
    * 通过图片ID获得图片url
    * @param imageId
    * @return
    */
   public String getImgUrlById(int imageId);
   
   /**
    * 通过司机的
    * @param driverId
    * @return
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:[{"driverId":13,
    *   "plateNumber":"湘A.45897",
    *   "dtrId":14,
    *   "imageId":图片ID
    *   "url":图片ID
    * }...]
    * }
    * 示例数据{"data":[{"driverId":13,"plateNumber":"湘A.45897","dtrId":14},{"driverId":13,"plateNumber":"湘A.123457","dtrId":16}],"resultCode":1}

    */
   public String getTrucksByDriverId(int driverId);
}