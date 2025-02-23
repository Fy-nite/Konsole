package org.finite;

import org.Finite.MicrOS.util.AppLauncher;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        if (AppLauncher.isRunningInMicrOS()) {
            AppLauncher.launchStandalone(DemoApp.class);
        } else {
            // Standalone mode
            JFrame frame = new JFrame("Demo App");
            DemoApp app = new DemoApp();
            frame.setContentPane(app.createUI());
            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    app.onStop();
                    System.exit(0);
                }
            });
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            app.onStart();
        }
    }
}