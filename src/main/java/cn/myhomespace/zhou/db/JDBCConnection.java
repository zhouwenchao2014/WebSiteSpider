package cn.myhomespace.zhou.db;

import cn.myhomespace.zhou.object.SetParam;
import cn.myhomespace.zhou.object.SpiderProjectManage;
import cn.myhomespace.zhou.object.TableManageDo;
import cn.myhomespace.zhou.object.WhereParam;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * describe:
 *
 * @author zhouwenchao
 * @date 2018/04/30
 */
public class JDBCConnection {

    private static final String MYSQL_CLASS="com.mysql.jdbc.Driver";

    private static final String ORACLE_CLASS="oracle.jdbc.driver.OracleDriver";

    private static final String DATA_BASE_MYSQL="mysql";

    private static final String DATA_BASE_ORACLE="oracle";

    private static final String DEFAULT_HOST="localhost";

    private static final String DEFAULT_PORT="3306";

    private static final String DEFAULT_DATA_BASE="web_spider";

    private static final String DEFAULT_USER="root";

    private static final String DEFAULT_PASSWORD="zwc160016ZWC2017";

    private static boolean USE_DEFAULT_TYPE=true;

    public static void initUseType(boolean type){
        USE_DEFAULT_TYPE=type;
    }

    public static Connection initParams(String host,String port,String database,String user,String password){
        String url = buildBDUrl(DATA_BASE_MYSQL, host, port, database);
        Connection connection = buildConnection(url, user, password);
        return connection;
    }
    public static Connection initParamsByDefault(){
        Connection connection = initParams(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_DATA_BASE, DEFAULT_USER, DEFAULT_PASSWORD);
        return connection;
    }

    public static Connection buildConnection(String url,String user,String password){
        return buildConnectionByType(DATA_BASE_MYSQL,url,user,password);
    }

    public static Connection buildConnectionByType(String type,String url,String user,String password){
        Connection con = null;
        try {
            switch (type){
                case DATA_BASE_MYSQL:
                    Class.forName(MYSQL_CLASS);
                    break;
                case DATA_BASE_ORACLE:
                    Class.forName(ORACLE_CLASS);
                    break;
            }

            con = DriverManager.getConnection(url,user,password);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public static ResultSet queryBySql(Connection connection , String sql){
        ResultSet resultSet=null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static boolean updateBySql(Connection connection , String sql){
        int i=0;
        Statement statement=null;
        try {
            statement = connection.createStatement();
            i = statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        return i>0;
    }

    public static String buildBDUrl(String type,String host,String port,String database ){
        StringBuilder url= new StringBuilder();
        switch (type){
            case DATA_BASE_MYSQL:
                url.append("jdbc:mysql://");
                url.append(host);
                url.append(":");
                url.append(port);
                url.append("/");
                url.append(database);
                url.append("?useUnicode=true&characterEncoding=utf-8&useSSL=true");
                break;
            case DATA_BASE_ORACLE:
                //jdbc:oracle:thin:@nx6330:1523:orcl
                url.append("jdbc:oracle:thin:@");
                url.append(host);
                url.append(":");
                url.append(port);
                url.append(":");
                url.append(database);
                break;
        }
        return url.toString();
    }

    /**
     *
     * @param clazz
     * @param queryFields 查询的列
     * @param whereParams 查询的条件
     * @param tableName 表名
     * @param querySize 查询的条数
     * @param <T>
     * @return
     */
    public static  <T> TableManageDo queryResultFormatClass(Class<T> clazz,String[] queryFields,List<WhereParam> whereParams,String tableName,int querySize){
        Connection connection=null;
        if(USE_DEFAULT_TYPE){
            connection = initParamsByDefault();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        if(queryFields!=null){
            int length = queryFields.length;
            for(int i=0;i<length;i++){
                String queryField = queryFields[i];
                sql.append(queryField);
                if(i!=length-1){
                    sql.append(",");
                }
            }

        }else {
            sql.append(" * ");
        }
        sql.append("from ");
        sql.append(tableName);
        if(whereParams!=null){
            sql.append(" where ");
            int size = whereParams.size();
            for (int i=0;i<size;i++){
                WhereParam whereParam = whereParams.get(i);
                sql.append(whereParam.getLeft());
                sql.append(whereParam.getOper());
                sql.append(whereParam.getRight());
                if(i!=size-1){
                    sql.append(" and ");
                }
            }
        }
        if(querySize!=0){
            sql.append( " limit 0,"+querySize);
        }
        String s = sql.toString();
        String replace = s.replace("*", "count(1)");
        ResultSet resultSet = queryBySql(connection, s);
        if(querySize==0){
            ResultSet resultSet1 = queryBySql(connection, replace);
            try {
                if(resultSet1.next()) {
                    querySize=resultSet1.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        TableManageDo tableManageDo = new TableManageDo();



        Field[] fields = clazz.getDeclaredFields();
        int length = fields.length;

        Object[][] obj = new Object[querySize][length];
        tableManageDo.setObj(obj);
        String[] columnNames=new String[length];
        tableManageDo.setColumnNames(columnNames);

        for(int i = 0; i< length; i++){
            columnNames[i]=fields[i].getName();
        }
        int row=0;

        try {
            while (resultSet.next()){
                for(int i = 0; i< length; i++){
                    String string = resultSet.getString(fields[i].getName());
                    obj[row][i]=string;
                }
                row=row+1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return tableManageDo;
    }

    public static  <T> TableManageDo queryResultFormatClass(Class<T> clazz,List<WhereParam> whereParams,String tableName,int querySize){
        TableManageDo tableManageDo = queryResultFormatClass(clazz, null, whereParams, tableName, querySize);
        return tableManageDo;
    }

    public static  <T> TableManageDo queryResultFormatClass(Class<T> clazz,List<WhereParam> whereParams,String tableName){
        TableManageDo tableManageDo = queryResultFormatClass(clazz, null, whereParams, tableName, 0);
        return tableManageDo;
    }

    public static  <T> TableManageDo queryResultFormatClass(Class<T> clazz,String tableName,int querySize){
        TableManageDo tableManageDo = queryResultFormatClass(clazz, null, null, tableName, querySize);
        return tableManageDo;
    }

    public static  <T> TableManageDo queryResultFormatClass(Class<T> clazz,String[] queryFields,String tableName,int querySize){
        TableManageDo tableManageDo = queryResultFormatClass(clazz, queryFields, null, tableName, querySize);
        return tableManageDo;
    }

    public static boolean deleteByParam(List<WhereParam> whereParams, String tableName){
        Connection connection=null;
        if(USE_DEFAULT_TYPE){
            connection = initParamsByDefault();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ");
        sql.append(tableName);
        sql.append(" where ");
        int size = whereParams.size();
        for(int i=0;i<size;i++){
            WhereParam whereParam = whereParams.get(i);
            sql.append(whereParam.getLeft());
            sql.append(whereParam.getOper());
            sql.append(whereParam.getRight());
            if(i!=size-1){
                sql.append(" and ");
            }
        }

        int i=0;
        try {
            Statement statement = connection.createStatement();
            i = statement.executeUpdate(sql.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return i>0;
    }

    public static <T> boolean updateResultFormatClass(List<SetParam> setParams,List<WhereParam> whereParams, String tableName){
        Connection connection=null;
        if(USE_DEFAULT_TYPE){
            connection = initParamsByDefault();
        }
        StringBuffer sql = new StringBuffer();
        sql.append("update ");
        sql.append(tableName);

        int size = setParams.size();
        if(size<1){
            return false;
        }else {
            sql.append(" set ");
        }
        for(int i=0;i<size;i++){
            SetParam setParam = setParams.get(i);
            sql.append(setParam.getLeft());
            sql.append(setParam.getOper());
            sql.append("\'"+setParam.getRight()+"\'");
            if(i!=size-1){
                sql.append(",");
            }
        }
        int size1 = whereParams.size();
        if(size1<1){
            return false;
        }else {
            sql.append(" where ");
        }
        for(int i=0;i<size1;i++){
            WhereParam whereParam = whereParams.get(i);
            sql.append(whereParam.getLeft());
            sql.append(whereParam.getOper());
            sql.append("\'"+whereParam.getRight()+"\'");
            if(i!=size1-1){
                sql.append(" and ");
            }
        }
        boolean b = updateBySql(connection, sql.toString());
        return b;

    }

    public static  <T> boolean insertResultFormatClass(T obj,String tableName){
        Connection connection=null;
        if(USE_DEFAULT_TYPE){
            connection = initParamsByDefault();
        }
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ");
        sql.append(tableName);
        sql.append("(");

        StringBuilder values = new StringBuilder();
        values.append("(");
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for(Field field : declaredFields){
            String name = field.getName();

            field.setAccessible(true);
            Object o=null;
            try {
                o = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if(o!=null&& !StringUtils.isEmpty(o.toString())){

                if(!name.equals("id")&&!o.toString().equals("0")){
                    sql.append(name);
                    sql.append(",");
                    values.append("\'"+o+"\'");
                    values.append(",");
                }
            }

        }
        int val_len = values.length();
        int sql_len = sql.length();

        String val = values.substring(0,val_len - 1);
        val=val+")";
        String sqls = sql.substring(0,sql_len - 1);
        sqls=sqls+")";

        String querySql=sqls +"values"+ val;


        boolean resultSet = updateBySql(connection, querySql);


        return resultSet;
    }

//    public static void main(String[] args) {
//        String url = buildBDUrl(DATA_BASE_MYSQL, "localhost", "3306", "web_spider");
//
//        Connection connection = buildConnection(url, "root", "zwc160016ZWC2017");
//        ResultSet resultSet = queryBySql(connection, "select * from web_spider.spider_project_manage");
//        try {
//            while (resultSet.next()){
//                int row = resultSet.getRow();
//                int id = resultSet.getInt(1);
//                String name = resultSet.getString(2);
//                String displayName = resultSet.getString(3);
//                String rootUrl = resultSet.getString(4);
//                String config = resultSet.getString(5);
//                String create_by = resultSet.getString(6);
//                String modified_by = resultSet.getString(7);
//                Date create_time = resultSet.getDate(8);
//                Date modified_time = resultSet.getDate(9);
//
//                SpiderProjectManage spiderProjectManage = new SpiderProjectManage(name,displayName,rootUrl,config,create_by,modified_by,create_time,modified_time);
//                System.out.printf(spiderProjectManage.toString());
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
