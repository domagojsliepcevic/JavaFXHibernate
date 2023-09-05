/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import hr.algebra.dao.StudentRepository;
import hr.algebra.model.Student;
import hr.algebra.model.StudentSubject;
import hr.algebra.model.Subject;
import java.util.List;
import javax.persistence.EntityManager;


public class HibernateStudentRepository implements StudentRepository {

    @Override
    public void release() throws Exception {
       HibernateFactory.release();
    }

    @Override
    public int addStudent(Student data, List<String> selectedSubjects) throws Exception {
    try (EntityManagerWrapper emw = HibernateFactory.getEntityManager()) {
        EntityManager em = emw.get();
        em.getTransaction().begin();

        // Step 1: Add the student to the Student table
        Student student = new Student(data);
        em.persist(student);

        // Step 2: Associate the student with selected subjects
        for (String subjectName : selectedSubjects) {
            // Query the Subject entity by subject name to get the corresponding subject entity
            Subject subject = em.createQuery("SELECT s FROM Subject s WHERE s.subjectName = :subjectName", Subject.class)
                .setParameter("subjectName", subjectName)
                .getSingleResult();

            // Create a new StudentSubject entity and associate the student and subject
            StudentSubject studentSubject = new StudentSubject();
            studentSubject.setStudentID(student); // Set the student entity
            studentSubject.setSubjectID(subject); // Set the subject entity
            em.persist(studentSubject);
        }

        em.getTransaction().commit();
        return student.getIDStudent();
        }
    }
    
    
//    @Override
//    public void updateStudent(Student student) throws Exception {
//        try(EntityManagerWrapper emw = HibernateFactory.getEntityManager()){
//            
//            EntityManager em = emw.get();
//            em.find(Student.class, student.getIDStudent()).updateData(student);
//
//        }
//    }
    
    @Override
    public void updateStudent(Student updatedStudent, List<String> selectedSubjects) throws Exception {
    try (EntityManagerWrapper emw = HibernateFactory.getEntityManager()) {
        EntityManager em = emw.get();
        em.getTransaction().begin();

        // Find the existing student entity by ID
        Student existingStudent = em.find(Student.class, updatedStudent.getIDStudent());

        if (existingStudent != null) {
            // Update the properties of the existing student entity with the new data
            existingStudent.setFirstName(updatedStudent.getFirstName());
            existingStudent.setLastName(updatedStudent.getLastName());
            existingStudent.setAge(updatedStudent.getAge());
            existingStudent.setEmail(updatedStudent.getEmail());
            existingStudent.setPicture(updatedStudent.getPicture());

            // Update the associated subjects:
            // 1. Clear existing associations
            existingStudent.getStudentSubjectCollection().clear();
            
            // 2. Associate the student with selected subjects
            if (selectedSubjects != null) {
                for (String subjectName : selectedSubjects) {
                    Subject subject = em.createQuery("SELECT s FROM Subject s WHERE s.subjectName = :subjectName", Subject.class)
                        .setParameter("subjectName", subjectName)
                        .getSingleResult();

                    if (subject != null) {
                        StudentSubject studentSubject = new StudentSubject();
                        studentSubject.setStudentID(existingStudent);
                        studentSubject.setSubjectID(subject);
                        existingStudent.getStudentSubjectCollection().add(studentSubject);
                    }
                }
            }
            
            // Commit the transaction to persist the changes
            em.getTransaction().commit();
            }
        }
    }


    @Override
    public void deleteStudent(Student student) throws Exception {
        try(EntityManagerWrapper emw = HibernateFactory.getEntityManager()){
            
            EntityManager em = emw.get();
            em.getTransaction().begin();
            em.remove(em.contains(student)? student : em.merge(student));
            em.getTransaction().commit();
  
        }
    }
//    @Override
//    public void deleteStudent(Student student) throws Exception {
//    try (EntityManagerWrapper emw = HibernateFactory.getEntityManager()) {
//        EntityManager em = emw.get();
//        em.getTransaction().begin();
//
//        // Check if the student is in a managed state; if not, merge it to make it managed
//        Student managedStudent = em.contains(student) ? student : em.merge(student);
//
//        // Clear the associations between the student and subjects
//        managedStudent.getStudentSubjectCollection().clear();
//
//        // Delete the student entity
//        em.remove(managedStudent);
//        em.flush(); // Explicitly flush changes
//        em.getTransaction().commit();
//        }
//    }


//    @Override
//    public Student getStudent(int idStudent) throws Exception {
//        try(EntityManagerWrapper emw = HibernateFactory.getEntityManager()){
//            
//            EntityManager em = emw.get();
//            return em.find(Student.class, idStudent);
//            
//            
//        }
//    }
    @Override
    public Student getStudent(int idStudent) throws Exception {
    try (EntityManagerWrapper emw = HibernateFactory.getEntityManager()) {
        EntityManager em = emw.get();

        // Use a JOIN FETCH query to retrieve the student and their associated subjects
        String query = "SELECT s FROM Student s LEFT JOIN FETCH s.studentSubjectCollection WHERE s.idStudent = :idStudent";

        return em.createQuery(query, Student.class)
            .setParameter("idStudent", idStudent)
            .getSingleResult();
        }
    }

//    @Override
//    public List<Student> getStudents() throws Exception {
//        try(EntityManagerWrapper emw = HibernateFactory.getEntityManager()){
//            
//            EntityManager em = emw.get();
//            return em.createNamedQuery(HibernateFactory.SELECT_STUDENTS).getResultList();
//
//        }
//    }
    @Override
    public List<Student> getStudents() throws Exception {
    try (EntityManagerWrapper emw = HibernateFactory.getEntityManager()) {
        EntityManager em = emw.get();

        // Use a JOIN FETCH query to retrieve students and their associated subjects
        String query = "SELECT s FROM Student s LEFT JOIN FETCH s.studentSubjectCollection";

        return em.createQuery(query, Student.class).getResultList();
        }
    }
}
