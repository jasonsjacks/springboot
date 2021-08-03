package com.chan;

import com.chan.pojo.UserInfoDto;

import com.chan.untils.MongoDBUtils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;


import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MongdbSpringbootApplicationTests {
    @Autowired
    private MongoDBUtils mongoDBUtils;
    private UserInfoDto item;

    //新增
    @Test
    public  void  saveMongoTest(){

        //List<UserInfoDto> dtoList =geLitData();
        //单条插入数据
        UserInfoDto infoDto = getUserInfoDto();
        //向mongo中插入数据
        mongoDBUtils.save(infoDto,"userInfo");
        //查询mongodb中插入的数据
        List<? extends Object> infoList = mongoDBUtils.findAll(new UserInfoDto(), "userInfo");
        for (Object o : infoList) {
            System.out.println(o);
        }

    }
    //查询所有的数据
    @Test
    public  void  queryFindAll(){
        List<? extends Object> infoList = mongoDBUtils.findAll(new UserInfoDto(), "userInfo");
        for (Object o : infoList) {
            System.out.println(o);
        }
    }
    //查询
    @Test
    public  void  queryMongodbTest(){
        String name="孙小雷";
        int  id=1;
        // 一般查询
        Query query=new Query();
        //查询条件
        Criteria criteria = Criteria.where("id").is(id);
        criteria.and("name").is(name); //and条件
        String [] findKeys={"id","name"};
        Object[]  findValues={id,name};
        query.addCriteria(criteria);
        UserInfoDto  userInfoDto=(UserInfoDto) mongoDBUtils.findOne(new UserInfoDto(),findKeys,findValues,"userInfo");
        System.out.println("信息输出："+userInfoDto.toString());

    }
    //批量添加
    @Test
    public  void  insertManyMongoTest(){
        List<UserInfoDto>  listData =  geLitData();
        mongoDBUtils.insertManyData(listData,"userInfo");
        queryFindAll();
    }
   //批量修改
   @Test
   public  void  updateManyMongoTest()
   {
       List<UserInfoDto>  listData =  geLitData();
       mongoDBUtils.updateManyData(listData,"userInfo");
   }
     //更新
    @Test
    public  void  updateMongoTest(){
        try {
            UserInfoDto userInfo = new UserInfoDto();
            userInfo.setId(1);
            userInfo.set_id("1");
            userInfo.setName("张小萌：萌萌");
            userInfo.setAge(8);
            String  accordingKey="id";
            Object accordingValues=1;
            String[] updateKeys={"name","age"};
            Object[] updateValues={userInfo.getName(),userInfo.getAge()};
            mongoDBUtils.updateFirst(accordingKey,accordingValues,updateKeys,updateValues,"userInfo");


            System.out.println("更新成功");
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("更新失败:"+exception.getMessage());
        }

    }

    @Test
    public  void  removeMongodbTest()
    {
        String name="李经济局明";
        int  id=52;
        // 一般查询
        Query query=new Query();
        //查询条件
        Criteria criteria = Criteria.where("id").is(id);
        criteria.and("name").is(name); //and条件
        String [] findKeys={"id","name"};
        Object[]  findValues={id,name};
        query.addCriteria(criteria);
        UserInfoDto  userInfoDto=(UserInfoDto) mongoDBUtils.findOne(new UserInfoDto(),findKeys,findValues,"userInfo");
        System.out.println(userInfoDto);

        mongoDBUtils.remove(userInfoDto,"userInfo");
    }



    private UserInfoDto getUserInfoDto() {
        UserInfoDto infoDto = new UserInfoDto();
        infoDto.set_id("2");
        infoDto.setAge(11);
        infoDto.setName("张志红");
        infoDto.setId(2);
        return infoDto;
    }

    private   List<UserInfoDto> geLitData() {
        List<UserInfoDto> dtos = new ArrayList<>();
        UserInfoDto infoDto = new UserInfoDto();
        infoDto.set_id("3");
        infoDto.setAge(28);
        infoDto.setName("陈梦：三少");
        infoDto.setId(3);
        dtos.add(infoDto);
        infoDto = new UserInfoDto();
        infoDto.set_id("4");
        infoDto.setAge(8);
        infoDto.setName("张霖雨：六雨");
        infoDto.setId(4);
        dtos.add(infoDto);
        return dtos;
    }

    @Test
    public  void  test(){
//        mongoDBUtils.add(new UserInfoDto());
    }
    @Test
    void contextLoads() {
    }

}
