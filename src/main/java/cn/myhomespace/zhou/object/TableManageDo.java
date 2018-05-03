package cn.myhomespace.zhou.object;

import java.io.Serializable;

/**
 * Created by zhouw on 2018/4/30.
 */
public class TableManageDo implements Serializable{
    private String[] columnNames;
    private Object[][] obj;

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public Object[][] getObj() {
        return obj;
    }

    public void setObj(Object[][] obj) {
        this.obj = obj;
    }
}
