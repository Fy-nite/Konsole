package org.finite;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.Finite.MicrOS.apps.AppManifest;
import org.Finite.MicrOS.apps.MicrOSApp;
import org.Finite.MicrOS.core.CLIRegistry;
import org.Finite.MicrOS.core.VirtualFileSystem;
import org.Finite.MicrOS.ui.FontLoader;

public class DemoApp extends MicrOSApp {


    int clicked = 0;

    @Override
    public JComponent createUI() {

        JPanel Pane = new JPanel();
        JLabel label = new JLabel("Hello, world!");
        JLabel label2 = new JLabel("This is a demo app.");
        JLabel label3 = new JLabel("Click the button below to count how many times you've clicked it.") ;
        JLabel label4 = new JLabel(" if you want to turn this into your own custom app, edit Demo.app/Contents/src/org/finite/DemoApp.java");
        JButton button = new JButton("Click me!");
        Pane.add(label);
        Pane.add(label2);
        Pane.add(label3);
        Pane.add(label4);
        Pane.add(button);
        // set the size of the label
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label2.setFont(new Font("Arial", Font.PLAIN, 12));
        label3.setFont(new Font("Arial", Font.PLAIN, 12));
        label4.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        // set the color of the label
        label.setForeground(Color.BLUE);
        button.addActionListener(e -> {
            clicked++;
            button.setText("Clicked " + clicked + " times");
        });
    
        
        // Setup basic manifest
        AppManifest manifest = new AppManifest();
        manifest.setName("DemoApp");
        manifest.setIdentifier("org.finite.DemoApp");
        manifest.setMainClass(getClass().getName());
        setManifest(manifest);
        
        return Pane;
    }

    @Override
    public void onStart() {
        System.out.println("Demo started");

       
    }

    @Override
    public void onStop() {
        System.out.println("Demo stopped");
    }
}
