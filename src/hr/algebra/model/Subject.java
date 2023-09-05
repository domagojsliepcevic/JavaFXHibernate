/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.dao.sql.HibernateFactory;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Domagoj
 */
@Entity
@Table(name = "Subject")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = HibernateFactory.SELECT_SUBJECTS, query = "SELECT s FROM Subject s")
    , @NamedQuery(name = "Subject.findByIDSubject", query = "SELECT s FROM Subject s WHERE s.iDSubject = :iDSubject")
    , @NamedQuery(name = "Subject.findBySubjectName", query = "SELECT s FROM Subject s WHERE s.subjectName = :subjectName")})
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDSubject")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer iDSubject;
    @Basic(optional = false)
    @Column(name = "SubjectName")
    private String subjectName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subjectID",orphanRemoval = true)
    private Collection<StudentSubject> studentSubjectCollection;

    public Subject() {
    }

    public Subject(Integer iDSubject) {
        this.iDSubject = iDSubject;
    }

    public Subject(Integer iDSubject, String subjectName) {
        this.iDSubject = iDSubject;
        this.subjectName = subjectName;
    }

    public Integer getIDSubject() {
        return iDSubject;
    }

    public void setIDSubject(Integer iDSubject) {
        this.iDSubject = iDSubject;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @XmlTransient
    public Collection<StudentSubject> getStudentSubjectCollection() {
        return studentSubjectCollection;
    }

    public void setStudentSubjectCollection(Collection<StudentSubject> studentSubjectCollection) {
        this.studentSubjectCollection = studentSubjectCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDSubject != null ? iDSubject.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subject)) {
            return false;
        }
        Subject other = (Subject) object;
        if ((this.iDSubject == null && other.iDSubject != null) || (this.iDSubject != null && !this.iDSubject.equals(other.iDSubject))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hr.algebra.model.Subject[ iDSubject=" + iDSubject + " ]";
    }
    
    
    public Subject (Subject data)
    {
        updateData(data);
    
    }

    public void updateData(Subject data) {
        
        subjectName = data.subjectName;
        
    }
    
}
