/***********************************************************************
 * Module:  DriverWayBillApi.java
 * Author:  hyq
 * Purpose: Defines the Interface DriverWayBillApi
 ***********************************************************************/
package com.net;

/**
 * 类型描述:司机端我的运单相关的API
 * </br>创建时期: 2014年12月22日
 * @author hyq
 */
public interface DriverWayBillApi {
	/**
	 * 已签约
	 */
   static final int CONTRACTED = 0;
   /**
    * 待发车
    */
   static final int NO_DEPART = 1;
   /**
    * 在途
    */
   static final int DEPARTED = 2;
   /**
    * 到达
    */
   static final int ARRIVED = 3;
   /**
    * 完成
    */
   static final int COMPLETED = 4;
   /**
    * 所有
    */
   static final int ALL = -1;
   
   /** 
    * 取得我已报价的、状态为发布中的货源数量
    * @param did 司机ID
    * @return n:货源数量 ,-2:会话超时,需要重新登陆,-1操作失败
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:[n:货源数量 ]
    * }
    */
   String getOfferedCargoCount(int did);
   
   /** 
    * 取得我参与报价的货源列表
    * @return 返回我的报价及货源信息的json对象数组，格式如：
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:[{
    *     cargoInfoId:货源信息ID
    *     cargoInfoStartPoint 发货地
    *     destination：卸货地
    *     shipDate:发货日期/装货时间
    *     ton:吨位数
    *     rotCount: 抢货人数，已经报价的司机人数
    *     distance:距离公里数
    *     offeredPrice:报价金额
    *     phone:企业货主联系电话
    *   },...]
    * }
    * 
    * @param did 司机ID 
    * 成功示例:{"data":[{"phone":"18874833569","distance":"","rotCount":2,"shipDate":"2015-01-18T01:01:18","cargoInfoId":1,"offeredPrice":500.00,"destination":"长沙","cargoInfoStartPoint":"深圳"},{"generateTotalCount":true,"pageNo":1,"pageSize":10,"totalCount":1}],"resultCode":1}
    */   
   String getOfferedCargoList(int did, int pageNo, int pageSize);
   
   /** 
    * 取得我已报价的货源详情
    * @return 返回我的报价及货源信息,格式如：
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:{
    *     cargoInfoId:货源信息ID
    *     cargoInfoStartPoint 发货地
    *     destination：卸货地
    *     shipDate:发货日期
    *     rotCount: 抢货人数，已经报价的司机人数
    *     distance:距离公里数
    *     ton:吨位数，
    *     offeredPrice:我的报价金额
    *     shipper:{
    *     	 shipperId:ID主键
    *        head:企业货主头像
    *        simpleName
    *        auditStatus
    *        phone
    *     }
    *   }
    * }    
    * @param oid 报价ID
    * 
    * 成功示例:{"data":{"phone":"18874833569","distance":"","rotCount":2,"shipDate":"2015-01-18T01:01:18","cargoInfoId":1,"offeredPrice":500.00,"destination":"长沙","cargoInfoStartPoint":"深圳"},"resultCode":1}
    */
   String getOfferedCargoDetail(int oid);
   
   /** 
    * 取得我的运单数
    * @param did 司机ID
    * @param status 用接口中定义的 运单状态值
    * @return 
    *  * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:[n:指定司机的在途运单数]
    * }
    */
   String getBillCount(int did, int status);
   
   /** 
    * 我的运单列表
    * 说明：要转成DriverWayBillDTO对象
    * @return 根据运单状态，查取我的运单列表的json运单对象数组，格式如：
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:[{
    * 		waybillId：运单ID
    *  		createTime运单生成时间
    * 		loadingTime:装货时间
    * 		simpleName企业简称
    * 		billStartPoint出发地
    * 		destination目的地
    * 		unit:货计量单位
    * 		amount:数量
    * 		agreementPrice:运费
    * 		phone:企业货主联系电话
    * 	},...]
    * }   
    * @param status 用接口中定义的几咱运单状态值
    * @param did  司机ID
    * 
    * 成功示例:{"data":[{"agreementPrice":1.00,"simpleName":"111111","createTime":"2014-01-01T00:00:00","phone":"18874833569","loadingTime":"2014-01-01T00:00:00","billStartPoint":"1","waybillId":1,"destination":"1"},{"generateTotalCount":true,"pageNo":1,"pageSize":10,"totalCount":1}],"resultCode":1}
    */
   String getBillList(int status, int did, int pageNo, int pageSize);
   
   /** 
    * 取得运单详情
    * @return 返回一个运单的详情JSON，格式如：
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:{
    *   	waybillId:运单ID
    *   	unloadAddress详细卸货地址
    *   	loadAddress详细装货地
    *   	departTime:发车时间
    *   	planArrivalDate:计划到达时间
    *   	receiverUnit收货单位
    *   	receiverContact收货联系人
    *   	receiverPhone收货联系电话
    *   	msgCount:动态消息数
    *  		agreementPrice协议运费
    *  		cargoType货物类型
    *  		waybillStatus运单状态
    *   	position::{//最近一次定位区域位置(到市）
    *   		lng:经茺,
    *   		lat:纬度
    *   	}
    *   	basicRemark基本备注
    *   	basicPics:[url1,url2]  
    *  		shipper:{
    *    		fullName:企业全称
    *   		contact 发货联系人
    *   		phone：发货联系电话
    *    		detailAddress：企业详细所在地
    *  		}
    * 	}
    * }     
    * @param billId 运单号
    * 
    * 成功示例:{"data":{"position":{"lng":0.0,"lat":20.0},"planArrivalDate":"2014-01-01T00:00:00","agreementPrice":1.00,"loadAddress":null,"receiverContact":null,"shipper":{"phone":"18874833569","fullName":"11111","contact":"1","detailAddress":"1"},"departTime":"2014-01-01T00:00:00","basicRemark":null,"amount":null,"basicPics":[],"waybillStatus":3,"unit":null,"cargoType":1,"billId":1,"unloadAddress":null,"msgCount":3,"receiverUnit":null,"receiverPhone":null},"resultCode":1}
    */
   
   String getBillDetail(int billId);
   /** 
    * 取得运单的动态列表
    * @return 返回一个动态对象的JSON数组，格式如：
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:[{
    * id：动态主键ID
    * content:事件内容
    * createTime:动态时间 格式：16:16 01-06
    * dynPics:[url1,url2,url3]
    * personId:事件发布人ID
    * eventType:动态事件类型 [1：司机触发的事件如，在途异常，2：货主触发的事件，如到货，发货；3：运单自然状态的变更]
    * head:头像
    * waybillId:运单id
    * },...]
    * }
    * 
    * @param billId 运单号
    * 
    * 成功示例:{"data":[{"id":1,"content":"12222","createTime":"16:16 01-06","dynPics":[],"personId":1,"eventType":1,"head":"1","waybillId":1},{"id":2,"content":"12222","createTime":"16:41 01-06","dynPics":["123"],"personId":1,"eventType":1,"head":"1","waybillId":1},{"id":3,"content":"12222","createTime":"17:26 01-06","dynPics":[],"personId":1,"eventType":1,"head":"1","waybillId":1}],"resultCode":1}
    */
   String getBillDynamics(int billId);
   
   /**
    * 新增运单动态
    * @param billId 运单号 
    * @param content 动态内容
    * @param pics 图片URL 1,2,3
    * @return 1：新增成功,-2:会话超时,需要重新登陆,-1：新增失败 */
   String addBillDynamic(int billId, String content, String pics);
   
   /** 
    * 提醒货主发车司机发给货主的一个消息，提供对方点确认发车操作
    * @param billId 运单号 
    * @return 1:提供成功 ,-2:会话超时,需要重新登陆,-1操作失败 
   {
	    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
	    * 	data:[{各字段如表tp_WaybillEventLog所述},...]
	    * }*/
   String remindGo(int billId);
   
   /**
    * 到货,改变运单为到货状态 
    * @param billId 运单号 
    * @return  
    * {
	    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
	    * 	data:[null]
	    * }
    * */
   String  arrived(int billId);
}