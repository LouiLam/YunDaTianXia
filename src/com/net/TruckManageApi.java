/***********************************************************************
 * Module:  truckManageAPI.java
 * Author:  hyq
 * Purpose: Defines the Interface truckManageAPI
 ***********************************************************************/
package com.net;

/**
 * 企业车队管理相关API(企业管车API)
 * 类型描述:
 * </br>创建时期: 2014年12月22日
 * @author hyq
 */
public interface TruckManageApi {
   /** 
    * 加司机到车队,企业扫司机二维码也是调此方法;
    * 1，如果司机存在，且车辆存在，则只添加关联
    * 2，如果司机不存在，则需要同时保存数据到司机与车辆表中后，再添加与企业的关联；
    * @param phone  电话
    * @param sid 企业货主ID
    * @return 返回DataMode;
    *  {resultCode:1:添加成功，0：该手机未注册,-2:会话超时,需要重新登陆, -1：操作失败} */
   String relationDriver(String phone, int sid);
   
   
   /** 
    * 新添加司机到车队
    * 司机没注册，车辆也没注册的情况下，先注册司机，及车辆，并关联司机与车辆，最后拉司机到车队
    * @param sid 企业货主ID
    * @param phoneNum 电话
    * @param name 司机呢称
    * @param truckNum 车牌
    * @return 返回DataMode;
    * {resultCode:1:新增成功，-2:会话超时,需要重新登陆,-1：操作失败 }
    */
   String addDriver(int sid, String phoneNum, String name, String truckNum);
   
   /** 扫码司机到车队
    * @param did 司机ID
    * @param sid 企业货主ID
    * @return 返回DataMode;
    *  {resultCode:1:加入成功，-2:会话超时,需要重新登陆,-1操作失败 }*/
   String scanToFleet(int did, int sid);
   
   /** 
    * 获得所有车队司机通讯录
    * @return 返回DataMode<List<GetContactListDTO>>
    * 
    * 返回我的车队中所有司机的JSON数组,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:[{
    *         driverId:司机ID，
    *         name:真实姓名
    *         alias:呢称
    *         head:url 头像
    *         position:当前位置
    *         memberStatus:激活状态
    *   },...]
    * }
    * @param sid 企业货主ID*/
   String getContactList(int sid);
   
   /** 
    * 查取新申请加入车队司机数  
    * @param sid 企业货主ID
    * @return 返回DataMode;
    *  {resultCode:n:新申请加入司机数,-2:会话超时,需要重新登陆,-1:操作失败 }
    */
   String getNewDriverCount(int sid);
   
   /** 
    * 读取所有分组列表
    * @return  返回DataModel<List<DriverGroupDTO>>
    * 一个json对象,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:[{字段描述如表tp_DriverGroup},...,{totalCount:总行数}]
    * }
    * @param sid 企业货主ID*/
   String getGroupList(int sid);
   
   /** 拉司机到黑名单
    * @param UserDriverId 车队司机ID
    * @return 返回DataMode;
    * {resultCode:1:拉黑成功，-2:会话超时,需要重新登陆,-1：操作失败 }*/
   String toBlackList(int userDriverId);
   
   /** 
    * 添加司机到分组
    * @param userDriverId 车队司机ID
    * @param gid 分组ID
    * @return 返回DataMode;
    * {resultCode:1:添加成功，-2:会话超时,需要重新登陆,-1：添加失败 }*/
   String addDriverToGroup(int userDriverId, int gid);
   
   /** 
    * 修改司机称谓(昵称)
    * @param userDriverId 车队司机ID
    * @param newAlias 新昵称
    * @return 返回DataMode;
    * {resultCode:1:修改成功,-2:会话超时,需要重新登陆,-1操作失败 }*/
   String updateAlias(int userDriverId, String newAlias);
   
   /** 
    * 从车队移除司机
    * @param userDriverId 车队司机ID
    * @return  返回DataMode;
    *  {resultCode:1:移除成功,-2:会话超时,需要重新登陆,-1:操作失败} */
   String removeDriver(int userDriverId);
   
   /** 
    * 查取与司机通话记录
    * @return 返回DataModel<List<CallRecordDTO>>
    * 
    * 返回通话记录表（tp_CallRecord）的json数组,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:[{字段描述如表tp_CallRecord所述},...,{totalCount:总行数}]
    * }
    * @param myPhone 我电话
    */
   String getContactRecordList(String myPhone);
   
   /**
    * 新增分组
    * @param sid  企业货主ID
    * @param group 组名称
    * @return 返回DataMode;
    *  {resultCode:1：新增成功，-2:会话超时,需要重新登陆,-1：新增失败 }*/
   String addGroup(int sid, String groupName);
   
   /** 
    * 删除分组
    * @param gidsStr 由1到多个分组ID及","组成的字符串,如"1,3,45,5";
    * @return  {resultCode:N：成功删除数，-2:会话超时,需要重新登陆,-1:操作失败} */
   String deleteGroup(String gidsStr);
   
   /**
    * 修改分组名称
    * @param gid 分组ID
    * @param group 组名
    * @return 返回DataMode;
    *  {resultCode:1:修改成功，-2:会话超时,需要重新登陆,-1：操作失败 }*/
   String updateGroup(int gid, String groupName);
   
   /** 分组统计车队司机总数
    * 根据分组情况，统计每组司机数
    * @param sid 企业货主ID
    * @return {resultCode:n:司机数，-2:会话超时,需要重新登陆,-1：操作失败 }
    */
   String getUsedDriverCountByGroup(int sid);
   
   /**
    * 统计车队总司机数
    * @param sid 企业货主ID
    * @return 返回DataMode;
    *  {resultCode: n:车队总司机数，-2:会话超时,需要重新登陆,-1：操作失败}
    */
   String getUsedDriverTotal(int sid);
   
   /**
    *  查取分组司机列表
    *  根据给定的车队0个或多个分组ID，查取对应的司机列表，当未指定分组时，为所有分组;
    *  @return DataMode<List<MatchedDriverListDTO>>
    *  
    *  返回司机的对象JSON数组,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:[{
    *        driverId:司机ID
    *        alias:呢称,
    *        usedDriverId:车队司机ID
    *        head:url,
    *        checked:是否验证，
    *        position:{lng:经度,lat:纬度,description:位置描述}
    *        truck:{
    *           len:长
    *           ton:吨位
    *           type:车型
    *        }
    *     },...,{totalCount:1}//总行数,所有分页查询,最后一个都是一样
    *     	]
    * }    
    * @param sid 企业货主ID
    * @param gid 分组ID 
    * @param pn 分页号
    * @param ps 每页行数
    */
   String getGroupDriverList(int sid, int gid,int pn,int ps);
   
   /**
    * 取得分组司机数
    * @param gid 分组id
    * @return 返回DataMode;
    *  {resultCode: n:司机数,-2:会话超时,需要重新登陆,-1:操作失败} */
   String getGroupDriverTotal(int gid);
   
   /** 
    * 查取所有注册司机的总数
    * @return 返回DataMode;
    *  {resultCode: n:注册司机总数, -2:会话超时,需要重新登陆,-1:操作失败}*/
   String getDriverTotal();
   
   /** 按省统计平台司机
    * @return json,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data: [
    * 		{湖南省：1000},
    * 		{湖北省：2000},
    *		...,
    *		{totalCount:总行数}
    * 	] 
    * }
    */
   String driverTotalByProvince();
   
   /** 按地市统计指定省内的平台司机数
    * @return json,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:{ 
    *    province:'湖南省'
    *    city:[
    *     	{长沙市：1000},
    *     	{郴州市：2000},
    *     	...,
    *     	{totalCount:总行数}
    *    ]
    * 	}	
    * }
    * 
    * @param province 省名称*/
   String driverTotalByCity(String province);
   /** 
    * 取得地市平台司机数据列表
    * @return  DataMode<List<MatchedDriverListDTO>>
    * 
    * json,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:[{
    *        driverId:司机ID
    *        realName：姓名
    *        head:url头像
    *        checked:是否验证，
    *        truck:{
    *           len:长
    *           ton:吨位
    *           type:车型
    *        }
    *     },...,{totalCount:总行数}]
    * }
    * @param city 地市名*/
   String cityDriverList(String city, int page, int size);
 //--------------------------------------------------------------------
   /** 
    * 取平台司机详情
    * @param did 司机id
    * @return DataModel<GetDriverDetailDTO>
    * 
    * json,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:{
    *    	driverId:司机ID
    *    	name:真实姓名
    *    	pics:[url,url]证件照料
    *    	head:url 头像
    *    	multiLocale: 长沙,益阳,.. 常跑地
    *    	phone:电话
    *    	qrCode:二维码名片    
    *    	truck:[{
    *       	head:'url',
    *       	len:长
    *       	ton:吨位
    *       	type:车型
    *    	}]
    *    }
    * } 
    */
   String getDriverDetail(int did);
   
   /** 根据手机查司机车辆
    * @return return DataMode<GetDriverByPhoneDTO>
    * 
    * json,格式如下:
    * {
    *   resultCode:1 :正确  0:给定的手机号未注册| -2:会话超时 | -1:操作失败,
    * 	data:{
    *     driverId:司机ID
    *     plateNumber:["",'",]多份货车车牌
    *     realName:姓名   
    * 	}
    * }
    * 
    * @param phone 电话*/
   String getDriverByPhone(String phone);
   
   /**
    * 查取请求加入车队司机列表
    * @param sid 企业货主ID
    * @param pn 分号
    * @param ps 每页行数
    *  @return DataMode<SimpleDriverAudit>
    *  
    *  json,格式如下:
    * {
    *   resultCode:申请加车队司机数 | -2:会话超时 | -1:发生服务器异常,
    * 	data:[{
    *        driverId:司机ID
    *        realName：姓名
    *        head:url头像
    *        checked:是否验证，
    *     },...,{totalCount:总行数}] 
    * } 
    */
   String getAppliedJoinList(int sid,int pn,int ps);
   
   /** 
    * 查找已发车源信息,查询符合条件的车源列表;
    * @return DataMode<List<SearchTruckInfoListDTO>>    * 
    * json,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,
    * 	data:[{
    *        driverId:司机ID
    *        alias:呢称,
    *        head:url,
    *        checked:是否验证，
    *        destination:[长沙,益阳]
    *        truck:{
    *            len:长
    *            ton:吨位
    *           type:车型
    *        }
    *     },...,{totalCount:总行数}]
    * }
    * 
    * @param start 出发地
    * @param end 目的地
    * @param len 车长
    * @param unit 载重单位
    * @param capacity 载重量
    * @param scope 位置范围(半径公里数）*/
   String searchTruckInfoList(String start, String end, int len, String unit, int capacity, int scope,int pn,int ps);
   
   /**
    * 取车源详情
    * @return DataModel<GetTruckInfoDetailDTO>
    * 
    * json,格式如下:
    * {
    *   resultCode:1 :正确| -2:会话超时 | -1:操作失败,|0:找不到对应的记录
    * 	data:{
    * 		startPoint;出发地
    * 		destination;目的地
    * 		loadType;装车类型
    * 		shipDate;发车时间
    * 		createTime;信息创建时间
    * 		driver:{
    *    		driverId:司机ID
    *    		name:真实姓名
    *    		alias:呢称
    *    		pics:[url,url]证件照料
    *    		head:url 头像
    *    		multiLocale: 长沙,益阳,.. 常跑地
    *    		phone:电话
    *    		qrCode:二维码名片
    *    		truck:[{
    *       		head:'url',
    *       		len:长
    *       		ton:吨位
    *       		type:车型
    *    		}]
    *   	}
    *   }
    * } 
    * 
    * @param tsid 车源ID*/
   String getTruckInfoDetail(int tsid);
   
   /** 
    * 向多个司机发送货源消息
    * @param cid 货源ID
    * @param dids 由司机ID及","拼成的字符串,格式如"1,34,4,519,3"
    * @return 返回DataMode;
    * {resultCode: 1:操作成功, -2:会话超时,需要重新登陆,-1:服务器异常}
    */
   String sendCargoInfoTo(int cid,String dids);

}