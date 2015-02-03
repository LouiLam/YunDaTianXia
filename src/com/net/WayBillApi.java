/***********************************************************************
 * Module:  WayBillAPI.java
 * Author:  hyq
 * Purpose: Defines the Interface WayBillAPI
 ***********************************************************************/
package com.net;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类型描述： 企业货主的运单管理相关API
 * </br>创建时期: 2014年12月22日
 * @author hyq
 */
public interface WayBillApi {
	/**已签约*/
   static final int STATUS_CONTRACTED = 0;
   /**未发车*/
   static final int STATUS_NODEPART = 1;
   /**已发车*/
   static final int STATUS_DEPARTED = 2;
   /**已到达*/
   static final int STATUS_ARRIVED = 3;
   /**已完成(已经结算）*/
   static final int STATUS_COMPLETED = 4;
   /**所有运单*/
   static final int STATUS_ALL = -1;
   
   /**
    * 删除状态
    */
   int STATUS_DELETED=-1;
   
   /** 
    * 签约(未定),签约就是在运单表生成一条运单,只不过状态还只是签约状态
    * @param cid  货源ID
    * @param tid  车辆ID
    * @param did 司机ID
    * @param price  运价
    * @return DataMode
    * {
    * 	resultCode:1:创建成功，-2:会话超时,需要重新登陆, -1：操作失败,服务器异常 ,-3:操作失败，业务逻辑异常
    * }
    */
   String contract(int cid, int tid, int did, float price);
   
   /** 
    * 签约单转运单,将状态改成运单
    * @param billId 簦约单ID
    * @param remark 
    * return DataMode 
    *  {
    * 	resultCode:1:创建成功，-2:会话超时,需要重新登陆, -1：操作失败,服务器异常 ,-3:操作失败，业务逻辑异常
    *  }
    */
   String contract2Bill(int billId, String remark);
   
   /** 
    * 全新的创建运单
    * @param sid 企业货主 
    * @param did 承运司机 
    * @param cid 货源 
    * @param shipperContact:发货联系人
    * @param planArrivalDate，计划到达日期 yyyy-MM-dd
    * @param departDate:发车时间 yyyy-MM-dd HH:mm:ss
    * @param agreementPrice:协议运费
    * @param plateNumber，承运车辆车牌
    * @param insurance:保险
    * @param receiverUnit：收货单位
    * @param receiverContact:收货联系人
    * @param receiverPhone：收货联系电话
    * @param unloadAddress:详细卸货地址
    * @return DataMode
    *  {
    * 	resultCode:1:创建成功，-2:会话超时,需要重新登陆, -1：操作失败,服务器异常 ,-3:操作失败，业务逻辑异常
    *  }
    *  */
   String newBill(int sid, int did, int cid, String shipperContact,
			String planArrivalDate, String departDate, float agreementPrice,
			String plateNumber, float insurance, String receiverUnit,
			String receiverContact, String receiverPhone, String unloadAddress);
   
   /** 
    * 取得企业货主的所有指定状态的运单数据,当运单为在途、到达状态时，需要统计该运单的未读动态条数,当前所在位置
    * @param sid 企业货主ID
    * @param status 运单状态,可以是WayBillAPI.STATUS_CONTRACTED等以"STATUS_"开头的常量值
    * @return DataMode<List<WayBillDTO>>
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1：操作失败,服务器异常 |-3:操作失败，业务逻辑异常
    * 	data:[{
    *   	billId:运单ID
    *   	billStartPoint:出发地
    *   	destination:卸货地
    *   	departTime:发车时间
    *   	plateNumber，承运车辆车牌
    *   	msgCount:动态消息数
    *       simpleName:货主简称
    *       loadingTime:装车时间
    *       amount:数量
    *       unit;单位
    *       agreementPrice:成交价
    *       shipperPhoneNumber:企业电话
    *   	position:最近一次定位区域位置(到市）
    *  	},....{totalCount:总行数,...}]
    * }
    * */
   String querytBillList(int sid, int status, int pageNo, int pageSize);
  
   /**
    * 读取运单详情,
    * @param billID 运单ID
    * @return DataMode<WayBillDTO>
    *  返回一个运单的详情JSON，,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | 1：操作失败,服务器异常 |-3:操作失败，业务逻辑异常
    * 	data:{
    *   	billId:运单ID
    *   	startPoint:出发地
    *   	destination:卸货地
    *   	departTime:发车时间
    *   	planArrivalDate:计划到达时间
    *   	plateNumber，承运车辆车牌
    *  	 	msgCount:动态消息数
    *   	position::{//最近一次定位区域位置(到市）
    *   		lng:经度,
    *   		lat:纬度
    *   	}, 
    *  		driver:{
    *    		head:头像url
    *    		checked:是否验证:
    *    		credit:信誉度（星级）
    *    		phone:电话
    *    		plateNumber:车牌
    *    		truckLength:车长
    *    		weightCapacity：载重
    *    		truckType;车型
    *    		pics:[url1,url2]  现在还没明确这时放哪些图片
    *  		}
    * 	}
    * }
    */
   String getBillDetail(int  billId);
   
   /**
    * 发送一个定位请求给司机，并返回司机的实时定位JSON对象
    * @param billId 运单ID
    * @return DataMode<List<DriverLocationDTO>>
    * 返回一个经纬度的结果,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 |-1：操作失败,服务器异常 |-3:操作失败，业务逻辑异常
    * 	data:{//如果长时间没有响应，则返回最近一次定位的结果
    * 		billId:运单ID，
    * 		lng:23.1212121,
    * 		lat:23,232323
    * 	}
    * }
    */
   String sendPositionRequest(int  billId);
   
   /** 
    * 将该运单到目前为止的所有定位点返回
    * @param billId 运单ID 
    * @return DataMode<BillLocationDTO>
    * JSON,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1：操作失败,服务器异常 |-3:操作失败，业务逻辑异常
    * 	data:{
    * 		billId:运单ID，
    * 		pos:[
    *           {lng:122.32323,lat:12.121212},
    *           {lng:121.32323,lat:12.121212},
    *            。。。
    *       ]
    * 	}
    * }
    * 
    */
   String getBillLocus(int  billId);
   
   /** 
    * 将发给本企业的所有未读消息总数返回，还包括运单动态的消息数
    * @param memberId 会员帐号ID
    * @return DataMode
    *  n:消息数,-2:会话超时,需要重新登陆, -1：操作失败,服务器异常 ,-3:操作失败，业务逻辑异常
    */
   String getBillTrendsCount(int memberId);
   
   /**
    * 读运单动态列表, 把未读的，放在前面，按时间倒序排序，
    * @param billCode 
    * @return DataMode
    * 返回的JSON,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1：操作失败,服务器异常 |-3:操作失败，业务逻辑异常
    * 	data:[{
    *    	动态相关字段看料表tp_WaybillEventLog所述；
    *    	pics:[url1,url2,...],//动态照片
    *   },...]
    * }
    * 
    */
   String getBillTrendsList(int memberId, int pageNo, int pageSize);
   
   /**
    * 读运单动态详情
    * @param 动态ID
    * @return DataMode<待定>
    * 格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1：操作失败,服务器异常 |-3:操作失败，业务逻辑异常
    * 	data:{字段如tp_Waybilleventlog所述}
    * }
    */
   String getBillTrendsDetail(int trendsId);
   
   /** 
    * 分享到(需求不明
    * @param billCode 运单ID
    * @return DataMode
    * 格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,-3:操作失败，业务逻辑异常
    * 	data:{}
    * }
    * 
    */
   String shallTo(int billId);
   
   /** 
    * 由司机触发，改变当前运单的状态为“到达”，并更新运单相关字段的值，
    * 如到货时间，当前状态等；    * 
    * @param billId 运单Id
    * @return 1:操作成功,-2:会话超时,需要重新登陆, -1:操作失败
    * {
    * 	resultCode:1:创建成功，-2:会话超时,需要重新登陆, -1：操作失败  ,-3:操作失败，业务逻辑异常
    * 	data:{}
    *  }
    *  */
   String submitArrived(int billId)throws Exception;
   
   /** 
    * 发车,由司机触发，改变当前运单为“在途”状态,并更新运单相关字段的值，如发车时间，当前状态等； 
    * @param billId
    * @return DataMode
    * {
    * 	resultCode:1:创建成功，-2:会话超时,需要重新登陆, -1：操作失败  ,-3:操作失败，业务逻辑异常
    * 	data:{}
    *  }
    */
   String depart(int billId);
   
   /** 
    * 添加承运司机
    * @param did  司机ID
    * @param billId 运单ID
    * @param plantNumber 车牌号
    * @param remark 备注
    * @param pics 图片外键ID数组
    * @return  DataMode 
    * 
    * 1:添加成功,-2:会话超时,需要重新登陆, -1操作失败 ,-3:操作失败，业务逻辑异常*/
   String appendDriver(int did, int billId, String plantNumber, String remark, int[] pics);
   
   /** 
    * 逻辑删除订单
    * @param billId 运单ID
    * @return DataMode
    * {
    * 	resultCode:1:操作成功,0：该运单不能删除;-2:会话超时,需要重新登陆, -1:操作失败  ,-3:操作失败，业务逻辑异常
    * 	data:{}
    *  }
    * 
    */
   String deleteBill(int billId);
  
   /** 
    * 修改运单
    * @param sid 企业货主ID
    * @param billId 运单ID
    * @param billJSON 运单json,json格式如下：
    * {
    *  shipperContact:发货联系人
    *  planArrivalDate，计划到达日期
    *  departDate:发车时间
    *  agreementPrice:协议运费
    *  plateNumber，承运车辆车牌
    *  insurance:保险
    *  receiverUnit：收货单位
    *  receiverContact:收货联系人
    *  receiverPhone：收货联系电话
    *  unloadAddress:详细卸货地址
    * }   	
    * @return DataMode
    * {
    *   resultCode:1：操作成功。-2:会话超时,需要重新登陆, -1：操作失败,-3操作失败，逻辑错误
    * }
    * 
    */
   String updateBill(int sid, String shipperContact,Date planArrivalDate ,Date departDate ,BigDecimal agreementPrice ,
		   String plateNumber,BigDecimal insurance ,String receiverUnit,
		   String receiverContact,String receiverPhone,String unloadAddress);
   
   /**
    * 添加破损记录
    * @param billId  运单ID
    * @param breakRemark  破损说明
    * @param pics 图片外键集
    * @return DataMode
    * {
    *   resultCode:1:添加成功，-2:会话超时,需要重新登陆, -1：操作失败,-3操作失败，逻辑错误
    * }
    * 
    */
   String addBreakRecord(int billId, String breakRemark, int[] pics);
   
   /**
    * 运单结算 :由企业？发起，改变运单状态为“完成”，并更新运单几个字段的值，结算时间，状态等 
    * @param billId 运单ID
    * @param sid  企业货主ID
    * @param breakCost 破损费 
    * @param carriage  运费
    * @param insurance 货险
    * @param otherCost 其他费用
    * @param pics  结算图片的外键ID集
    * @param remark 结算说明
    * @return DataMode
    * {
    *   resultCode:1:结算成功，-2:会话超时,需要重新登陆, -1：结算失败,-3操作失败，逻辑错误
    * }
    * 
    */ 
   String settleAccounts(int billId,int sid, float breakCost, float carriage, float insurance, float otherCost, int[] pics, String remark);
   
   /** 运单统计   * 需求暂不明 ,格式如下:
    * @return DataMode<待定>
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:{}}*/
   String billStatistics();
   
   /**
    * 我（企业货主）的签约
    * @param 企业货主ID
    * @return DataMode<待定>
    * 签约对象的JSON数组(格式未明）,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:{需求不明}
    * }
    */
   String queryBillDriverList(int sid);
   
   /** 
    * 手工关闭，改运单状态为完成    * 
    * @param billID 运单ID
    * @return DataMode
    * {
    *   resultCode:1:手工结束成功，-2:会话超时,需要重新登陆, -1:操作失败 
    * }
    */
   String CompleteBill(int billId);
}