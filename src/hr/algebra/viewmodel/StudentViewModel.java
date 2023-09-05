/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.model.Student;
import hr.algebra.model.StudentSubject;
import java.util.Collection;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 *
 * @author Domagoj
 */
public class StudentViewModel {
    
    private final Student student;
    
    

    public StudentViewModel(Student student) {
        if (student == null) {
            
            student = new Student(0,"","",0,"");
        }
        
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
    
    public IntegerProperty getIdStudentProperty(){
        return new SimpleIntegerProperty(student.getIDStudent());
    }
    
    public IntegerProperty getAgeProperty(){
        return new SimpleIntegerProperty(student.getAge());
    }
    
    public StringProperty getFirstNameProperty(){
        return new SimpleStringProperty(student.getFirstName());
    }
    
    public StringProperty getLastNameProperty(){
        return new SimpleStringProperty(student.getLastName());
    }
    
    public StringProperty getEmailProperty(){
        return new SimpleStringProperty(student.getEmail());
    }

    public ObjectProperty<byte[]> getPictureProperty(){
        return new SimpleObjectProperty<>(student.getPicture());
    }
    
     public SimpleObjectProperty<Collection<StudentSubject>> getSubjectCollectionProperty() {
        return new SimpleObjectProperty<>(student.getStudentSubjectCollection());
    }
}
