package com.wgcloud.mapper;


import com.wgcloud.entity.DingSet;
import com.wgcloud.entity.MailSet;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DingSetMapper {
    public List<DingSet> selectAllByParms(Map<String, Object> map) throws Exception;

    public void save(DingSet DingSet) throws Exception;

    public int deleteById(String[] id) throws Exception;

    public int updateById(DingSet DingSet) throws Exception;


}
