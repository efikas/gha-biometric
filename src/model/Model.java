/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.MysqlConnect;
import partial.Pupil;

/**
 *
 * @author latyf
 */
public class Model {
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
//            preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
            preparedStatement.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //disconnect
       this.mysqlConnent.disconnect(this.connect);        
    }
    
     public ResultSet fetchAllParentRecord() throws FileNotFoundException{
        ResultSet rs = null;
        this.connect = this.mysqlConnent.connect();
        
        try {
            rs = connect.createStatement().executeQuery("SELECT * FROM gha_record.parent");
//            if(rs.next()){
//                
//                rs.close();
//            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //disconnect
//       this.mysqlConnent.disconnect(this.connect); 
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
        
        //disconnect
        //this.mysqlConnent.disconnect(this.connect); 
        
        return pupilsData;
     }
    
      public List<Pupil> fetchAllPupils() throws Exception{
        List pupilsList = new LinkedList();
        
        this.connect = this.mysqlConnent.connect();
        
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
            pupilsList.add(new Pupil(serialNo, name, pupilClass, classArm));
            
            counter++;
        }
       
        //disconnect
        //this.mysqlConnent.disconnect(this.connect); 
        
        return pupilsList;
     }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void createTables() throws Exception{
        
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/gha?user=root&password=jboy01");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            
            // Result set get the result of the SQL query
            resultSet = statement
                    .executeQuery("select * from feedback.comments");
            
            writeResultSet(resultSet);
          
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect
                    .prepareStatement("insert into  feedback.comments values (default, ?, ?, ?, ? , ?, ?)");
            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
            // Parameters start with 1
            preparedStatement.setString(1, "Test");
            preparedStatement.setString(2, "TestEmail");
            preparedStatement.setString(3, "TestWebpage");
            preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
            preparedStatement.setString(5, "TestSummary");
            preparedStatement.setString(6, "TestComment");
            preparedStatement.executeUpdate();

            preparedStatement = connect
                    .prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from feedback.comments");
            resultSet = preparedStatement.executeQuery();
            writeResultSet(resultSet);

            // Remove again the insert comment
            preparedStatement = connect
            .prepareStatement("delete from feedback.comments where myuser= ? ; ");
            preparedStatement.setString(1, "Test");
            preparedStatement.executeUpdate();

            resultSet = statement
            .executeQuery("select * from feedback.comments");
            writeMetaData(resultSet);

        } catch (Exception e) {
            throw e;
        } finally {
            //close();
        }

    }

    private void writeMetaData(ResultSet resultSet) throws SQLException {
        //  Now get some metadata from the database
        //  Result set get the result of the SQL query

        System.out.println("The columns in the table are: ");

        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
        for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
            System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
        }
    }
    
  
    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            String user = resultSet.getString("myuser");
            String website = resultSet.getString("webpage");
            String summary = resultSet.getString("summary");
            Date date = resultSet.getDate("datum");
            String comment = resultSet.getString("comments");
            System.out.println("User: " + user);
            System.out.println("Website: " + website);
            System.out.println("summary: " + summary);
            System.out.println("Date: " + date);
            System.out.println("Comment: " + comment);
        }
    }

            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
    private void sqlite(ResultSet resultSet) throws SQLException {         
          try{  
          
//          statement.executeUpdate("drop table if exists person");
          statement.executeUpdate("create table users (id integer PRIMARY KEY ASC, name string, password string)");
          
          // pupil table
          statement.executeUpdate("create table pupils (id integer PRIMARY KEY ASC, firstname string, middlename string,"
                  + "lastname string, class string, arm string, imageId string)");
          
          // parent table
          statement.executeUpdate("create table parents (id integer PRIMARY KEY ASC, title string, name string, "
                  + "phone string, mobile, string, imageId string, rightThumb blob, leftThumb blob)");
          
          // parent pupil pivot table
          statement.executeUpdate("create table parent_pupil (id integer PRIMARY KEY ASC, parentId string, pupilId string)");
          
//          statement.executeUpdate("insert into person values('admin', 'admin')");
//          ResultSet rs = statement.executeQuery("select * from admin");
//          while(rs.next()) {
//            // read the result set
//            System.out.println("name = " + rs.getString("name"));
//            System.out.println("id = " + rs.getInt("id"));
//          }
        }
        catch(SQLException e){
          // if the error message is "out of memory", 
          // it probably means no database file is found
          System.err.println(e.getMessage());
        }
        
    }
}
