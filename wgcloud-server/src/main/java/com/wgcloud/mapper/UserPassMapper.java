package com.wgcloud.mapper;

import com.wgcloud.entity.UserPass;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;

@Repository
public interface UserPassMapper {
    public List<UserPass> selectUserPass(String username) throws Exception;

    public void save(UserPass userpass) throws Exception;

    public int delete(String[] id ) throws Exception;

    public int update(UserPass user_pass) throws Exception;
}
