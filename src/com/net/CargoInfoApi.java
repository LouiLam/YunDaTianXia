/**************************************************
 * Module:  CargoInfoAPI.java
 * Author:  hyq
 * Purpose: Defines the Interface CargoInfoAPI
 **************************************************/
package com.net;

/**
 * 
 * 类型描述：分布货源API定义；
 * </br>创建时期: 2014年12月22日
 * @author hyq
 */
public interface CargoInfoApi {
   /** 发布中 */
   static final int STATUS_PUBLISHING = 0;
   /** 已签约 */
   static final int STATUS_CONTRACTED = 1;
   /** 已过期 */
   static final int STATUS_EXPIRED = 2;
   /**
    * 已到货
    */
   static final int status_Arraved=3;
   /**所有*/
   static final int STATUS_ALL = -1;
   
   /**
    * 装货单位:件
    */
   static final String UNIT_PIECE="件";
   /**
    * 装货单位:吨
    */
   static final String UNIT_TON="吨";
   /**
    * 装货单位:立方
    */
   static final String UNIT_CUBE="立方";
   
   /**
    * 发布货源信息,如果有图片，记得带上图片的ID
    * @param sid 企业货主ID
    * @param startAddr 装货地ID
    * @param destinationAdd 卸货地ID
    * @param unit 单位(件,立方,吨) 见UNIT_开头的常量
    * @param count 数量
    * @param truckLen 要求车长
    * @param date 发货时间
    * @param other 其他要求
    * @param picIds 图片id:上传图片成功返回的那个id值,有多张图则多个id,由","分隔,如:"1,3,4,5"
    * @param cargoType 货品类型码
    * @return  返回  DataMode;
    * { resultCode:1:发布成功,-2:会话超时,需要重新登陆,-1:操作失败 }
    */
   String publish(int sid, String startAddr,String destinationAdd,String unit,float count,int truckLen,String date,String other,String picIds, String cargoType);
   
   /** 
    * 取得给定企业货主的所有发货列表
    * @param sid 
    * @param status 状态值可以为：该接口中定义的几个常量值
    * @param pageNo  页号
    * @param pageSize 每页行数
    * @return 返回 DataModel<List<CargoInfo>> 
    * 一个的json数组,格式:
    * {
    * 	resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:[{字段同表tp_CargoInfo},...,{totalCount:总行数}]
    * }
    * */
   String getCargoList(int sid, int status,int pageNo,int pageSize);
   
   /** 
    * 取得发货详情
    * @param cid 货源ID
    * @return 返回 DataModel<CargoInfo>
    * 
    * 一个字段同表tp_CargoInfo的json对象 
    *  {
    * 	resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:{字段同表tp_CargoInfo}
    *  }
    */
   String getCargoDetail(int cid);
   
   /**
    * 取得给定货源的所有报价，按时间倒序，价格升序排序
    * @param cid 货源ID
    * @param pageNo  页号
    * @param pageSize 每页行数
    * @return 返回  DataModel<List<OfferListDTO>> 
    * 返回返回一个JSON数组，，其中司机为一个对象
    * {
    * 	resultCode:1-n :正确| -2:会话超时 | -1:操作失败,
    * 	data:[{
    *    pricedId:报价ID
    *    offeredPrice:价格
    *    createTime:报价时间
    *    driver:{
    *    	 driverId:司机ID
    *        head:url
    *        checked:是否验证，
    *        audited:是否审核
    *        credit:信誉度
    *     }
    *    }，...]
    * }
    */   
   String offerList(int cid,int pageNo,int pageSize);
   
   /**
    * 查看司机详情
    * @param 车队司机id 也就是表tp_UsedDriver的主键 
    * @return 返回  DataMode<GetDriverDetailDTO>
    * josn格式如下:
    * {
    * 	resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:{
    *    driverId:司机ID
    *    usedDriverId:车队司机ID
    *    name:真实姓名
    *    alias:呢称
    *    pics:[url,url]证件照料
    *    head:url 头像
    *    multiLocale: 长沙,益阳,.. 常跑地
    *    phone:电话
    *    QrCode:二维码名片
    *    truck:{
    *       head:[url,url,url],
    *       len:长
    *       ton:吨位
    *       type:车型
    *    }
    *  }
    * }
    */
   String getDriverDetail(int udid);
   
   /** 
    * 当前最低报价,有报价则返回最低报价及所关联的报价对象及关联的司机情况;
    * @param cid 货源ID
    * @return 返回 DataModel<LowestOffer>
    * 
    * 返回该报价详情josn, json格式如下
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:{
    *    pricedId:报价ID
    *    offeredPrice:价格
    *    createTime:报价时间
    *    driver:{
    *    	 driverId,
    *        head:url,
    *        checked:是否验证，
    *        audited:是否审核
    *        credit:信誉度
    *     }
    *   }
    *  }
    */
   String lowestOffer(int cid);
   
   /**
    * 向司机发送一个询价消息，以方便该司机在线上报个价
    * @param cid 货源ID
    * @param dids 将多个司机ID加","组成一个字符串,格式如:1,2,4,5
    * @return DataMode;
    * 
    * { resultCode:n:成功询价的个数，-1:操作失败 }*/
   String queryPrice(int cid, String dids);
   
   /** 
    * 根据给定的车队分组ID，查取对应的司机列表，当未指定分组gids="-1"时，为所有分组：
    * @param sid
    * @param gids 将多个分组ID加","组成一个字符串,格式如:1,2,4,5
    * @param pageNo  页号
    * @param pageSize 每页行数
    * @return 返回 DataMode<List<ListDriver>>对象
    * 
    * 返回JSON为司机的对象数组，格式如下
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:[{
    *        driverId:司机ID
    *        alias:呢称,
    *        usedDriverId:车队司机ID
    *        head:url,
    *        checked:是否验证，
    *        audited:是否审核
    *        credit:信誉度
    *        realName:真实姓名
    *        phone:手机号       
    *     },...,{totalCount:总记录数}]
    *   }
    */
   String getMyDriverList(int sid, int pageNo,int pageSize,String gids);
   
   /** 
    * 将当前货源指派给特定的司机； 
    * <该方法暂放这里>
    * @param did 司机ID
    * @param sid 企业货主ID
    * @param startAddr 装货地ID
    * @param destinationAdd 卸货地ID
    * @param unit 单位(件,立方,吨) 见UNIT_开头的常量
    * @param count 数量
    * @param price 协议运费
    * @param bail 保证金
    * @param date 发货时间,格式:2014-04-01
    * @param cargoType 货物类型
    * @param recieverId 收货单位
    * @param recieverMan 收货单位联系人
    * @param recieverPhone 收货单位联系电话
    * @param remark 备注
    * @param picIds 图片id:上传图片成功返回的那个id值,有多张图则多个id
    * @return 返回DataMode对象;
    *   { resultCode:1:派单成功能数;-2:会话超时,需重新登陆,-1:操作失败 }*/
   String dispachBill(int did, int sid,String startAddr,String destinationAddr,String unit,float count,int price,float bail,
		   String date,int cargoType,String remark,int recieverId,String recieverContact,String recieverPhone,int[] picIds);
   
   
   /**
    * 条件搜索车源信息
    * @param startAddr 装货地
    * @param destationAddr 货地
    * @param truckLen 车长
    * @param load 吨位
    * @param scope 范围公里数
    * @param pageNo 页号
    * @param pageSize 每页行数
    * @return 返回的数据结构模型:DataMode<List<SearchTruckInfoDTO>>
    * json格式发下
    * <pre>
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data: [{
	*		truckInfoId:
	*		destination:目的地
	*		truckLength
	*		unit
	*		capacity
	*		truckType
	*		driver:{
	*			driverId:司机ID
    *       	head:"url",
    *       	realName:"真实姓名"
    *      		checked:是否验证0|1
    *       }
    *   },...]
    * }
    * </pre>
    */
   String searchTruckInfoList(String startAddr,String destationAddr,int truckLen,String unit,float capacity, int scope,int pageNo,int pageSize);
   
   /**
    * 根据给定车源ID,取得该车源详细数据
    * @param truckInfoId 车源id
    * @return 返回 DataMode<GetTruckInfoDetailDTO>
    * 
    * 一个json对象,格式如下:
    * {
    * 	 resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	 data:{
    * 		startPoints:"出发地ID"
    * 		destination:"目的地ID"
    * 		loadType:装车类型码
    * 		driver:{
    * 			driverId:司机ID
    *       	head:"司机头像url"
    *       	realName:"真实姓名"
    *      		checked:0|1,是否验证
    *      		picIds:[1,3,4,...] 证件照料    
    *      		qrCode:"URL二维码URL"	
    *      		multiLocale:"常跑地"	
    *      		phone:"司机电话"	
    * 		},
    * 		truck:{
    * 			plateNumber:"车牌号码"
    * 			truckLength:车长
    * 			capacity:可截数量
    * 			unit:'单位'
    * 			truckType:车型码
    * 			head:"url货车头像"
    * 		}
    * 	 }
    * }
    */
   String getTruckInfoDetail(int truckInfoId);
   
   /** 
    * 上传企业发货地的定位信息
    * @param slid 企业货主发货地ID
    * @param lng 经度
    * @param lat 纬度
    * @return DataModel;
    * 
    * { resultCode:1:派单成功能数;-2:会话超时,需重新登陆,-1:操作失败 }
    */
   String setPosition(int slid ,double lng, double lat);
   
   /** 
    * 彼配货源的平台司机列表
    * 把货源的参数作为条件，找到平台中符合该条件车辆的司机列表当然优先把企业车队的排在前面；
    * @param cid  货源ID
    * @param pageNo  页号
    * @param pageSize 每页行数
    * @return  DataModel<List<MatchedDriverListDTO>>
    * 
    * json数组,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:[{
    *        driverId:司机ID
    *        head:url,
    *        checked:是否验证，
    *        realName:真实姓名
    *        phone:手机号
    *        position:{lng:经度,lat:纬度}
    *        truck:{ 车辆情况
    *          len:长
    *          ton:吨位
    *          type:车型
    *        }
    *     },...,{totalCount:总记录数}]
    *  }
    */
   String matchedDriverList(int cid,int pageNo,int pageSize);
   
   /** 
    * 彼配货源的车队司机列表
    * @param cid  货源ID
    * @param pageNo  页号
    * @param pageSize 每页行数
    * @return 返回  DataModel<List<MatchedDriverListDTO>>对象
    * 
    * json数组,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:[{
    *        driverId:司机ID
    *        usedDriverId:车队司机ID
    *        alias:呢称,
    *        head:url,
    *        checked:是否验证，
    *        realName:真实姓名
    *        phone:手机号
    *        position:{lng:经度,lat:纬度}
    *        truck:{ 车辆情况
    *          len:长
    *          ton:吨位
    *          type:车型
    *        }
    *     },...]
    *  }
    */
   String matchedMyDriverList(int cid,int pageNo,int pageSize);
   
   /**
    * 取得指定货源匹配到的符合要求的车源信息条数
    * @param cid 货源ID
    * @return
    */
   String matchedTruckInfoCount(int cid);
   
   /**
    * 取得指定货源接收到的报价条数
    * @param cid 货源ID
    * @return
    */
   String getPricingCount(int cid);
   
   /**
    * 货源重发接口
    * @param cargoId 货源ID
    * @return
    */
   String repost(int cargoId);
}