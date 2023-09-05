/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.model.Subject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Domagoj
 */
public class SubjectViewModel {
    
    private final Subject subject;

    public SubjectViewModel(Subject subject) {
        if (subject == null) {
            
            subject = new Subject(0,"");
        }
        this.subject = subject;
    }
    
    public SubjectViewModel(String subjectName) {
        this.subject = new Subject();
        this.subject.setSubjectName(subjectName);
    }


    public Subject getSubject() {
        return subject;
    }
    
    public IntegerProperty getIDSubjectProperty(){
        return new SimpleIntegerProperty(subject.getIDSubject());
    }
    
    public StringProperty getSubjectNameProperty(){
        return new SimpleStringProperty(subject.getSubjectName());
    }
    
     @Override
    public String toString() {
        return subject.getSubjectName(); // Return the subject name as the string representation
    }
    
}
