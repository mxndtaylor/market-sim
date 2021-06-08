package com.dkkm.marketsim.model.dao;

import java.util.List;

public interface CrudDao<T> {

    T addMember(T member);
    T getMemberById(int id);
    List<T> getMembers();
    boolean deleteMemberById(int id);
    boolean updateMember(T member);
}
