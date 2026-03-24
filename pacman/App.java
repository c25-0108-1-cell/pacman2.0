import java.awt.*;
import javax.swing.*;

public class App {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Pacman Game");

        frame.setSize(608, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        PacMan gamePanel = new PacMan();
        MenuPanel menuPanel = new MenuPanel(cardLayout, mainPanel);
        SettingsPanel settingsPanel = new SettingsPanel(cardLayout, mainPanel);

        mainPanel.add(menuPanel, "menu");
        mainPanel.add(gamePanel, "game");
        mainPanel.add(settingsPanel, "settings");

        frame.add(mainPanel);

        frame.setVisible(true);

        // show menu first
        cardLayout.show(mainPanel, "menu");
    }
}