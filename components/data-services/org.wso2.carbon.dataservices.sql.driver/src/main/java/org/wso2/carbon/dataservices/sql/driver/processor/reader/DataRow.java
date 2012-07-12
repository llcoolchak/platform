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
package org.wso2.carbon.dataservices.sql.driver.processor.reader;

import java.util.List;

public class DataRow {

    private int rowID;

    private List<DataCell> cells;

    public DataRow(int rowID, List<DataCell> cells) {
        this.rowID = rowID;
        this.cells = cells;
    }

    public DataRow(int rowID) {
        this.rowID = rowID;
    }

    public int getRowID() {
        return rowID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    public List<DataCell> getCells() {
        return cells;
    }

    public void setCells(List<DataCell> cells) {
        this.cells = cells;
    }

    public void addCellToRow(DataCell cell) {
        this.getCells().add(cell);
    }

}