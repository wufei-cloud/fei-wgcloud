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

    public void save(DingSet dingSet) throws Exception {
        dingSet.setId(UUIDUtil.getUUID());
        dingSet.setCreateTime(DateUtil.getNowTime());
        dingSet.setFromDingName(dingSet.getFromDingName().trim());
        dingSet.setToPhone(dingSet.getToPhone().trim());
        dingSetMapper.save(dingSet);
    }

    public List<DingSet> selectAllByParms(Map<String, Object> params) throws Exception {
        return dingSetMapper.selectAllByParms(params);
    }


    public int deleteById(String[] id) throws Exception {
        return dingSetMapper.deleteById(id);
    }

    public int updateById(DingSet DingSet) throws Exception {
        return dingSetMapper.updateById(DingSet);
    }

    public List<DingSet> selectByParams(Map<String, Object> params) throws Exception {
        return dingSetMapper.selectAllByParms(params);
    }


    @Autowired
    private DingSetMapper dingSetMapper;
}
