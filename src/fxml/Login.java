import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Login {

    @FXML
    private JFXTextField userText;

    @FXML
    private JFXPasswordField passwordText;

    @FXML
    private Button loginBtn;

    @FXML
    void onLogin(ActionEvent event) throws IOException {
        
        dbConnection();
        if(userText.getText().equals("admin") && passwordText.getText().equals("admin")){
            //
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.hide();
            primaryStage.setTitle("GHA Parent");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        else {
            
        }
        
        
    }
    
    private void dbConnection(){
        
        Connection connection = null;
        try {
           // load the sqlite-JDBC driver using the current class loader
           // Class.forName("org.sqlite.jdbc3");
          // create a database connection
          connection = DriverManager.getConnection("jdbc:sqlite:gha.db");
            System.out.println("db conected");
          Statement statement = connection.createStatement();
          statement.setQueryTimeout(30);  // set timeout to 30 sec.

//          statement.executeUpdate("drop table if exists person");
//          statement.executeUpdate("create table person (id integer, name string)");
//          statement.executeUpdate("insert into person values(1, 'leo')");
//          statement.executeUpdate("insert into person values(2, 'yui')");
          ResultSet rs = statement.executeQuery("select * from person");
          while(rs.next()) {
            // read the result set
            System.out.println("name = " + rs.getString("name"));
            System.out.println("id = " + rs.getInt("id"));
          }
        }
        catch(SQLException e){
          // if the error message is "out of memory", 
          // it probably means no database file is found
          System.err.println(e.getMessage());
        }
        finally {
          try {
            if(connection != null)
              connection.close();
          }
          catch(SQLException e){
            // connection close failed.
            System.err.println(e);
          }
        }
    }

}
