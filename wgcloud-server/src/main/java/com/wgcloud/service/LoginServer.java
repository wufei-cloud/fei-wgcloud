package com.wgcloud.service;

import com.wgcloud.entity.LoginSet;
import com.wgcloud.mapper.LoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServer {
    public List<LoginSet> selectUserPass(String username) throws Exception{
        return loginMapper.selectUserPass(username);
    }

    @Autowired
    private LoginMapper loginMapper;
}
