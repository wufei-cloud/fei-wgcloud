package com.wgcloud.service;

import com.wgcloud.entity.LoginSet;
import com.wgcloud.entity.RegisterSet;
import com.wgcloud.mapper.LoginMapper;
import com.wgcloud.mapper.RegisterMapper;
import com.wgcloud.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterServer {

    public void save(RegisterSet registerSet) throws Exception{
        registerSet.setId(UUIDUtil.getUUID());
        registerSet.setUsername(registerSet.getUsername().trim());
        registerSet.setPassword(registerSet.getPassword().trim());
        registerMapper.save(registerSet);
    }
    public List<RegisterSet> selectUserPass(String username) throws Exception{
        return registerMapper.selectUserPass(username);
    }

    @Autowired
    private RegisterMapper registerMapper;
}
