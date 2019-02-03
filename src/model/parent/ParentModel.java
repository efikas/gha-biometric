/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.parent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Model;
import model.MysqlConnect;
import model.models.Pupil;

/**
 *
 * @author USER
 */
public class ParentModel {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
    model.MysqlConnect mysqlConnent = new MysqlConnect();
    
     public ResultSet fetchAllParentRecord() throws FileNotFoundException{
        ResultSet rs = null;
        this.connect = this.mysqlConnent.connect();
        
        try {
            rs = connect.createStatement().executeQuery("SELECT * FROM gha_record.parent");
            
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //disconnect
       // this.mysqlConnent.disconnect(this.connect); 
       return rs;
    }
     
    public void addParentRecord(Map<String, Object> record) throws FileNotFoundException{
        this.connect = this.mysqlConnent.connect();
        
        try {
            File file = (File) record.get("image");
            FileInputStream fin = new FileInputStream(file);
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect.prepareStatement("insert into gha_record.parent values (default, ?, ?, ?, ? , ?, ?)", Statement.RETURN_GENERATED_KEYS);
             
            // Parameters start with 1
            preparedStatement.setString(1, (String) record.get("fullname"));
            preparedStatement.setString(2, (String) record.get("mobile"));
            preparedStatement.setString(3, (String) record.get("phone"));
            preparedStatement.setBinaryStream(4, (InputStream)fin,(int)file.length());
            preparedStatement.setBytes(5, (byte[]) record.get("rightThumb"));
            preparedStatement.setBytes(6, (byte[]) record.get("leftThumb"));
            preparedStatement.executeUpdate();
            
            ResultSet rs = preparedStatement.getGeneratedKeys();
            int parentId = 0;
            if(rs.next()){
                parentId = rs.getInt(1);
            }
            
            if(parentId != 0){
                List<Pupil> pupils  = (List<Pupil>)record.get("pupils");

                // Iterating over values only
                for (int i = 0; i < pupils.size(); i++) {
                    
                    if(pupils.get(i) != null){
                        preparedStatement = connect.prepareStatement("insert into gha_record.parent_pupil values (default, ?, ?)");

                        preparedStatement.setInt(1, parentId);
                        preparedStatement.setInt(2, pupils.get(i).getId());
                        preparedStatement.executeUpdate();
                    }
                }
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //disconnect
       this.mysqlConnent.disconnect(this.connect);        
    }
}
