/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao;

import hr.algebra.dao.sql.HibernateStudentRepository;
import hr.algebra.dao.sql.HibernateSubjectRepository;

/**
 *
 * @author Domagoj
 */
public class RepositoryFactory {

    private RepositoryFactory() {
    } 
    
    private static final StudentRepository STUDENT_REPOSITORY = new HibernateStudentRepository();
    private static final SubjectRepository SUBJECT_REPOSITORY = new HibernateSubjectRepository();
    
    public static StudentRepository getStudentRepository() {
        return STUDENT_REPOSITORY;
    }
    
    public static SubjectRepository getSubjectRepository() {
        return SUBJECT_REPOSITORY;
    }
    
    
}
