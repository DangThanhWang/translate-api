package com.example.learn2.TranslateController;

import com.example.learn2.microphone.Microphone;
import com.example.learn2.recognizer.*;
import com.example.learn2.recognizer.GSpeechDuplex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sourceforge.javaflacencoder.FLACFileWriter;

import java.io.IOException;

public class TranslateController {
    final Microphone mic = new Microphone(FLACFileWriter.FLAC);
    GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");



    @FXML
    private TextField textOld;

    @FXML
    private TextArea textNew;

    @FXML
    private MenuButton buttonFromLanguage;

    @FXML
    private MenuButton buttonToLanguage;

    private TextToSpeech textToSpeech = new TextToSpeech();
    private Translator translator = new Translator();


    @FXML
    void getLangFrom(ActionEvent event) {
        String s = ((MenuItem) event.getSource()).getText();
        buttonFromLanguage.setText(s);
        translator.setFromLanguage(s);
    }

    @FXML
    void getLangTo(ActionEvent event) {
        String s = ((MenuItem) event.getSource()).getText();
        buttonToLanguage.setText(s);
        translator.setToLanguage(s);
    }

    @FXML
    protected void translate() throws IOException {
        textNew.setText(translator.translate(textOld.getText()));
    }

    @FXML
    void speak1(ActionEvent event) {
        textToSpeech.setLanguageCode(translator.getFromLanguage());
        textToSpeech.speak(textOld.getText());
    }

    @FXML
    void speak2(ActionEvent event) {
        textToSpeech.setLanguageCode(translator.getToLanguage());
        textToSpeech.speak(textNew.getText());
    }

    @FXML
    void record(ActionEvent event) {
        duplex.setLanguage("vi");
        new Thread(() -> {
            try {
                duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }).start();
        duplex.addResponseListener(new GSpeechResponseListener() {
            String old_text = "";

            public void onResponse(GoogleResponse gr) {
                String output = "";
                output = gr.getResponse();
                if (gr.getResponse() == null) {
                    if (this.old_text.contains("(")) {
                        this.old_text = this.old_text.substring(0, this.old_text.indexOf('('));
                    }
                    System.out.println("Paragraph Line Added");
                    this.old_text = this.old_text.replace(")", "").replace("( ", "");

                    return;
                }
                if (output.contains("(")) {
                    output = output.substring(0, output.indexOf('('));
                }
                if (!gr.getOtherPossibleResponses().isEmpty()) {
                    output = output + " (" + (String) gr.getOtherPossibleResponses().get(0) + ")";
                }
                System.out.println(output);

            }
        });
    }

    @FXML
    void stop(ActionEvent event) {
        mic.close();
        duplex.stopSpeechRecognition();
    }
}
