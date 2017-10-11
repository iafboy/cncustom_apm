package com.cncustompoc.SingletonSrvcs.repository;


import com.cncustompoc.SingletonSrvcs.domains.DeclarationEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeclarationRepository {
    @Select("select * from Declaration where ID =#{ID}")
    public DeclarationEntity selectDeclarationByID(@Param("ID") String ID);
    @Select("select * from Declaration")
    public List<DeclarationEntity> selectDeclarations();
    @Insert("insert into Declaration(ID,EnterpriseInfo,AttachedInfo,FreightCost,InsuranceCost,OtherCost) values (${ID},#{EnterpriseInfo},#{AttachedInfo},#{FreightCost},#{InsuranceCost},#{OtherCost})")
    public void insertDeclaratio(DeclarationEntity declarationEntity);
    @Update("update Declaration set EnterpriseInfo=#{EnterpriseInfo},AttachedInfo=#{AttachedInfo},FreightCost=#{FreightCost},InsuranceCost=#{InsuranceCost},OtherCost=#{OtherCost} where ID=#{ID}")
    public void updateDeclarationByID(DeclarationEntity declarationEntity);
    @Delete("Delete from Declaration where ID =#{ID}")
    public void deleteDeclarationByID(@Param("ID") String ID);
}
