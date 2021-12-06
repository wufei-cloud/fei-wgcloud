package com.wgcloud.mapper;

import com.wgcloud.entity.LoginSet;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface LoginMapper {
    public List<LoginSet> selectUserPass(String username) throws Exception;

    public void save(LoginSet userpass) throws Exception;

    public int delete(String[] id ) throws Exception;

    public int update(LoginSet user_pass) throws Exception;
}
