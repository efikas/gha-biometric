package partial;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author latyf
 */
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Pupil {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleObjectProperty streamImage;

    public Pupil(int id, String name, Object streamImage)  {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.streamImage = new SimpleObjectProperty(streamImage);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int ID) {
        this.id.set(ID);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String nme) {
        this.name.set(nme);
    }

    public Object getImage() {
        return streamImage.get();
    }

    public void setAge(Object streamImage) {
        this.streamImage.set(streamImage);
    }

    @Override
    public String toString() {
        //return "id: " + id.get() + " - " + "name: " + name.get()+ "age: "+ age.get();
        return "";
    }

}
