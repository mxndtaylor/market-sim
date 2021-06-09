package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dao.CrudDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * allows for pass-through crud functions in a service
 * @param <M> the managed or Member type
 * @param <K> the look up or Key type
 */
public interface PassThruCrudService<M, K> {

    M addMember(M member);
    M getMemberByKey(K key);
    List<M> getMembers();
    boolean deleteMemberByKey(K key);
    boolean updateMember(M member);
}
