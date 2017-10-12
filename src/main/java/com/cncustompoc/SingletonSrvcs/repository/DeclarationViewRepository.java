package com.cncustompoc.SingletonSrvcs.repository;

import com.cncustompoc.SingletonSrvcs.domains.DeclarationViewEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.jdbc.core.SqlProvider;

import java.util.List;

@Mapper
public interface DeclarationViewRepository {
    @Select("select * from DeclarationView")
    public List<DeclarationViewEntity> selectDeclarationViews();
    @SelectProvider(type = DeclarationSqlProvider.class, method = "selectDeclarationView")
    //@ResultMap("DeclarationViewEntity")
    public List<DeclarationViewEntity> selectDeclarationViewsByCriteria(@Param("ct") String ct);
}
