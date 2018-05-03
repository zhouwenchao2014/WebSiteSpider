package cn.myhomespace.zhou.object;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

/**
 * Created by zhouw on 2018/4/30.
 */
public class SpiderProjectManage {
    private int id;
    private String name;
    private String displayName;
    private String rootUrl;
    private String config;
    private String createdBy;
    private String modifiedBy;
    private Date createdTime;
    private Date modifiedTime;

    public SpiderProjectManage(int id, String name, String displayName, String rootUrl, String config, String createdBy, String modifiedBy, Date createdTime, Date modifiedTime) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.rootUrl = rootUrl;
        this.config = config;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public SpiderProjectManage( String name, String displayName, String rootUrl, String config, String createdBy, String modifiedBy, Date createdTime, Date modifiedTime) {
        this.name = name;
        this.displayName = displayName;
        this.rootUrl = rootUrl;
        this.config = config;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public SpiderProjectManage( String name, String displayName, String rootUrl, String config) {
        this.name = name;
        this.displayName = displayName;
        this.rootUrl = rootUrl;
        this.config = config;
    }

    public SpiderProjectManage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public List<SetParam> buildWhereParams(){
        List<SetParam> setParams = new ArrayList<>();
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for(Field field : declaredFields){
            String name = field.getName();
            field.setAccessible(true);
            try {
                Object o = field.get(this);
                if(o!=null&& !StringUtils.isEmpty(o.toString())){
                    SetParam whereParam = new SetParam(name,"=",o.toString());
                    setParams.add(whereParam);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return setParams;
    }

    public SpiderProjectManage buildSpiderProjectManageFromArray(Object[] obj){
        SpiderProjectManage spiderProjectManage = new SpiderProjectManage();
        Field[] declaredFields = spiderProjectManage.getClass().getDeclaredFields();
        int length = declaredFields.length;
        for(int i=0;i<length;i++){
            Field declaredField = declaredFields[i];
            if(obj[i]==null){
               continue;
            }
            String s = obj[i].toString();
            if(declaredField.getType().getName().equals("int")){
                obj[i]=Integer.parseInt(s);
            }else if(declaredField.getType().getName().equals("java.lang.Integer")){
                obj[i]=Integer.parseInt(s);
            }else if(declaredField.getType().getName().equals("java.sql.Date")){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long time=0L;
                try {
                    s = s.substring(0, s.length() - 2);
                    java.util.Date parse = simpleDateFormat.parse(s);
                    time = parse.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date = new Date(time);
                obj[i]=date;
            }
            declaredField.setAccessible(true);
            try {
                declaredField.set(spiderProjectManage,obj[i]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return spiderProjectManage;
    }

    @Override
    public String toString() {
        return "SpiderProjectManage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", rootUrl='" + rootUrl + '\'' +
                ", config='" + config + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", createdTime=" + createdTime +
                ", modifiedTime=" + modifiedTime +
                '}';
    }
}
