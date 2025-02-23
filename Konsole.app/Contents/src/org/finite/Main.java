package org.finite;

import org.Finite.MicrOS.util.AppLauncher;
import org.finite.Konsole.Konsole;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        if (AppLauncher.isRunningInMicrOS()) {
            AppLauncher.launchStandalone(Konsole.class);
        } else {
            // Standalone mode
            JFrame frame = new JFrame("Demo App");
            Konsole app = new Konsole();
            frame.setContentPane(app.createUI());
            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    app.onStop();
                    System.exit(0);
                }
            });
            frame.setSize(1050, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            app.onStart();
        }
    }
}