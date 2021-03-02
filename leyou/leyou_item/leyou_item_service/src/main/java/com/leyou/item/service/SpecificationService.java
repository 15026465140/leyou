package com.leyou.item.service;

import com.leyou.item.mapper.ParamMapper;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SpecificationService {

    @Autowired
    private ParamMapper paramMapper;

    @Autowired
    private SpecGroupMapper specGroupMapper;

    //根据分类ID查询参数组
    public List<SpecGroup> queryGroupsByCid(Long cid) {

        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);

        List<SpecGroup> groups = specGroupMapper.select(specGroup);

        return groups;
    }

    /**
     * 根据传递的参数查询规格参数
     * @param gid
     * @return
     */
    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching ) {
        SpecParam record = new SpecParam();
        record.setGroupId(gid);
        record.setCid(cid);
        record.setGeneric(generic);
        record.setSearching(searching);
        return this.paramMapper.select(record);
    }
    //根据id删除参数组
    public void deleteGroupByGid(Long id) {
        SpecGroup record = new SpecGroup();
        record.setId(id);
        specGroupMapper.delete(record);
    }
    //修改参数组信息
    public void saveGroup(SpecGroup specGroup) {

        specGroupMapper.updateByPrimaryKey(specGroup);

    }


    public List<SpecGroup> queryGroupWithParam(Long cid) {
        // 查询规格组
        List<SpecGroup> specGroups= this.queryGroupsByCid(cid);
        specGroups.forEach(specGroup -> {
            // 查询组内参数
            List<SpecParam> specParams=this.queryParams(specGroup.getId(), null, null, null);
            specGroup.setParams(specParams);
        });
        return specGroups;
    }

    }

