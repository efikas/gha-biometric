/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.parent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
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
     public List<Pupil> fetchAllPupils() throws Exception{
        this.connect = this.mysqlConnent.connect();
        
         List parentList = new LinkedList();
        
        String query = "SELECT id, firstname, middlename, lastname, class, arm FROM gha_record.pupil";
        preparedStatement = connect.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
         
        int counter = 1;
        while (resultSet.next()) {
            Integer id = resultSet.getInt("id");
            Integer serialNo = counter;
            String name = resultSet.getString("firstname") + " " 
                    + resultSet.getString("lastname") + " " + resultSet.getString("middlename");
            Integer pupilClass = resultSet.getInt("class");
            Integer classArm = resultSet.getInt("arm");
            parentList.add(new Pupil(serialNo, name, pupilClass, classArm));
            
            counter++;
        }
       
        //disconnect
        this.mysqlConnent.disconnect(this.connect); 
        
        return parentList;
     }
}
