package model.models;

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
import partial.Partial;

public class Pupil {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty pupilClass;
    private SimpleIntegerProperty classArm;
    private SimpleObjectProperty streamImage;
    Partial partial = new Partial();

    public Pupil(int id, String name, Object streamImage)  {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.streamImage = new SimpleObjectProperty(streamImage);
    }
    
    public Pupil(int id, String name, int pupilClass, int classArm)  {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.pupilClass = new SimpleIntegerProperty(pupilClass);
        this.classArm = new SimpleIntegerProperty(classArm);
    }
  
    
    public Pupil(int id, String name, int pupilClass, int classArm, Object streamImage)  {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.pupilClass = new SimpleIntegerProperty(pupilClass);
        this.classArm = new SimpleIntegerProperty(classArm);
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

    public void setName(String name) {
        this.name.set(name);
    }
    
    public int getPupilClass() {
        return pupilClass.get();
    }
    
    public String getPupilClassName() {
        return partial.populateClass().get(pupilClass.get());
    }

    public void setPupilClass(int pupilClass) {
        this.pupilClass.set(pupilClass);
    }
    
    public int getClassArm() {
        return classArm.get();
    }
    
    public String getPupilClassArmName() {
        return partial.populateClassArm().get(classArm.get());
    }

    public void setClassArm(int classArm) {
        this.classArm.set(classArm);
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
