package com.dkkm.marketsim.model.dao;

import java.util.List;

/**
 * allows for crud operations in a dao
 * @param <M> the managed or Member type
 * @param <K> the look up or Key type
 */
public interface CrudDao<M, K> {

    M addMember(M member);
    M getMemberByKey(K key);
    List<M> getMembers();
    boolean deleteMemberByKey(K key);
    boolean updateMember(M member);
}
