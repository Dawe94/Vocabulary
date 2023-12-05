package com.vocabulary;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class PrimaryController implements Initializable{
    
    //<editor-fold defaultstate="collapsed" desc="Init Variables">
    private Reader reader;
    
    private final int frequencyOfRead = 3;
    
    private int count = frequencyOfRead;
    
    private List<Word> list = new ArrayList<>();
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FXML Objects">
    @FXML
    private Button checkButton;
    @FXML
    private Pane errorPane;
    @FXML
    private Pane base;
    @FXML
    private Pane showAnswer;
    @FXML
    private Pane menu;
    @FXML
    private Pane status;
    @FXML
    private Label numOfHits;
    @FXML
    private Label englishWord;
    @FXML
    private Label answer;
    @FXML
    private Label checkAnswer;
    @FXML
    private TextField hungarianEquivalent;
    @FXML
    private Label exception;
    @FXML
    private Label correctAnswer;
//</editor-fold>
       
    @FXML
    private void handleButtonAction() {
        if (hungarianEquivalent.getText() != null && hungarianEquivalent.getText().length() > 0) {
            checkAnswer();
            setQuestion(false);
        } else {
            base.setDisable(true);
            base.setOpacity(0.3);
            errorPane.setVisible(true);
            exception.setText("You have to enter the answer!");            
        }
        
    }
    
    private void handleEnterPressed(KeyEvent key) {
        if (key.getCode() == KeyCode.ENTER) {
            handleButtonAction();
        }
    }
    
    @FXML
    private void okButtonAction() {
        base.setDisable(false);
        base.setOpacity(1.0);
        errorPane.setVisible(false);
    }
    
    @FXML
    private void handleUnderstandAction() {
        base.setDisable(false);
        base.setOpacity(1.0);
        showAnswer.setVisible(false);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       // Reader inicializálása
        String path = System.getProperty("user.dir")+"\\src\\main\\resources"
                +"\\com\\vocabulary\\VocabularyCollection.xlsx";
        reader = new Reader(path, frequencyOfRead);
        System.out.println("Number Of Hits: "+reader.getSumOfHits());
        
        // Enter billentyű hozzárendelése a Check gomb-hoz
        checkButton.setFocusTraversable(true);
        checkButton.addEventHandler(KeyEvent.KEY_PRESSED, key -> this.handleEnterPressed(key));
        
        // Eseménykezelő a lezárás gombhoz
       Platform.runLater(() -> {
           Stage stage = (Stage)base.getScene().getWindow();
           stage.setOnCloseRequest(event -> handleCloseRequest(event));
           setQuestion(true);
        });
    }
    
    private void setQuestion(boolean firstTime) {
        if (count >= frequencyOfRead) {
            list = firstTime ? reader.lazyRead() : reader.lazyReadAndWrite(list);
            count = 1;
        }
        englishWord.setText(list.get(count).getWord());
    }
    
    private void checkAnswer() {
        if (list.get(count).isThereMatch(hungarianEquivalent.getText())) {
             correctAnswer.setVisible(true);
             answer.setText("");
             list.get(count).incrementHit();
        } else {
            checkAnswer.setText("Not a correct Answer!");
            correctAnswer.setVisible(false);
            base.setDisable(true);
            base.setOpacity(0.6);
            showAnswer.setVisible(true);  
            answer.setText(list.get(count).getWord() +":"+"\n"
                    +list.get(count).getMeaningsAsString());
        }
        count++;
    }
    
    private void handleCloseRequest(WindowEvent event) {
       // A meglévő lista mentése
        reader.lazyWrite(list);
      // Kiiratás és bezárás
      System.out.println("The app is closed!");
      System.exit(0);
    }
    
    @FXML
    private void handleStartGame() {
         base.setVisible(true);
         menu.setVisible(false);
    }
    
    @FXML
    private void handleStatus() {
        base.setVisible(false);
        menu.setVisible(false);
        status.setVisible(true);
        calculateStatusValues();
    }
    
    @FXML
    private void handleBackToMenu() {
         base.setVisible(false);
        menu.setVisible(true);
        status.setVisible(false);
    }
    
    private void calculateStatusValues() {
        numOfHits.setText(reader.getSumOfHits()+"");
    }
}

