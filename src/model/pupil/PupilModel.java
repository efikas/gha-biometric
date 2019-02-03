/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.pupil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Model;
import model.MysqlConnect;
import model.models.Pupil;
import partial.Partial;

/**
 *
 * @author USER
 */
public class PupilModel {
    
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
    model.MysqlConnect mysqlConnent = new MysqlConnect();
    
    public void addPupilRecord(Map<String, Object> record) throws FileNotFoundException{
        this.connect = this.mysqlConnent.connect();
        
        try {
            File file = (File) record.get("image");
            FileInputStream fin = new FileInputStream(file);
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect.prepareStatement("insert into gha_record.pupil values (default, ?, ?, ?, ? , ?, ?)");
             
            // Parameters start with 1
            preparedStatement.setString(1, (String) record.get("firstName"));
            preparedStatement.setString(2, (String) record.get("middleName"));
            preparedStatement.setString(3, (String) record.get("lastName"));
            preparedStatement.setInt(4, (Integer) record.get("class"));
            preparedStatement.setInt(5, (Integer) record.get("arm"));
            preparedStatement.setBinaryStream(6, (InputStream)fin,(int)file.length());
            preparedStatement.setString(7, (String) record.get("comment"));
//            preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
            preparedStatement.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //disconnect
       this.mysqlConnent.disconnect(this.connect);        
    }
    
    public int updatePupilRecord(int pupilId, Map<String, Object> record) throws FileNotFoundException{
        this.connect = this.mysqlConnent.connect();
        int response = 0;
        
        try {
            File file = (File) record.get("image");
            FileInputStream fin = new FileInputStream(file);
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect.prepareStatement("UPDATE gha_record.pupil "
                    + " SET firstname = ?, middlename = ?, lastname = ?, class = ?, arm = ?, image = ?, comment = ? "
                    + " WHERE id = ?");
             
            // Parameters start with 1
            preparedStatement.setString(1, (String) record.get("firstName"));
            preparedStatement.setString(2, (String) record.get("middleName"));
            preparedStatement.setString(3, (String) record.get("lastName"));
            preparedStatement.setInt(4, (Integer) record.get("class"));
            preparedStatement.setInt(5, (Integer) record.get("arm"));
            preparedStatement.setBinaryStream(6, (InputStream)fin,(int)file.length());
            preparedStatement.setString(7, (String) record.get("comment"));
            preparedStatement.setInt(8, pupilId);
//            preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
           response = preparedStatement.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if (response > 0) {
            this.mysqlConnent.disconnect(this.connect);   
           return 1;
        }
        
       this.mysqlConnent.disconnect(this.connect);   
       return 0;
    }
   
    public List<Pupil> fetchAllPupils() throws Exception{
        this.connect = this.mysqlConnent.connect();
        
         List parentList = new LinkedList();
        
        String query = "SELECT id, firstname, middlename, lastname, class, arm FROM gha_record.pupil";
        preparedStatement = connect.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
         
        while (resultSet.next()) {
            Integer id = resultSet.getInt("id");
            String name = resultSet.getString("firstname") + " " 
                    + resultSet.getString("lastname") + " " + resultSet.getString("middlename");
            Integer pupilClass = resultSet.getInt("class");
            Integer classArm = resultSet.getInt("arm");
            parentList.add(new Pupil(id, name, pupilClass, classArm));
            
        }
       
        //disconnect
        this.mysqlConnent.disconnect(this.connect); 
        
        return parentList;
     }
     
    public Map<String, Object> getPupils(int pupilClass, int classArm) throws Exception{
        Map<String, Object> pupilsData = new HashMap<String, Object>();
        
        this.connect = this.mysqlConnent.connect();
        
        String query = "SELECT * FROM gha_record.pupil WHERE class = ? AND arm = ?";
        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setInt(1, pupilClass);
        preparedStatement.setInt(2, classArm);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        List<Object> pupilsInfo = new ArrayList<Object>();
        ObservableList<String> pupilsNames = FXCollections.observableArrayList("");
        
        
        while (resultSet.next()) {
            
            Map<String, Object> data = new HashMap<String, Object>();
            
            int id = resultSet.getInt("id");
            String fullname = resultSet.getString("firstname") + " " +
                            resultSet.getString("middlename") + " " +
                            resultSet.getString("lastname");
            Blob image = resultSet.getBlob("image");
            
            //:todo -- pass the blog image to the dialog controller
            
            data.put("id", id);
            data.put("fullname", fullname);
            data.put("image", image);
            pupilsInfo.add(data);
            
            pupilsNames.add(fullname);
        }
        
        pupilsData.put("pupilsInfo", pupilsInfo);
        pupilsData.put("pupilsNames", pupilsNames);
        
        return pupilsData;
     }
    
    public  List<Map<String, Object>> getParentWards(int parentId) throws Exception{
        List<Map<String, Object>> pupilsInfo = new ArrayList<>();
        Partial partial = new Partial();
        
        this.connect = this.mysqlConnent.connect();
        
        String query = "SELECT * FROM gha_record.parent_pupil WHERE parentId = ?";
        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setInt(1, parentId);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            //fetch the student information
            String wardQuery = "SELECT * FROM gha_record.pupil WHERE id = ?";
            preparedStatement = connect.prepareStatement(wardQuery);
            preparedStatement.setInt(1, resultSet.getInt("pupilId"));
            ResultSet wardResultSet = preparedStatement.executeQuery();
            
             while (wardResultSet.next()) {
                 Map<String, Object> data = new HashMap<>();
            
                int id = wardResultSet.getInt("id");
                String fullname = wardResultSet.getString("firstname") + " " +
                                wardResultSet.getString("middlename") + " " +
                                wardResultSet.getString("lastname");
                String pupilClass = partial.populateClass().get(wardResultSet.getInt("class")) + " " 
                    + partial.populateClassArm().get(wardResultSet.getInt("arm"));
                byte[] image = wardResultSet.getBytes("image");
                String comment = wardResultSet.getString("comment");

                data.put("id", id);
                data.put("fullname", fullname);
                data.put("pupilClass", pupilClass);
                data.put("image", image);
                data.put("comment", comment);
                pupilsInfo.add(data);
            }
        }
        //disconnect
        this.mysqlConnent.disconnect(this.connect); 
        
        return pupilsInfo;
     }
    
    public Map<String, Object> getPupil(int pupilId) throws Exception{
        Map<String, Object> pupilData = new HashMap<String, Object>();
        
        this.connect = this.mysqlConnent.connect();
        
        String query = "SELECT * FROM gha_record.pupil WHERE id = ?";
        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setInt(1, pupilId);
        
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("firstname");
            String middleName = resultSet.getString("middlename");
            String lastName = resultSet.getString("lastname");
            int _class = resultSet.getInt("class");
            int classArm = resultSet.getInt("arm");
            byte[] image = resultSet.getBytes("image");
            String comment = resultSet.getString("comment");
            
            pupilData.put("id", id);
            pupilData.put("firstname", firstName);
            pupilData.put("middlename", middleName);
            pupilData.put("lastname", lastName);
            pupilData.put("pupil_class", _class);
            pupilData.put("class_arm", classArm);
            pupilData.put("image", image);
            pupilData.put("comment", comment);
        }
        
        return pupilData;
     }
    
}
