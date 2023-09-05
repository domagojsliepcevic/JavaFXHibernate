/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import hr.algebra.dao.SubjectRepository;
import hr.algebra.model.Subject;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Domagoj
 */
public class HibernateSubjectRepository implements SubjectRepository {
    
     @Override
    public void release() throws Exception {
       HibernateFactory.release();
    }
    
    
    @Override
    public int addSubject(Subject data) throws Exception {
        try(EntityManagerWrapper emw = HibernateFactory.getEntityManager()){
            
            EntityManager em = emw.get();
            em.getTransaction().begin();
            Subject subject = new Subject(data);
            em.persist(subject);
            em.getTransaction().commit();
            return subject.getIDSubject();
            
        }
    }

    @Override
    public void updateSubject(Subject subject) throws Exception {
        try(EntityManagerWrapper emw = HibernateFactory.getEntityManager()){
            
            EntityManager em = emw.get();
            em.getTransaction().begin();
            em.find(Subject.class, subject.getIDSubject()).updateData(subject);
            em.getTransaction().commit();

        }
    }

    @Override
    public void deleteSubject(Subject subject) throws Exception {
        try(EntityManagerWrapper emw = HibernateFactory.getEntityManager()){
            
            EntityManager em = emw.get();
            em.getTransaction().begin();
            em.remove(em.contains(subject)? subject : em.merge(subject));
            em.getTransaction().commit();
  
        }
    }

    @Override
    public Subject getSubject(int idSubject) throws Exception {
       try(EntityManagerWrapper emw = HibernateFactory.getEntityManager()){
            
            EntityManager em = emw.get();
            return em.find(Subject.class, idSubject);
            
            
        }
    }

    @Override
    public List<Subject> getSubjects() throws Exception {
         try(EntityManagerWrapper emw = HibernateFactory.getEntityManager()){
            
            EntityManager em = emw.get();
            return em.createNamedQuery(HibernateFactory.SELECT_SUBJECTS).getResultList();

        }
    } 

    @Override
    public List<String> getSubjectNamesByIds(List<Integer> subjectIds) throws Exception {
    try (EntityManagerWrapper emw = HibernateFactory.getEntityManager()) {
        EntityManager em = emw.get();
        
        // Create a query to select subject names by IDs
        String jpql = "SELECT s.subjectName FROM Subject s WHERE s.idSubject IN :subjectIds";
        TypedQuery<String> query = em.createQuery(jpql, String.class);
        query.setParameter("subjectIds", subjectIds);
        
        return query.getResultList();
        }
    }
}
