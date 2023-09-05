/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao;

import hr.algebra.model.Student;
import java.util.List;

/**
 *
 * @author Domagoj
 */
public interface StudentRepository {
    
    int addStudent(Student data, List<String> selectedSubjects) throws Exception;
    void updateStudent(Student student, List<String> selectedSubjects) throws Exception;
    void deleteStudent(Student student) throws Exception;
    
    Student getStudent(int idStudent) throws Exception;
    List<Student> getStudents() throws Exception;
    
    
    default void release() throws Exception {}
    
}
