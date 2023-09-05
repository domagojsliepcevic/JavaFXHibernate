/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.model.Student;
import hr.algebra.model.StudentSubject;
import hr.algebra.model.Subject;
import hr.algebra.utilities.FileUtils;
import hr.algebra.utilities.ImageUtils;
import hr.algebra.utilities.ValidationUtils;
import hr.algebra.viewmodel.StudentViewModel;
import hr.algebra.viewmodel.SubjectViewModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author Domagoj
 */
public class StudentController implements Initializable {

    private Map<TextField,Label> validationMap;
    
    private final ObservableList<StudentViewModel> students = FXCollections.observableArrayList();
    private StudentViewModel selectedStudentViewModel;
    
    private final ObservableList<SubjectViewModel> subjects = FXCollections.observableArrayList();
    private SubjectViewModel selectedSubjectViewModel;
    
    private final ObservableList<StudentViewModel> filteredStudents = FXCollections.observableArrayList();

    
    private ObservableList<String> subjectNamesObservableList = FXCollections.observableArrayList();
    
    private Tab previousTab;
    
    
    @FXML
    private TabPane tpContent;
    @FXML
    private Tab tabListStudents;
    @FXML
    private TableView<StudentViewModel> tvStudents;
    @FXML
    private TableColumn<StudentViewModel, String> tcFirstName;
    @FXML
    private TableColumn<StudentViewModel, String> tcLastName;
    @FXML
    private TableColumn<StudentViewModel, Integer> tcAge;
    @FXML
    private TableColumn<StudentViewModel, String> tcEmail;
    @FXML
    private ComboBox<SubjectViewModel> cbFilterBySubject;
    @FXML
    private Tab tabEditStudent;
    @FXML
    private ImageView ivStudent;
    @FXML
    private TextField tfFirstName;
    @FXML
    private Label lbFirstNameError;
    @FXML
    private TextField tfLastName;
    @FXML
    private Label lbLastNameError;
    @FXML
    private TextField tfAge;
    @FXML
    private Label lbAgeError;
    @FXML
    private TextField tfEmail;
    @FXML
    private Label lbEmailError;
    @FXML
    private Label lbIconError;
    @FXML
    private ListView<String> lvSubjects;
    @FXML
    private Label lbSubjectsListError;
    @FXML
    private Tab tabListSubjects;
    @FXML
    private TableView<SubjectViewModel> tvSubjects;
    @FXML
    private TableColumn<SubjectViewModel, String> tcSubjectName;
    @FXML
    private Tab tabEditSubject;
    @FXML
    private TextField tfSubjectName;
    @FXML
    private Label lbSubjectNameError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initStudentValidation();
        initStudents();
        initSubjects();
        initStudentTable();
        initSubjectTable();
        addIntegerMask(tfAge);
        setupListeners();
        populateListViewWithAllSubjects();
    }    

    @FXML
    private void editStudent(ActionEvent event) {
        
        if (tvStudents.getSelectionModel().getSelectedItem() != null) {
            bindStudent(tvStudents.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabEditStudent);
            previousTab = tabEditStudent;
        }
    }

    @FXML
    private void deleteStudent(ActionEvent event) {
        
        if (tvStudents.getSelectionModel().getSelectedItem() != null) {
            students.remove(tvStudents.getSelectionModel().getSelectedItem());
            
        }
    }

    @FXML
    private void filterBySubject(ActionEvent event) {
     
     // Remove the listener from the observable
     students.removeListener(this::studentsChangeListener);
     tvStudents.setItems(filteredStudents);
     
    // Get the selected subject from the ComboBox
    SubjectViewModel selectedSubjectViewModel = cbFilterBySubject.getValue();
    
    if (selectedSubjectViewModel != null) {
        // Retrieve the underlying Subject object from the ViewModel
        Subject selectedSubject = selectedSubjectViewModel.getSubject();

        if (selectedSubject != null && !"Show All".equals(selectedSubject.getSubjectName())) {
            // Filter the students based on the selected subject
            List<StudentViewModel> filteredStudentsList = students
                    .stream()
                    .filter(studentViewModel -> studentViewModel.getStudent().getStudentSubjectCollection()
                            .stream()
                            .anyMatch(studentSubject -> studentSubject.getSubjectID().equals(selectedSubject)))
                    .collect(Collectors.toList());

            // Update the TableView with the filtered students
            filteredStudents.setAll(filteredStudentsList);
            System.out.println("Filtered Students:");
                for (StudentViewModel studentViewModel : filteredStudents) {
                    System.out.println(studentViewModel.getStudent().getFirstName());
                }
            } else {
                // If the default option is selected or the subject is null, show all students
                filteredStudents.setAll(students);
            }
        } else {
            // If no subject is selected, show all students
            filteredStudents.setAll(students);
        }
        students.addListener(this::studentsChangeListener);
    }

    @FXML
    private void upload(ActionEvent event) {
        
        File file = FileUtils.uploadFileDialog(tfAge.getScene().getWindow(), "jpg","jpeg","png");
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            ivStudent.setImage(image);
            
            String ext = file.getName().substring(file.getName().lastIndexOf(".")+1);
            try {
                selectedStudentViewModel.getStudent().setPicture(ImageUtils.imageToByteArray(image, ext));
            } catch (IOException ex) {
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void commitStudent(ActionEvent event) {
    if (studentFormIsValid()) {
        // Extract the Student object from the StudentViewModel
        Student updatedStudent = selectedStudentViewModel.getStudent();
        
        updatedStudent.setFirstName(tfFirstName.getText().trim());
        updatedStudent.setLastName(tfLastName.getText().trim());
        updatedStudent.setEmail(tfEmail.getText().trim());
        updatedStudent.setAge(Integer.valueOf(tfAge.getText().trim()));
        
        // Get the selected subjects from the ListView
        ObservableList<String> selectedSubjects = (ObservableList<String>) lvSubjects.getSelectionModel().getSelectedItems();
        
        // Convert the ObservableList to a standard List
        List<String> selectedSubjectsList = new ArrayList<>(selectedSubjects);
        
        if (updatedStudent.getIDStudent() == 0) {
            try {
                // Pass the extracted Student object and selectedSubjectsList to the addStudent method
                int studentId = RepositoryFactory.getStudentRepository().addStudent(updatedStudent, selectedSubjectsList);
                updatedStudent.setIDStudent(studentId); // Update the Student object with the new ID
                students.add(selectedStudentViewModel);
                // Populate the ListView with subjects (new student, no preselected subjects)
                populateListViewWithAllSubjects();
            } catch (Exception ex) {
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            }else {
                try {
                // Pass the extracted Student object and selectedSubjectsList to the updateStudent method
                    RepositoryFactory.getStudentRepository().updateStudent(updatedStudent, selectedSubjectsList);
                    tvStudents.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           
        tpContent.getSelectionModel().select(tabListStudents);
        resetStudentForm();
        selectedStudentViewModel = null;
            
        }
    }


    @FXML
    private void deleteSubject(ActionEvent event) {
        
        if (tvSubjects.getSelectionModel().getSelectedItem() != null) {
            subjects.remove(tvSubjects.getSelectionModel().getSelectedItem());
            updateComboBoxWithSubjects();
        }
    }

    @FXML
    private void editSubject(ActionEvent event) {
        
        if (tvSubjects.getSelectionModel().getSelectedItem() != null) {
            bindSubject(tvSubjects.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabEditSubject);
            previousTab = tabEditSubject;
            updateComboBoxWithSubjects();
        }
    }

    @FXML
    private void commitSubject(ActionEvent event) {
        
        if (subjectFormIsValid()) {
            selectedSubjectViewModel.getSubject().setSubjectName(tfSubjectName.getText().trim());
            if (selectedSubjectViewModel.getIDSubjectProperty().get() == 0) {
                subjects.add(selectedSubjectViewModel);
            }else{
            
                try {
                    RepositoryFactory.getSubjectRepository().updateSubject(selectedSubjectViewModel.getSubject());
                    tvSubjects.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            tpContent.getSelectionModel().select(tabListSubjects);
            resetSubjectForm();
            selectedSubjectViewModel = null;
            updateComboBoxWithSubjects();
        }
    }

    private void initStudentValidation() {
        validationMap = Stream.of(
        new AbstractMap.SimpleImmutableEntry<>(tfFirstName,lbFirstNameError),
        new AbstractMap.SimpleImmutableEntry<>(tfLastName,lbLastNameError),
        new AbstractMap.SimpleImmutableEntry<>(tfAge,lbAgeError),
        new AbstractMap.SimpleImmutableEntry<>(tfEmail,lbEmailError)
        ).collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }

    private void initStudents() {
        try {
            RepositoryFactory.getStudentRepository().getStudents().forEach(
                    p-> students.add(new StudentViewModel(p)));
        } catch (Exception ex) {
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initSubjects() {
        try {
            RepositoryFactory.getSubjectRepository().getSubjects().forEach(
                    p-> subjects.add(new SubjectViewModel(p)));
        } catch (Exception ex) {
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initStudentTable() {
        tcFirstName.setCellValueFactory(p-> p.getValue().getFirstNameProperty());
        tcLastName.setCellValueFactory(p-> p.getValue().getLastNameProperty());
        tcAge.setCellValueFactory(p-> p.getValue().getAgeProperty().asObject());
        tcEmail.setCellValueFactory(p-> p.getValue().getEmailProperty());
        tvStudents.setItems(students);
        
        SubjectViewModel defaultOption = new SubjectViewModel(new Subject());
        defaultOption.getSubject().setSubjectName("Show All"); // Set the default option's label
        // Set the ComboBox's cell factory to render "Show All" differently
        cbFilterBySubject.setCellFactory(param -> new ListCell<SubjectViewModel>() {
                @Override
                protected void updateItem(SubjectViewModel item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item.getSubject() == null) {
                        setText("Show All");
                    } else {
                        setText(item.getSubject().getSubjectName());
                    }
                }
          });
        // Set the ComboBox's button cell to display "Show All" initially
        cbFilterBySubject.setButtonCell(new ListCell<SubjectViewModel>() {
                @Override
                protected void updateItem(SubjectViewModel item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item.getSubject() == null) {
                        setText("Show All");
                    } else {
                        setText(item.getSubject().getSubjectName());
                    }
                }
            });
        // Set the default option as the initially selected item
        cbFilterBySubject.getSelectionModel().selectFirst();
        updateComboBoxWithSubjects();   
    }

    private void initSubjectTable() {
        
        tcSubjectName.setCellValueFactory(p-> p.getValue().getSubjectNameProperty());
        tvSubjects.setItems(subjects);
    }

    private void addIntegerMask(TextField tfAge) {
        
        UnaryOperator<TextFormatter.Change> filter = change ->
        { 
            if (change.getText().matches("\\d*")) {
                
                return change;
            }
            return null;
        };
        
        tfAge.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(),0,filter));
    }

    private void setupListeners() {
        
         tpContent.setOnMouseClicked(event -> {
             
             if (tpContent.getSelectionModel().getSelectedItem().equals(tabEditStudent) && !tabEditStudent.equals(previousTab)) {
                 bindStudent(null);
             }
             if (tpContent.getSelectionModel().getSelectedItem().equals(tabEditSubject) && !tabEditSubject.equals(previousTab)) {
                 
                 bindSubject(null);
             }
             
             
             previousTab = tpContent.getSelectionModel().getSelectedItem();
         });
         
         students.addListener(this::studentsChangeListener);
         subjects.addListener(this::subjectsChangeListener);
         
    }

    private void bindStudent(StudentViewModel studentViewModel) {
        resetStudentForm();
        
        selectedStudentViewModel = studentViewModel != null ? studentViewModel : new StudentViewModel(null);
        
        tfFirstName.setText(selectedStudentViewModel.getFirstNameProperty().get());
        tfLastName.setText(selectedStudentViewModel.getLastNameProperty().get());
        tfEmail.setText(selectedStudentViewModel.getEmailProperty().get());
        tfAge.setText(selectedStudentViewModel.getAgeProperty().get()+"");
        
        ivStudent.setImage(
                selectedStudentViewModel.getPictureProperty().get() != null ? new Image(new ByteArrayInputStream(selectedStudentViewModel.getPictureProperty().get()))
                        : new Image(new File("src/assets/no_image.png").toURI().toString())
        );
        
        if (studentViewModel != null) {
        // Retrieve the subject collection from the ViewModel
        Collection<StudentSubject> studentSubjects = studentViewModel.getSubjectCollectionProperty().get();
        
        if (studentSubjects != null && !studentSubjects.isEmpty()) {
            // Extract subject IDs from StudentSubject entities
            List<Integer> subjectIds = studentSubjects.stream()
                .map(studentSubject -> studentSubject.getSubjectID().getIDSubject())
                .collect(Collectors.toList());

            // Query your data source to get the subject names based on the IDs
            List<String> subjectNames = new ArrayList<>();
            try {
                subjectNames = RepositoryFactory.getSubjectRepository().getSubjectNamesByIds(subjectIds);
            } catch (Exception ex) {
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Add the subjects to the ListView
            ((ListView<String>) lvSubjects).getItems().addAll(subjectNames);
            }
        }
    }
    
    

    private void bindSubject(SubjectViewModel subjectViewModel) {
        resetSubjectForm();
        
        
        if (subjectViewModel != null) {
        // If subjectViewModel is not null, use it
        selectedSubjectViewModel = subjectViewModel;
        } else {
        // If subjectViewModel is null, create a new instance with a default value
        selectedSubjectViewModel = new SubjectViewModel("Default Subject Name");
        }
        tfSubjectName.setText(selectedSubjectViewModel.getSubjectNameProperty().get());
        
    }

    private void resetStudentForm() {
        validationMap.values().forEach(lb -> lb.setVisible(false));
        lbIconError.setVisible(false);
        lbSubjectsListError.setVisible(false);
    }

    private void resetSubjectForm() {
        lbSubjectNameError.setVisible(false);
    }

    private boolean studentFormIsValid() {
        resetStudentForm();
        final AtomicBoolean ok = new AtomicBoolean(true);
        validationMap.forEach((tf, lb) -> {
            if (tf.getText().isEmpty() || tf.getId().contains("Email") && !ValidationUtils.isValidEmail(tf.getText())) {
                lb.setVisible(true);
                ok.set(false);
            }
        });
        
        if (selectedStudentViewModel.getPictureProperty() == null) {
            lbIconError.setVisible(true );
            ok.set(false);
            
        }
        
        if (selectedStudentViewModel.getSubjectCollectionProperty() == null) {
            
            lbSubjectsListError.setVisible(true);
            ok.set(false);
        }
                
        return ok.get();
    }

    private boolean subjectFormIsValid() {
        resetSubjectForm();
        boolean ok = true;
        if (tfSubjectName.getText().isEmpty()) {
            ok = false;
        }
                
        return ok;
    }
    
    private void updateComboBoxWithSubjects() {
        
        List<Subject> allSubjects = Collections.emptyList();
        try {
            // Retrieve the list of subjects (you should replace this with your actual data source)
           allSubjects = RepositoryFactory.getSubjectRepository().getSubjects();
            
        } catch (Exception ex) {
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        // Convert the list of subjects to SubjectViewModel objects
        List<SubjectViewModel> subjectViewModels = allSubjects.stream()
        .map(SubjectViewModel::new)
        .collect(Collectors.toList());
        
        // Add the "Show All" option to the list
        subjectViewModels.add(0, new SubjectViewModel("Show All"));
        
        // Set the converted list of SubjectViewModel objects as items in the ComboBox
         cbFilterBySubject.getItems().setAll(subjectViewModels);
    }
    
    // Method to populate the ListView lvSubjects with all subjects
    private void populateListViewWithAllSubjects() {
        
        
        List<Subject> allSubjects;
            try {
                allSubjects = RepositoryFactory.getSubjectRepository().getSubjects();
            } catch (Exception ex) {
                    Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                    allSubjects = new ArrayList<>(); // Handle the exception gracefully
        }

        // Convert the list of subjects to a list of subject names
        List<String> subjectNames = allSubjects.stream()
            .map(Subject::getSubjectName)
            .collect(Collectors.toList());

        // Update the ObservableList with the new subject names
        subjectNamesObservableList.setAll(subjectNames);
        
        // Set the ObservableList as items in the ListView
        lvSubjects.setItems(subjectNamesObservableList);
    }
    
        private void studentsChangeListener(ListChangeListener.Change<? extends StudentViewModel> change) {
        if (change.next()) {
            if (change.wasRemoved()) {
                change.getRemoved().forEach(stvm -> {
                    try {
                        if (!"Show All".equals(cbFilterBySubject.getValue().getSubjectNameProperty())) {
                            RepositoryFactory.getStudentRepository().deleteStudent(stvm.getStudent());
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            } else if (change.wasAdded()) {
                change.getAddedSubList().forEach(stvm -> {
                    try {
                        int idStudent = RepositoryFactory.getStudentRepository().addStudent(stvm.getStudent(), null);
                        stvm.getStudent().setIDStudent(idStudent);
                    } catch (Exception ex) {
                        Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        }
    }

    private void subjectsChangeListener(ListChangeListener.Change<? extends SubjectViewModel> change) {
        if (change.next()) {
            if (change.wasRemoved()) {
                change.getRemoved().forEach(sbvm -> {
                    try {
                        RepositoryFactory.getSubjectRepository().deleteSubject(sbvm.getSubject());
                    } catch (Exception ex) {
                        Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

            } else if (change.wasAdded()) {
                change.getAddedSubList().forEach(sbvm -> {
                    try {
                        int idSubject = RepositoryFactory.getSubjectRepository().addSubject(sbvm.getSubject());
                        sbvm.getSubject().setIDSubject(idSubject);
                    } catch (Exception ex) {
                        Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        }
    }
}
