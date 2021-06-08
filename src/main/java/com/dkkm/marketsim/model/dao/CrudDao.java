package com.dkkm.marketsim.model.dao;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface CrudDao<T> {

    T addMember(T member);
    T getMemberById(int id);
    List<T> getMembers();
    boolean deleteMemberById(int id);
    boolean updateMember(T member);
}
