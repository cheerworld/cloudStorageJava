package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> getCredentialsByUser(Integer userId);

    @Insert("INSERT INTO CREDENTIALS (url, username, salt, password, userid) VALUES (#{url}, #{username}, #{salt}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, salt = #{salt}, password = #{password} WHERE credentialId = #{credentialId}")
    int update(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    void delete(Integer credentialId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    Credential getCredential(Integer credentialId);
}
