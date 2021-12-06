package com.wgcloud.service;

import com.wgcloud.entity.UserPass;
import com.wgcloud.mapper.UserPassMapper;
import com.wgcloud.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserPassServer {
    public void save(UserPass userpass) throws Exception{
        userpass.setId(UUIDUtil.getUUID());
        userpass.setUsername(userpass.getUsername().trim());
        userpass.setPassword(userpass.getPassword().trim());
    }

    public List<UserPass> selectUserPass(String username) throws Exception{
        return userPassMapper.selectUserPass(username);
    }

    @Autowired
    private UserPassMapper userPassMapper;
}
