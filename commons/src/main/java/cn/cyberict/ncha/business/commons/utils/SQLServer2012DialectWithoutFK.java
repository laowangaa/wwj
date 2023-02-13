package cn.cyberict.ncha.business.commons.utils;

import org.hibernate.dialect.SQLServer2012Dialect;


public class SQLServer2012DialectWithoutFK extends SQLServer2012Dialect {
    @Override
    public String getAddForeignKeyConstraintString(
            String constraintName,
            String[] foreignKey,
            String referencedTable,
            String[] primaryKey,
            boolean referencesPrimaryKey) {
        return " alter column " + foreignKey[0] + " varchar(40) ";
    }
}  
