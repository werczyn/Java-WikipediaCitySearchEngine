/**
 *
 *  @author Werczyński Piotr S17093
 *
 */

package zad1;


import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Service s = new Service("Poland");
        String weatherJson = s.getWeather("Warsaw");
        Double rate1 = s.getRateFor("USD");
        Double rate2 = s.getNBPRate();
        // ...
        // część uruchamiająca GUI
        uruchomGUI();
    }

    private static void uruchomGUI(){
        JFrame frame = new JFrame("Okno Danych");
        JPanel gora = new JPanel(new GridLayout(1,7));
        JPanel dol = new JPanel(new GridLayout(5,1));

        JLabel labelKraj = new JLabel("Kraj: ");
        gora.add(labelKraj);

        JTextField textFieldKraj = new JTextField();
        gora.add(textFieldKraj);

        JLabel labelMiasto = new JLabel("Miasto: ");
        gora.add(labelMiasto);

        JTextField textFieldMiasto = new JTextField();
        gora.add(textFieldMiasto);

        JLabel labelWaluta = new JLabel("Waluta: ");
        gora.add(labelWaluta);

        JTextField textFieldWaluta = new JTextField();
        gora.add(textFieldWaluta);

        JLabel labelTemperatura = new JLabel();
        dol.add(labelTemperatura);

        JLabel labelCisnienie = new JLabel();
        dol.add(labelCisnienie);

        JLabel labelWilgotnosc = new JLabel();
        dol.add(labelWilgotnosc);

        JLabel labelRateFor = new JLabel();
        dol.add(labelRateFor);

        JLabel LabelNBP = new JLabel();
        dol.add(LabelNBP);


        JFXPanel jfxPanel = new JFXPanel();

        JButton button = new JButton("wyszukaj Informacji");
        button.addActionListener(event -> {
            if (textFieldKraj.getText().equals("") || textFieldMiasto.getText().equals("")||textFieldWaluta.getText().equals("")){
                JOptionPane.showMessageDialog(frame, "Nie wprowadzono wartosci","Brak danych",JOptionPane.WARNING_MESSAGE);
                return;
            }
            Service service = new Service(textFieldKraj.getText());

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) parser.parse(service.getWeather(textFieldMiasto.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            JSONArray list = (JSONArray) jsonObject.get("list");
            JSONObject firstObject = (JSONObject) list.get(0);
            JSONObject mainObject = (JSONObject) firstObject.get("main");

            labelTemperatura.setText("Temperatura: " + mainObject.get("temp") + "°C");
            labelCisnienie.setText("Cisnienie: "+mainObject.get("pressure")+" hPa");
            labelWilgotnosc.setText("Wilgotnosc: "+mainObject.get("humidity")+"%");
            Platform.runLater(()->{
                WebView browser = new WebView();
                WebEngine webEngine = browser.getEngine();
                webEngine.load("https://www.wikipedia.org/wiki/"+textFieldMiasto.getText());
                jfxPanel.setScene(new Scene(browser));
            });
            labelRateFor.setText("Kurs waluty: z " + service.getWaluta().getCurrencyCode()+" na " + textFieldWaluta.getText() + ": " + service.getRateFor(textFieldWaluta.getText()));
            LabelNBP.setText("kurs: z PLN na " + service.getWaluta().getCurrencyCode() + ": " +service.getNBPRate());
        });
        gora.add(button);

        frame.add(gora, BorderLayout.PAGE_START);
        frame.add(dol, BorderLayout.PAGE_END);
        frame.add(jfxPanel);

        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
