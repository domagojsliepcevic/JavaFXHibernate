/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Domagoj
 */
@Entity
@Table(name = "StudentSubject")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentSubject.findAll", query = "SELECT s FROM StudentSubject s")
    , @NamedQuery(name = "StudentSubject.findByIDStudentSubject", query = "SELECT s FROM StudentSubject s WHERE s.iDStudentSubject = :iDStudentSubject")})
public class StudentSubject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDStudentSubject")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer iDStudentSubject;
    @JoinColumn(name = "StudentID", referencedColumnName = "IDStudent")
    @ManyToOne(optional = false)
    private Student studentID;
    @JoinColumn(name = "SubjectID", referencedColumnName = "IDSubject")
    @ManyToOne(optional = false)
    private Subject subjectID;

    public StudentSubject() {
    }

    public StudentSubject(Integer iDStudentSubject) {
        this.iDStudentSubject = iDStudentSubject;
    }

    public Integer getIDStudentSubject() {
        return iDStudentSubject;
    }

    public void setIDStudentSubject(Integer iDStudentSubject) {
        this.iDStudentSubject = iDStudentSubject;
    }

    public Student getStudentID() {
        return studentID;
    }

    public void setStudentID(Student studentID) {
        this.studentID = studentID;
    }

    public Subject getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(Subject subjectID) {
        this.subjectID = subjectID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDStudentSubject != null ? iDStudentSubject.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StudentSubject)) {
            return false;
        }
        StudentSubject other = (StudentSubject) object;
        if ((this.iDStudentSubject == null && other.iDStudentSubject != null) || (this.iDStudentSubject != null && !this.iDStudentSubject.equals(other.iDStudentSubject))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hr.algebra.model.StudentSubject[ iDStudentSubject=" + iDStudentSubject + " ]";
    }
    
}
