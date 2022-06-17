package com.atguigu.mybatisplus;

import com.atguigu.mybatisplus.entity.User;
import com.atguigu.mybatisplus.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class MybatisplusApplicationTests {

    @Autowired
    private UserMapper userMapper;

    //查询所有数据
    @Test
    void findAll() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    //插入数据
    @Test
    void testInsert(){

        User user = new User();
        user.setName("luokai");
        user.setAge(20);
        user.setEmail("464132475@qq.com");

        int result = userMapper.insert(user);
        System.out.println(result); //影响的行数
        System.out.println(user); //id自动回填
    }

    //修改操作
    @Test
    void testUpdateById(){

        User user = new User();
        user.setId(1L);
        user.setAge(128);

        int result = userMapper.updateById(user);
        System.out.println(result);

    }

    /**
     * 测试 乐观锁插件，必须先查询再修改
     */
    @Test
    public void testOptimisticLocker() {

        //查询
        User user = userMapper.selectById(1L);
        //修改数据
        user.setName("Goudan");
        user.setEmail("goudan@qq.com");
        //执行更新
        userMapper.updateById(user);
    }

    /**
     * 查询多个id
     */
    @Test
    public void testSelectBatchIds(){

        List<User> users = userMapper.selectBatchIds(Arrays.asList(1L, 2L, 3L));
        users.forEach(System.out::println);
    }

    /**
     * 条件查询，不常用
     */
    @Test
    public void testSelectByMap(){

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Helen");
        map.put("age", 18);
        List<User> users = userMapper.selectByMap(map);

        users.forEach(System.out::println);
    }


    /**
     * 分页
     */
    @Test
    public void testSelectPage() {

        Page<User> page = new Page<>(1,5);
        userMapper.selectPage(page, null);

        page.getRecords().forEach(System.out::println);
        System.out.println(page.getCurrent());
        System.out.println(page.getPages());
        System.out.println(page.getSize());
        System.out.println(page.getTotal());
        System.out.println(page.hasNext());
        System.out.println(page.hasPrevious());
    }

    //根据id删除记录
    @Test
    public void testDeleteById(){

        int result = userMapper.deleteById(1528962758215757825L);
        System.out.println(result);
    }

    //批量删除
    @Test
    public void testDeleteBatchIds() {

        int result = userMapper.deleteBatchIds(Arrays.asList(8, 9, 10));
        System.out.println(result);
    }

    //简单的条件查询删除
    @Test
    public void testDeleteByMap() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Helen");
        map.put("age", 18);

        int result = userMapper.deleteByMap(map);
        System.out.println(result);
    }




}
