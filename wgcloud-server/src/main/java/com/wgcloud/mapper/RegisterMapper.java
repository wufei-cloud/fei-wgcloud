package com.wgcloud.mapper;

import com.wgcloud.entity.LoginSet;
import com.wgcloud.entity.RegisterSet;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterMapper {
    public List<RegisterSet> selectUserPass(String username) throws Exception;

    public void save(RegisterSet registerSet) throws Exception;

    public int delete(String[] id ) throws Exception;

    public int update(LoginSet user_pass) throws Exception;
}
