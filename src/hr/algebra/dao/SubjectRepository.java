/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao;

import hr.algebra.model.Subject;
import java.util.List;

/**
 *
 * @author Domagoj
 */
public interface SubjectRepository {
    
    
    int addSubject(Subject data) throws Exception;
    void updateSubject(Subject subject) throws Exception;
    void deleteSubject(Subject subject) throws Exception;
    
    Subject getSubject(int idSubject) throws Exception;
    List<Subject> getSubjects() throws Exception;
    
    // New method declaration for getting subject names by IDs
    List<String> getSubjectNamesByIds(List<Integer> subjectIds) throws Exception;
    
    default void release() throws Exception {}
}
