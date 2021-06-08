package com.dkkm.marketsim.model.dao;

import java.util.List;

public interface CrudDao<T, K> {

    T addMember(T member);
    T getMemberByKey(K key);
    List<T> getMembers();
    boolean deleteMemberByKey(K key);
    boolean updateMember(T member);
}
