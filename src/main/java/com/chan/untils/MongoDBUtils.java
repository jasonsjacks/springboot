package com.chan.untils;


import com.mongodb.bulk.BulkWriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.List;


/**
 * mongodb curd 工具类
 * author LiuLuoBing
 */
@Component
public class MongoDBUtils {
    //自动装配 MongoTemplate
    @Autowired
    @Qualifier("mongoTemplatesQuote")
    private MongoTemplate mongoTemplates;

    public  MongoDBUtils mongoDBUtils;

    /**
     * 完成依赖注入后，在对象的非静态的 void 方法内执行特定任务，以确保在对象使用之前可以将任务执行效果使用起来。
     */
    @PostConstruct
    public void init() {
        mongoDBUtils = this;
        mongoDBUtils.mongoTemplates = this.mongoTemplates;
    }

    /**
     * 保存数据对象，集合为数据对象 @Document(相当于：行) 注解所配置的collection(表)
     *
     * @param obj 数据对象
     */
    public  void save(Object obj) {
        mongoDBUtils.mongoTemplates.save(obj);
    }

    /**
     * 把数据对象 保存到指定集合
     *
     * @param obj            数据对象
     * @param collectionName 指定集合名
     */
    public  void save(Object obj, String collectionName) {
        mongoDBUtils.mongoTemplates.save(obj, collectionName);
    }

    /***
     * 根据数据对象中的id删除数据，集合为数据对象@Document(相当于：行) 注解所配置的collection(表)
     * @param obj 数据对象
     */
    public  void remove(Object obj) {
        mongoDBUtils.mongoTemplates.remove(obj);
    }

    /***
     * 指定集合，根据数据对象中的Id 删除数据
     * @param obj      数据对象
     * @param collectionName 集合名
     */
    public  void remove(Object obj, String collectionName) {
        mongoDBUtils.mongoTemplates.remove(obj, collectionName);
    }

    /***
     * 根据key，value到指定集合删除数据
     * @param key  键
     * @param value 值
     * @param collectionName 集合名
     */
    public  void removeById(String key, Object value, String collectionName) {
        Criteria criteria = Criteria.where(key).is(value);
        criteria.and(key).is(value);
        Query query = Query.query(criteria);
        mongoDBUtils.mongoTemplates.remove(query, collectionName);

    }

    /***
     * 指定集合，修改数据，且仅修改找到的第一条数据
     * @param accordingKey       修改条件key
     * @param accordingValue     修改条件value
     * @param updateKeys         修改内容key数组
     * @param updateValues       修改内容value数组
     * @param collectionName     指定的集合名
     */
    public  void updateFirst(String accordingKey, Object accordingValue, String[] updateKeys
            , Object[] updateValues, String collectionName) {
        //单条修改
        updateCommon(accordingKey, accordingValue, updateKeys, updateValues, collectionName, 1== 2);
    }

    /***
     * 指定集合，修改数据，且修改找到的所有数据
     * @param accordingKey       修改条件key
     * @param accordingValue     修改条件value
     * @param updateKeys         修改内容key数组
     * @param updateValues       修改内容value数组
     * @param collectionName     指定的集合名
     */
    public  void updateMulti(String accordingKey, Object accordingValue, String[] updateKeys
            , Object[] updateValues, String collectionName) {
        //修改多条数据
        updateCommon(accordingKey, accordingValue, updateKeys, updateValues, collectionName, 2 == 2);
    }

    /***
     * 修改操作公共部分
     * @param accordingKey    修改条件key
     * @param accordingValue  修改条件value
     * @param updateKeys      修改内容key数组
     * @param updateValues    修改内容value数组
     * @param collectionName  指定的集合名
     * @param multi           false : 修改一条  true: 修改多条
     */
    private  void updateCommon(String accordingKey, Object accordingValue, String[] updateKeys, Object[] updateValues,
                               String collectionName, boolean multi) {
        Criteria criteria = Criteria.where(accordingKey).is(accordingValue);
        Query query = Query.query(criteria);
        Update update = new Update();
        for (int i = 0; i < updateKeys.length; i++) {
            update.set(updateKeys[i], updateValues[i]);
        }
        if (multi)
            mongoDBUtils.mongoTemplates.updateMulti(query, update, collectionName);
        else
            mongoDBUtils.mongoTemplates.updateFirst(query, update, collectionName);
    }

    /***
     * 根据条件查询出所有结果集，集合为数据对象中@Document 注解所配置的collection
     * @param obj  数据对象
     * @param findKeys   查询条件key
     * @param findValues 查询条件value
     * @return
     */
    public  List<? extends Object> find(Object obj, String[] findKeys, Object[] findValues) {
        return getListCommon(obj, findKeys, findValues, "",false,"");

    }

    /***
     * 指定集合，根据条件查询出所有结果集
     * @param obj  数据对象
     * @param findKeys   查询条件key
     * @param findValues 查询条件value
     * @return
     */
    public  List<? extends Object> find(Object obj, String[] findKeys, Object[] findValues, String collectionName) {
        return getListCommon(obj, findKeys, findValues, collectionName,false,"");

    }

    /***
     * 指定集合，根据条件查询出所有结果集，并排倒序
     * @param obj      数据对象
     * @param findKeys 查询条件key
     * @param findValues  查询条件value
     * @param collectionName 集合名
     * @param sort           排序字段
     * @return
     */
    public  List<? extends Object> find(Object obj, String[] findKeys, Object[] findValues, String collectionName,String sort) {
        return getListCommon(obj, findKeys, findValues, collectionName,true,sort);

    }

    /***
     * 查询数据公共部分
     * @param obj   数据对象
     * @param findKeys   查询条件key
     * @param findValues 查询条件value
     * @param collectionName    查询的集合名称
     * @param desc              true :表示降序   false: 表示升序
     * @param sortStr           排序字段名称
     * @return
     */
    private  List<? extends Object> getListCommon(Object obj, String[] findKeys, Object[] findValues, String collectionName,boolean desc,String sortStr) {
        Criteria criteria = null;
        for (int i = 0; i < findKeys.length; i++) {
            if (i == 0) {
                criteria = Criteria.where(findKeys[i]).is(findValues[i]);
            } else {
                criteria.and(findKeys[i]).is(findValues[i]);
            }
        }
        Query query = Query.query(criteria);
        if(sortStr!="") {
            if (desc)
                query.with(Sort.by(Sort.Direction.DESC, sortStr));
            else
                query.with(Sort.by(Sort.Direction.ASC,sortStr));
        }
        List<? extends Object> resultList = null;
        if (collectionName != "") {
            resultList = mongoDBUtils.mongoTemplates.find(query, obj.getClass(), collectionName);
        } else {
            resultList = mongoDBUtils.mongoTemplates.find(query, obj.getClass());
        }
        return resultList;
    }

    /***
     * 根据条件查询出符合的第一条数据，集合为数据对象中@Document 注解所配置的collection
     * @param obj        数据对象
     * @param findKeys   查询条件key
     * @param findValues 查询 条件value
     * @return
     */
    public    Object findOne(Object obj,String[] findKeys,Object[] findValues){
        return getFindCommon(obj, findKeys, findValues,"");
    }
    /***
     * 指定集合， 根据条件查询出符合的第一条数据
     * @param obj        数据对象
     * @param findKeys   查询条件key
     * @param findValues 查询 条件value
     * @return
     */
    public    Object findOne(Object obj,String[] findKeys,Object[] findValues,String collectionName){
        return getFindCommon(obj, findKeys, findValues,collectionName);
    }

    /***
     * 查询公共方法
     * @param obj
     * @param findKeys
     * @param findValues
     * @param collectionName
     * @return
     */
    private  Object getFindCommon(Object obj, String[] findKeys, Object[] findValues,String collectionName) {
        Criteria criteria=null;
        for (int i = 0; i < findKeys.length; i++) {
            if(i==0){
                criteria=Criteria.where(findKeys[i]).is(findValues[i]);
            }else {
                criteria.and(findKeys[i]).is(findValues[i]);
            }
        }
        Query query = Query.query(criteria);
        Object resultObj=null;
        if(collectionName=="") {
            resultObj=  mongoDBUtils.mongoTemplates.findOne(query, obj.getClass());
        }
        else {
            resultObj=mongoDBUtils.mongoTemplates.findOne(query, obj.getClass(), collectionName);
        }
        return  resultObj;
    }

    /***
     * 查询出所有结果集 集合为数据对象中@Document 注解所配置的collection
     * @param obj  数据对象
     * @return
     */
    public    List<? extends  Object> findAll(Object obj){
        List<? extends Object > resultList = mongoDBUtils.mongoTemplates.findAll(obj.getClass());
        return  resultList;
    }
    /***
     * 指定集合 查询出所有结果集
     * @param obj  数据对象
     * @param  collectionName 集合名
     * @return
     */
    public    List<? extends  Object> findAll(Object obj,String collectionName){
        List<? extends Object > resultList = mongoDBUtils.mongoTemplates.findAll(obj.getClass(),collectionName);
        return  resultList;
    }
    /***
     *  批量新增
     * @param objList         批量新增的内容
     * @param collectionName 集合名称
     * @return
     */
    public int insertManyData(List<? extends Object> objList,String collectionName){
        if(CollectionUtils.isEmpty(objList)){
            return 0;
        }
        //插入数据
        // BulkNode 有两种形式 一种遇到错误就会全部中断。一种是对的都执行完成，错误的就执行失败
        BulkOperations bulkOptions=mongoDBUtils.mongoTemplates.bulkOps(BulkOperations.BulkMode.UNORDERED,collectionName);
        bulkOptions.insert(objList);
        BulkWriteResult result = bulkOptions.execute();
        int insertCount=result.getInsertedCount();
        return  insertCount;
    }
    /***
     * 批量修改
     */
    public int updateManyData(List<? extends Object> objList,String collectionName ){
        if(CollectionUtils.isEmpty(objList)){
            return  0; //表示集合不存在
        }
        BulkOperations bulkOperations = mongoDBUtils.mongoTemplates.bulkOps(BulkOperations.BulkMode.UNORDERED, collectionName);
        //反射：
        //获取类的成员变量，包含继承的父类成员变量
        Class cls=objList.getClass();
        //获取类的成员变量，包含继承的父类成员变量
        //Field[] fieldCoins=cls.getFields();
        //获取类的成员变量，不包含继承的父类成员变量
        //Field[] dlField = cls.getDeclaredFields();
        try {
            System.out.println("======================================");
            for (Object o : objList) {
                // System.out.println(o.getClass().getDeclaredFields());
                System.out.println("item:"+o.getClass());
                for (Field field : o.getClass().getDeclaredFields()) {
                    System.out.println(field.getName());
                }
            }
            System.out.println("======================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        objList.forEach(item->{
//            bulkOperations.up));
//        });
        return  0;

    }

}
