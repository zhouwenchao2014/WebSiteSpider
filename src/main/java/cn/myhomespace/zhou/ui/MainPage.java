package cn.myhomespace.zhou.ui;

import cn.myhomespace.zhou.db.JDBCConnection;
import cn.myhomespace.zhou.object.SetParam;
import cn.myhomespace.zhou.object.SpiderProjectManage;
import cn.myhomespace.zhou.object.TableManageDo;
import cn.myhomespace.zhou.object.WhereParam;
import cn.myhomespace.zhou.utils.TableUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.*;
import java.util.List;


/**
 * Created by zhouw on 2018/4/29.
 */
public class MainPage {
    private final int WIDTH=900;
    private final int HEIGHT=600;

    private final SpiderProjectManage DEFAULT_SPIDER_PROJECT_MANAGE=new SpiderProjectManage("dyg","电影港","http://www.dygang.net/","{}");;

    public void init(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        final JFrame main = new JFrame("网页蜘蛛");
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setSize(WIDTH,HEIGHT);
//        main.setLocationRelativeTo(null);

        //主菜单栏
        JMenuBar jMenuBar = new JMenuBar();

        //项目菜单栏
        //---------项目菜单栏开始
        JMenu webProject = new JMenu("网页项目");
        jMenuBar.add(webProject);

        JMenuItem jMenuItem = new JMenuItem("爬取页面管理");
        webProject.add(jMenuItem);

        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSpiderWebSiteManage(main);
            }
        });

        //---------项目菜单栏结束

        main.setJMenuBar(jMenuBar);
        // 设置界面可见
        main.setVisible(true);
    }

    private void showSpiderWebSiteManage(final JFrame main) {

        final TableManageDo tableManageDo = JDBCConnection.queryResultFormatClass(SpiderProjectManage.class,TableUtils.TABLE_SPIDER_PROJECT_MANAGE, 10);

        JPanel controllerPanel = new JPanel();
        controllerPanel.setBounds(0,0,WIDTH,30);


        final JTable manageTable = new JTable(tableManageDo.getObj(),tableManageDo.getColumnNames());
        manageTable.setBounds(0,200,640,300);
        JButton newProject = new JButton();
        newProject.setBounds(0,0,150,30);

        JButton updateProject = new JButton();
        updateProject.setBounds(150,0,150,30);
        updateProject.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = manageTable.getSelectedRow();
                if(selectedRow==-1){
                    JOptionPane.showMessageDialog(null,"请选择数据","错误",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Object valueAt = manageTable.getValueAt(selectedRow, 0);
                if(valueAt==null){
                    JOptionPane.showMessageDialog(null,"请选择数据","错误",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                List<WhereParam> whereParams = new ArrayList<>();
                WhereParam whereParam = new WhereParam("id","=",valueAt.toString());
                whereParams.add(whereParam);
                TableManageDo tableManageDo1 = JDBCConnection.queryResultFormatClass(SpiderProjectManage.class, whereParams, TableUtils.TABLE_SPIDER_PROJECT_MANAGE);
                Object[][] objs = tableManageDo1.getObj();
                SpiderProjectManage spiderProjectManage = new SpiderProjectManage();
                for(Object[] obj : objs){
                    spiderProjectManage=spiderProjectManage.buildSpiderProjectManageFromArray(obj);
                }

                showAddPage(spiderProjectManage,main,manageTable);
            }
        });
        updateProject.setText("更新爬取根页面");
        JButton deleteProject = new JButton();
        deleteProject.setBounds(300,0,150,30);
        deleteProject.setBounds(150,0,150,30);
        deleteProject.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = manageTable.getSelectedRow();
                if(selectedRow==-1){
                    JOptionPane.showMessageDialog(null,"请选择数据","错误",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Object valueAt = manageTable.getValueAt(selectedRow, 0);
                if(valueAt==null){
                    JOptionPane.showMessageDialog(null,"请选择数据","错误",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                List<WhereParam> whereParams = new ArrayList<>();
                WhereParam whereParam = new WhereParam("id","=",valueAt.toString());
                whereParams.add(whereParam);
                int i = JOptionPane.showConfirmDialog(null, "是否删除当前这条记录");
                if(i==0){
                    boolean b = JDBCConnection.deleteByParam(whereParams, TableUtils.TABLE_SPIDER_PROJECT_MANAGE);
                    if(b){
                        TableManageDo tableManageDo = JDBCConnection.queryResultFormatClass(SpiderProjectManage.class, TableUtils.TABLE_SPIDER_PROJECT_MANAGE, 10);
                        TableModel tableModel = new DefaultTableModel(tableManageDo.getObj(),tableManageDo.getColumnNames());
                        JOptionPane.showMessageDialog(null,"删除成功","成功",JOptionPane.INFORMATION_MESSAGE);

                        manageTable.setModel(tableModel);
                        manageTable.validate();
                        manageTable.updateUI();
                        main.repaint();
                    }
                }

            }
        });
        deleteProject.setText("删除爬取根页面");

        controllerPanel.add(newProject);
        controllerPanel.add(updateProject);
        controllerPanel.add(deleteProject);
        main.add(controllerPanel);
        controllerPanel.setLayout(new FlowLayout());
        controllerPanel.setVisible(true);
        newProject.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddPage(DEFAULT_SPIDER_PROJECT_MANAGE,main,manageTable);
            }
        });
        newProject.setText("新增爬取根页面");
        main.setLayout(new FlowLayout());
        main.add(manageTable);
        main.repaint();
    }

    private void showAddPage(final SpiderProjectManage spiderProjectManage, final JFrame main, final JTable table) {
        final JFrame addFrame = new JFrame("新增");
        addFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        addFrame.setSize(400,300);
        addFrame.setLayout(new GridLayout(5,2,5,5));

        JLabel name = new JLabel("根网页标识:");
        name.setBounds(0,0,100,30);
        final JTextField nameText = new JTextField();
        nameText.setText(spiderProjectManage.getName());
        nameText.setBounds(100,0,300,30);

        JLabel displayName = new JLabel("根网页中文名:");
        displayName.setBounds(0,30,100,30);
        final JTextField displayNameText = new JTextField();
        displayNameText.setText(spiderProjectManage.getDisplayName());
        displayNameText.setBounds(100,30,300,30);

        JLabel url = new JLabel("根网页地址:");
        url.setBounds(0,60,100,30);
        final JTextField urlText = new JTextField();
        urlText.setText(spiderProjectManage.getRootUrl());
        urlText.setBounds(100,60,300,30);

        JLabel config = new JLabel("根网页爬取配置:");
        config.setBounds(0,90,100,30);
        final JTextField configText = new JTextField();
        configText.setText(spiderProjectManage.getConfig());
        configText.setBounds(100,90,300,30);

        JButton cancel = new JButton();
        cancel.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFrame.setVisible(false);
            }
        });
        cancel.setText("取消");
        JButton ok = new JButton();
        ok.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameText.getText();
                if(StringUtils.isEmpty(name)){
                    JOptionPane.showMessageDialog(null,"参数错误","根网页不能为空",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String displayNameText_val = displayNameText.getText();
                if(StringUtils.isEmpty(displayNameText_val)){
                    JOptionPane.showMessageDialog(null,"参数错误","根网页中文名",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String urlText_val = urlText.getText();
                if(StringUtils.isEmpty(urlText_val)){
                    JOptionPane.showMessageDialog(null,"参数错误","根网页地址",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String configText_val = configText.getText();
                if(StringUtils.isEmpty(configText_val)){
                    JOptionPane.showMessageDialog(null,"参数错误","根网页爬取配置",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Date date = new Date(System.currentTimeMillis());
                int id = spiderProjectManage.getId();

                SpiderProjectManage spiderProjectManage_new = new SpiderProjectManage(id,name,displayNameText_val,urlText_val,configText_val,"","",date,date);
                boolean spider_project_manage=false;
                if(id==0){
                    spider_project_manage = JDBCConnection.insertResultFormatClass(spiderProjectManage_new, TableUtils.TABLE_SPIDER_PROJECT_MANAGE);
                }else {
                    List<SetParam> setParams = spiderProjectManage_new.buildWhereParams();
                    List<WhereParam> whereParams = new ArrayList<>();
                    WhereParam whereParam = new WhereParam("id","=",id+"");
                    whereParams.add(whereParam);
                    spider_project_manage = JDBCConnection.updateResultFormatClass(setParams, whereParams,TableUtils.TABLE_SPIDER_PROJECT_MANAGE);
                }
                if(spider_project_manage){
                    addFrame.setVisible(false);
                    TableManageDo tableManageDo = JDBCConnection.queryResultFormatClass(SpiderProjectManage.class,TableUtils.TABLE_SPIDER_PROJECT_MANAGE, 10);
                    TableModel tableModel = new DefaultTableModel(tableManageDo.getObj(),tableManageDo.getColumnNames());
                    String message = "新增成功";
                    if(id!=0){
                        message = "修改成功";
                    }
                    JOptionPane.showMessageDialog(null,"新增成功","成功",JOptionPane.INFORMATION_MESSAGE);

                    table.setModel(tableModel);
                    table.repaint();
                    main.repaint();
                }
            }
        });
        ok.setText("保存");


        addFrame.add(name);
        addFrame.add(nameText);
        addFrame.add(displayName);
        addFrame.add(displayNameText);
        addFrame.add(url);
        addFrame.add(urlText);
        addFrame.add(config);
        addFrame.add(configText);
        addFrame.add(cancel);
        addFrame.add(ok);


        addFrame.repaint();
        // 设置界面可见
        addFrame.setVisible(true);
    }

}
