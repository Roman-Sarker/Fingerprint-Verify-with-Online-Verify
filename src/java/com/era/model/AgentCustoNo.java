package com.era.model;

import java.sql.*;

public class AgentCustoNo implements SQLData {

    public String sql_type;
    public long agentCustNO; 

    public AgentCustoNo() {
    } 

    public void setAgentCustNO(long agentCustNO) {
        this.agentCustNO = agentCustNO;
    } 
    
    public String getSQLTypeName() throws SQLException {
        return sql_type;
    } 
     
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        sql_type = typeName;
        agentCustNO = stream.readLong(); 
    }
    
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeLong(agentCustNO); 
    }
    
}

