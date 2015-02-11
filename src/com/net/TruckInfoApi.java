/***********************************************************************
 * Module:  TruckInfoApi.java
 * Author:  hyq
 * Purpose: Defines the Interface TruckInfoApi
 ***********************************************************************/
package com.net;


/**
 *  类型描述:发布货源相关功能的API
 * </br>创建时期: 2014年12月22日
 * @author hyq
 */
public interface TruckInfoApi {
   /** 
    * 发布车源
    * @param driverId  司机ID
    * @param plateNumber 车牌号码
    * @param departTime  出发时间
    * @param start 出发地
    * @param destination  目的地
    * @param type 装车类型 
    * @return 
    * {
    *   resultCode:1:发布成功，-2:会话超时,需要重新登陆,-1：操作失败
    * 	data:[]
    * }   
    */
   String publishTruckInfo(int driverId,String plateNumber,String departTime, String start, String destination, int type);
   
   /** 
    * 返回的数据data对象使用TruckInfoDTO对象
    * 取得所有未结束的车源记录, 并读取彼配货源信息个数；
    * @return 一个json对象,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:[{
    * 		id:车源ID
    *    	startPoints;出发地
    *    	destination;目的地
    *    	loadType;装车类型
    *    	shipDate;发车时间
    *    	matchedCount:彼配的货源数
    *   },...]
    * }     
    * @param did 司机ID
    * 成功示例:{"data":[{"id":1,"loadType":1,"matchedCount":0,"startPoints":"深圳","shipDate":"2015-01-16T14:11:08","destination":"长沙"},{"id":2,"loadType":1,"matchedCount":0,"startPoints":"深圳","shipDate":"2015-01-16T17:10:11","destination":"长沙"},{"generateTotalCount":true,"pageNo":1,"pageSize":10,"totalCount":2}],"resultCode":1}

    * */
   String getTruckInfoList(int did,int pageNo,int pageSize);
   
   /** 
    * 返回的数据data对象使用OfferedCargoDTO对象
    * 取得与车源彼配的货源列表
    * @return 返回每个车源彼配的所有状态为“发布中”的货源列表，并计算出车源与该货源的距离（公里数）,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:[{
    *     	cargoInfoId:货源信息ID
    *     	cargoInfoStartPoint 发货地
    *     	destination：卸货地
    *     	shipDate:发货日期
    *     	rotCount: 抢货人数，已经报价的司机人数
    *     	distance:距离公里数
    *    	ton:吨位数
    *       credit:推荐指数
    *       cargoInfoStatus：信息状态
    * 	},。。。]
    * } 
    * @param tid 车源id
    * 成功示例;{"data":[{"distance":"","cargoInfoStatus":0,"rotCount":2,"shipDate":"2015-01-18T01:01:18","cargoInfoId":1,"destination":"长沙","cargoInfoStartPoint":"深圳"},{"generateTotalCount":true,"pageNo":1,"pageSize":10,"totalCount":1}],"resultCode":1}
    */
   String getMatchedList(int tid,int pageNo,int pageSize);
   
   /**
    * 取得货源详情
    * 返回的数据data对象使用OfferedCargoDTO对象
    * @return 返回JSON,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:{
    *     	cargoInfoId:货源信息ID
    *     	cargoInfoStartPoint 发货地
    *     	destination：卸货地
    *     	shipDate:发货日期
    *     	rotCount: 抢货人数，已经报价的司机人数
    *     	distance:距离公里数
    *    	ton:吨位数
    *       credit:推荐指数
    *       cargoInfoStatus：信息状态
    *     	shipper:{
    *        	head:企业货主头像
    *        	simpleName
    *        	auditStatus
    *     	}
    * 	}
    * }
    * 
    * @param cid 货源ID
    */
   String getCargoDetail(int cid);
   
   /** 
    * 抢货报价
    * @param did 司机ID
    * @param cid 货源ID
    * @param offeredPrice 报价
    * @return 1:抢货成功，-2:会话超时,需要重新登陆,-1：操作失败 
    *  {
    *   resultCode:1:抢货成功，-2:会话超时,需要重新登陆,-1：操作失败 
    * 	data:[]
    * } 
    * 
    */
   String lootCargo(int did, int cid, float offeredPrice);
   
   
   /** 
    * 查源一手货源列表
    * 返回的数据data对象使用OfferedCargoDTO对象
    * @return 返回每个车源彼配的所有状态为“发布中”的货源列表，并计算出车源与该货源的距离（公里数）,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:[{
    *     	cargoInfoId:货源信息ID
    *     	cargoInfoStartPoint 发货地
    *     	destination：卸货地
    *     	shipDate:发货日期
    *     	rotCount: 抢货人数，已经报价的司机人数
    *     	distance:距离公里数
    *     	ton:吨位数
    *       credit:推荐指数
    *       cargoInfoStatus：信息状态
    * 	},。。。]
    * }
    * @param departTime departDate 
    * @param start 出发地
    * @param destination 目的地
    * 
    * 成功示例：{"data":[{"distance":"","CargoInfoStatus":0,"rotCount":2,"shipDate":"2015-01-18T01:01:18","cargoInfoId":1,"destination":"长沙","cargoInfoStartPoint":"深圳"},{"generateTotalCount":true,"pageNo":1,"pageSize":10,"totalCount":1}],"resultCode":1}

    */
   String queryCargoList(int departDate, String start, String destination,int pageNo,int pageSize);
   
   /** 
    * 取车源详情
    * 返回的数据data对象使用TruckInfoDTO对象
    * @return 返回一个车源详情的json对象， ,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:{
    * id：主键
    * driverId：发布司机主键
    * startPoints：出发地
    * destination：目的地
    * plateNumber：车牌号码
    * createTime：信息创建时间
    * shipDate：发车时间
    * truckStatus：信息状态
    * truckLength：车长
    * unit：单位
    * capacity：可载数
    * loadType：装车类型
    * truckType：车型
    * startLon：出发地定位经度
    * startLat：出发地定位纬度
    *  }
    * }
    * @param tid 车源ID
    * 
    * 成功示例：{"data":{"capacity":null,"createTime":"2015-01-16T14:11:08","destination":"长沙","driverId":1,"id":1,"loadType":1,"plateNumber":"湘A.123456","shipDate":"2015-01-18T14:11:08","startLat":0.0,"startLon":0.0,"startPoints":"深圳","truckLength":null,"truckStatus":1,"truckType":null,"unit":null},"resultCode":1}

    */
   String getTruckInfoDetail(int tid);
   
   /** 
    * 换一部车,将司机绑定到另一台车 
    * @param did 司机ID
    * @param plateNumber 货车号码
    * @return 1：换车成功，-2:会话超时,需要重新登陆,-1:操作失败
    *  {
    *   resultCode:1：换车成功，-2:会话超时,需要重新登陆,-1:操作失败 
    * 	data:[]
    * } 
    *  
    */
   String changeTruck(int did, String plateNumber);
   
   
   /**
    * 返回的数据data对象使用OfferedCargoDTO对象
    * 条件筛选(搜索)一手货源
    * @param departDate 出发时间 
    * @param startAddr 出发地
    * @param destationAddr 目的地
    * @param pageNo  页号
    * @param pageSize 每页行数
    * @return 一个json对象数组
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data: [{字段同表tp_TruckInfo}]
    * }
    */
   //@Deprecated 这个是要实现的,就是调用'CargoInfoService"对应的方法;
   String searchCargoInfoList(int departDate,String startAddr,String destationAddr,int pageNo,int pageSize);
   
}