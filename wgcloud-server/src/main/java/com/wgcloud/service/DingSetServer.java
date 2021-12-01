package com.wgcloud.service;

import com.wgcloud.entity.DingSet;
import com.wgcloud.mapper.DingSetMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DingSetServer {

    public void save(DingSet DingSet) throws Exception{
        DingSet.setId(UUIDUtil.getUUID());
        DingSet.setCreateTime(DateUtil.getNowTime());
        DingSet.setFromDingName(DingSet.getFromDingName().trim());
        DingSet.setToPhone(DingSet.getToPhone().trim());
        dingSetMapper.save(DingSet);
    }
    public List<DingSet> selectAllByParms(Map<String, Object> params) throws Exception{
        return dingSetMapper.selectAllByParms(params);
    }


    public int deleteById(String[] id) throws Exception{
        return dingSetMapper.deleteById(id);
    }

    public int updateById(DingSet DingSet) throws Exception{
        return dingSetMapper.updateById(DingSet);
    }


    @Autowired
    private DingSetMapper dingSetMapper;
}
