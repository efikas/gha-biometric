/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

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
    
     public boolean Register(String fullname, String user, String pass) throws FileNotFoundException{
        this.connect = this.mysqlConnent.connect();
        int response = 0;
        
        try {
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect.prepareStatement("insert into gha_record.users values (default, ?, ?, ?, ?)");
            
            preparedStatement.setString(1, (String) fullname);
            preparedStatement.setString(2, (String) user);
            preparedStatement.setBytes(3, hasher(pass));
            preparedStatement.setInt(4, 2);
            
            response = preparedStatement.executeUpdate();
         
        } catch (InvalidKeySpecException ex) {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(response > 0) return true;
        
        return false;        
     }
     
     public boolean Login(String user, String pass) throws SQLException, 
             InvalidKeySpecException, NoSuchAlgorithmException{
        this.connect = this.mysqlConnent.connect();
        
        String query = "SELECT * FROM gha_record.users WHERE username = ? AND password = ?";
        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setString(1, user);
        preparedStatement.setBytes(2, hasher(pass));
        
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          return true;
        }
        
        return false;
     }
     
     public void ChangePass(Map<String, Object> record) throws FileNotFoundException{
        this.connect = this.mysqlConnent.connect();
       
     }
     
     
     private byte[] hasher(String input) throws InvalidKeySpecException, NoSuchAlgorithmException{
          String _salt = "GHA_PPS";
            byte[] salt = null;
            
            salt = _salt.getBytes();
            KeySpec spec = new PBEKeySpec(input.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            
        return hash;
     }
}