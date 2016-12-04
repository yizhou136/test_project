package com.zy.nut.relayer.server.dao;



import com.zy.nut.relayer.common.beans.Boker;
import org.apache.ibatis.annotations.Mapper;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/11/28.
 */
//@Repository
//public interface BokerDao extends JpaRepository<Boker, Long>{
@Mapper
public interface BokerDao extends BaseDao<Boker>{

}
