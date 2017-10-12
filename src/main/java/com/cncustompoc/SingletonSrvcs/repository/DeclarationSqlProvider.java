package com.cncustompoc.SingletonSrvcs.repository;

import java.util.Map;

public class DeclarationSqlProvider {
     public String selectDeclarationView(Map<String, Object> para) {
                return "select * from DeclarationView where 1=1 and "+para.get("ct");
            }
}