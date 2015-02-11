/***********************************************************************
 * Module:  MessageAPI.java
 * Author:  hyq
 * Purpose: Defines the Interface MessageAPI
 ***********************************************************************/
package com.net;

/**
 * 类型描述:消息相关操作的API;
 * </br>创建时期: 2014年12月22日
 * @author hyq
 */
public interface MessageApi {
	/**11：系统消息: 对所有企业货主的广播消息*/
	int TYPE_SYSTEM_SHIPPER=11;
	/**12：系统消息：对所有司机的广播消息*/
	int TYPE_SYSTEM__DRIVER=12;
    /** 2，货源配车*/
    int TYPE_CARGOINFO=2;
    /** 3，车源配货*/
    int TYPE_TRUCKINFO=3;
    /** 4：司机发给货主的 */
    int TYPE_DRIVER_TO_SHIPPER=4;
    /** 5：货主发给司机的 */
    int TYPE_SHIPPER_TO_DRIVER=5;
    /**
     * 消息通知,外部系统有新消息需要通知平台;调用些方法;
     * 有此接口的动机是：货车的位置数据发到定位服务处，定位服务再调用该方法(回调）通知平台，平台根据发过来的数据，做一些具体业务：
     * <Li>当在在途时，根据货车的位置来自动修改该运单的状态
     * <Li>当在途时，货车到哪(行政区）了，自动生成动态数据到表
     * @param messageJson JSON消息对象,结构(另定)
     * 暂定格式如下：{'messageContent':'货车司机已发车，发消息通知货主，该单已发货!','messageCreator':0,'messageId':1,'messageTime':'2015-01-06T17:17:15','messageTitle':'司机发送给客户，已发车','messageType':5,'messageWeight':0}
     *  @return json对象
     * {
     *   resultCode:1：操作成功，-2,会话超时,需重新登陆,-1：操作失败 
     * 	 data:{}为空
     * }
     */
    String messageNotice(String messageJson);
	
   /** 
    * 获得最新消息的前两条，并统计出总条数，及每类消息的子消息总数
    * @param mid 会员ID
    * @return json对象数组
    * {
    *   resultCode:1 :正确| 0:会话超时 | -1:操作失败,
    * 	data:{
    * 		total:n条数,
    * 		msg:[{
    * 			count:该类条数,
    * 			字段如com.ql.entity.TpMessage 
    * 			},...]
    * 	s}
    * }
    */
   String getMessage(int mid);
   
   /** 
    * 查取发送给我的所有未读消息；包括运单动态消息，三类（系统、车配货，货配车）消息；
    * @param mid 会员ID
    * @param pageNo 当前页
    * @param pageSize 每页显示大小
    * @param msgType 消息类型,可以MessageAPI.TYPE_WAY_BILL以TYPE_开头的几个常量值, 格式如:(11,12,2,3)
    * @return json对象数组
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
    * 
    * 成功示例：
    * {"data":[{"messageContent":"货车司机已发车，发消息通知货主，该单已发货!","messageCreator":0,"messageId":3,"messageTime":"2015-01-06T17:26:29","messageTitle":"司机发送给客户，已发车","messageType":11,"messageWeight":0},{"generateTotalCount":true,"pageNo":1,"pageSize":10,"totalCount":1}],"resultCode":1}
    */   
   String getMessageList(int mid,int pageNo,int pageSize, String msgType);
 
   /** 
    * 删除tp_MessageMemberRelation表的指定ID的一行数据
    * @param mmrid 该ID为tp_MessageMemberRelation表中的主键
    * @return json对象
    * {
    *   resultCode:1：删除成功，-2,会话超时,需重新登陆,-1：操作失败 
    * }
    * 成功示例:{"resultCode":1}
    * */
   String delMessage(int mmrid);
   
   
   /**
	 * 发送消息,新增消息及消息关系表;并推送到客户端;
	 * @param senderId 发送者会员ID
	 * @param recieverIds 接收者会员ID
	 * @param message 消息内容
	 * @return {resultCode:1,发送成功,-1:服务器发生异常}
	 */
	String sendMessage(int senderId,String recieverIds,String message);
	
	/**
	 * 发送系统消息到所有用户,新增消息及消息关系表;并推送到客户端;
	 * @param messsage 消息内容
	 * @return {resultCode:1,发送成功,-1:服务器发生异常}
	 */
	String sendSystemMessageToAll(String message);
	
	/**
	 * 发送系统消息到所有司机,新增消息及消息关系表;并推送到客户端;
	 * @param message 消息内容
	 * @param driverIds 由司机ID及","拼成的字符串,如"1,3,4,44" 
	 *  @return {resultCode:1,发送成功,-1:服务器发生异常}
	 */
	String sendSystemMessageToDriver(String message,String driverIds);
	
	/**
	 * 发送系统消息到所有企业货主,新增消息及消息关系表;并推送到客户端;
	 * @param message 消息内容
	 * @param shipperIds 由企业货主ID及","拼成的字符串,如"1,3,4,44"
	 * @return {resultCode:1,发送成功,-1:服务器发生异常}
	 */
	String sendSystemMessageToShipper(String message,String shipperIds);
	
	/**
	 * 发送系统消息到所有收货单位,新增消息及消息关系表;并推送到客户端;
	 * @param message 消息内容
	 * @param recieverIds 由由货单位ID及","拼成的字符串,如"1,3,4,44"
	 * @return {resultCode:1,发送成功,-1:服务器发生异常}
	 */
	String sendSystemMessageToReciever(String message,String recieverIds);
	
	/**
	 * 向指定的一组司机会员发送指定货源询价短信
	 * @param cargoId 货源ID
	 * @param driverIds 由司机ID及","拼成的字符串,如"1,3,4,44"
	 * @return {resultCode:1,发送成功,-1:服务器发生异常}
	 */
	String sendCargoMessageToDriver(int cargoId, String driverIds);
}