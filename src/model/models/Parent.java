/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.models;

/**
 *
 * @author USER
 */
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import partial.Partial;

public class Parent {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty phone;
    private SimpleStringProperty relationship;
    private SimpleObjectProperty image;
    private SimpleObjectProperty rightThumb;
    private SimpleObjectProperty leftThumb;
    Partial partial = new Partial();
    
    public Parent(int id, String name, String phone, String relationship, Object image,
            Object rightThumb, Object leftThumb){
       this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.relationship = new SimpleStringProperty(relationship);
        this.image = new SimpleObjectProperty(id);
        this.rightThumb = new SimpleObjectProperty(rightThumb);
        this.leftThumb = new SimpleObjectProperty(leftThumb);
    }
    
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
    
    public String getRelationship() {
        return relationship.get();
    }
    
    public void setRelationship(String rel) {
        this.relationship.set(rel);
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }
    
    public String getPhone() {
        return phone.get();
    }
    
    public Object getImage() {
        return this.image.get();
    }

    public void setImage(Object img) {
        this.image.set(img);
    }

    public Object getRightThumb() {
        return this.rightThumb.get();
    }

    public void setRightThumb(Object rT) {
        this.rightThumb.set(rT);
    }
    
    public Object getLeftThumb() {
        return this.leftThumb.get();
    }

    public void setLeftThumb(Object lT) {
        this.leftThumb.set(lT);
    }
    
}
