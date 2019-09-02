package com.bjt.xint.mapper;


import com.bjt.xint.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Mapper
public interface UserMapper {

    @Insert("insert into user(name, accountId, token, gmtCreate, gmtModified) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);
}
