import java.awt.*;
import javax.swing.*;

public class SettingsPanel extends JPanel {

    public SettingsPanel(CardLayout layout, JPanel mainPanel) {

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel title = new JLabel("Volume Settings");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JSlider volumeSlider = new JSlider(0,100,50);

        JButton backButton = new JButton("Back");

        add(title, BorderLayout.NORTH);
        add(volumeSlider, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> layout.show(mainPanel,"menu"));
    }
}