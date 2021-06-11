package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dao.CrudDao;

import java.util.List;

/**
 * extend this class to get the pass-through methods of your crud
 * @param <M> the managed or Member type
 * @param <K> the look up or Key type
 */
public class PassThruCrudServiceImpl<M, K> implements PassThruCrudService<M, K> {

    protected CrudDao<M, K> dao;

    @Override
    public M addMember(M member) {
        return dao.addMember(member);
    }

    @Override
    public M getMemberByKey(K key) {
        return dao.getMemberByKey(key);
    }

    @Override
    public List<M> getMembers() {
        return dao.getMembers();
    }

    @Override
    public boolean deleteMemberByKey(K key) {
        return dao.deleteMemberByKey(key);
    }

    @Override
    public boolean updateMember(M member) {
        return dao.updateMember(member);
    }
}
