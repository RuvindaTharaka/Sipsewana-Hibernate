package lk.ijse.pos.hibernate.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.pos.hibernate.bo.BOFactory;
import lk.ijse.pos.hibernate.bo.custom.impl.ProgramBOImpl;
import lk.ijse.pos.hibernate.bo.custom.impl.StudentBOImpl;
import lk.ijse.pos.hibernate.dto.ProgramDTO;
import lk.ijse.pos.hibernate.dto.StudentDTO;
import lk.ijse.pos.hibernate.entity.Program;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddProgramController {
    @FXML
    private TableView<ProgramDTO> tblPrograms;

    @FXML
    private TableColumn<ProgramDTO, ?> colProgramId;

    @FXML
    private TableColumn<ProgramDTO, ?> colName;

    @FXML
    private TableColumn<ProgramDTO, ?> colDuration;

    @FXML
    private TableColumn<ProgramDTO, ?> colFee;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtDuration;

    @FXML
    private JFXTextField txtFee;

    @FXML
    private Button btnGoBack;

    @FXML
    private Button btnAdd;

    @FXML
    private Label lblProgramId;

    @FXML
    private TextField txtId;

    private final ObservableList<ProgramDTO> prList = FXCollections.observableArrayList();
    ProgramBOImpl programBOImpl= (ProgramBOImpl) BOFactory.getInstance().getBO(BOFactory.BOType.PROGRAM);

    public void initialize(){
        setTableData();
        createId();
    }

    private void createId() {
        String idNo = programBOImpl.generateId();
        lblProgramId.setText(getNewId(idNo));
    }

    private String getNewId(String id){
        int tempId = Integer.parseInt(id.split("-")[1]);
        tempId=tempId+1;
        if (tempId<=9){
            return "ITS-00"+tempId;
        }else if(tempId<99){
            return "ITS-0"+tempId;
        }else if(tempId<999){
            return "ITS-"+tempId;
        }
        return null;
    }

    @FXML
    void addProgramOnAction(ActionEvent event) throws IOException {
        ProgramDTO program = new ProgramDTO(lblProgramId.getText(),txtName.getText(),txtDuration.getText(),Integer.parseInt(txtFee.getText()));

        boolean b = programBOImpl.addProgram(program);
        if(b){
            setTableData();
            tblPrograms.refresh();
            loadNext("MenuForm");
            new Alert(Alert.AlertType.CONFIRMATION,"Program Added Done").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Program Not Added!").show();
        }
    }

    @FXML
    void goBackOnAction(ActionEvent event) throws IOException {
        loadNext("MenuForm");
    }

    private void loadNext(String name) throws IOException {
        Stage stage = (Stage) btnGoBack.getScene().getWindow();
        stage.close();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/"+name+".fxml")));
        primaryStage.setTitle("Register Window");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void setTableData(){
        colProgramId.setCellValueFactory(new PropertyValueFactory<>("programId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("program"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));

        prList.clear();
        tblPrograms.refresh();

        List<ProgramDTO> all = programBOImpl.getAllPrograms();
        for(ProgramDTO pr : all) {
            ProgramDTO tm = new ProgramDTO(
                    pr.getProgramId(),
                    pr.getProgram(),
                    pr.getDuration(),
                    pr.getFee()
            );
            prList.add(tm);
        }
        tblPrograms.setItems(prList);
    }
}
