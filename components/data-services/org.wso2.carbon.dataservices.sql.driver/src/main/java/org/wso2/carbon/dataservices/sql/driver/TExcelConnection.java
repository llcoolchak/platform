/*
 *  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.carbon.dataservices.sql.driver;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.wso2.carbon.dataservices.sql.driver.parser.Constants;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class TExcelConnection extends TConnection {

    private Workbook workbook;

    public TExcelConnection(Properties props) throws SQLException {
        super(props);
        String filePath = (String) props.get(Constants.DRIVER_PROPERTIES.FILE_PATH);
        this.workbook = this.createConnectionToExcelDocument(filePath);
    }

    /**
     * Creates a connection to the given Excel document and returns a workbook instance
     *
     * @param filePath Path to the Excel file
     * @return Instance of workbook class containing which represents a database in the
     *         world of SQL
     * @throws java.sql.SQLException SQLException
     */
    private Workbook createConnectionToExcelDocument(String filePath) throws SQLException {
        Workbook workbook;
        try {
            InputStream fin = new FileInputStream(new File(filePath));
            workbook = new HSSFWorkbook(fin);
        } catch (FileNotFoundException e) {
            throw new SQLException("Could not locate the EXCEL datasource in the provided " +
                    "location", e);
        } catch (IOException e) {
            throw new SQLException("Error occurred while initializing the EXCEL datasource", e);
        }
        return workbook;
    }

    public Workbook getWorkbook() {
        return workbook;
    }
    
    public Statement createStatement(String sql) throws SQLException {
        return new TPreparedStatement(this, sql);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return new TPreparedStatement();
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new TPreparedStatement(this, sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException("CallableStatements are not supported");
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency) throws SQLException {
        return new TPreparedStatement(this, sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
                                         int resultSetConcurrency) throws SQLException {
        throw new SQLFeatureNotSupportedException("CallableStatements are not supported");
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency,
                                              int resultSetHoldability) throws SQLException {
        return new TPreparedStatement(this, sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
                                         int resultSetHoldability) throws SQLException {
        throw new SQLFeatureNotSupportedException("CallableStatements are not supported");  
    }

    @Override
    public PreparedStatement prepareStatement(String sql,
                                              int autoGeneratedKeys) throws SQLException {
        return new TPreparedStatement(this, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql,
                                              int[] columnIndexes) throws SQLException {
        return new TPreparedStatement(this, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql,
                                              String[] columnNames) throws SQLException {
        return null;  
    }


}
