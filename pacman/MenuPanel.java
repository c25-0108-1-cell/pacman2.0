import java.awt.*;
import javax.swing.*;


public final class MenuPanel extends JPanel {

    Image background;

    public MenuPanel(CardLayout layout, JPanel mainPanel) {

        setLayout(null); //free positioning

        //load background image
        background = new ImageIcon(getClass().getResource("./BG.png")).getImage();

        JLabel title = new JLabel("");
        title.setFont(new Font("Arial", Font.BOLD, 50));
        title.setForeground(Color.YELLOW);
        title.setBounds(200, 80, 300, 60);

        JButton startButton = new JButton("START");
        JButton helpButton = new JButton("HELP");
        JButton settingsButton = new JButton("SETTINGS");

        styleButton(startButton);
        styleButton(helpButton);
        styleButton(settingsButton);

        startButton.setBounds(230, 250, 150, 40);
        helpButton.setBounds(230, 320, 150, 40);
        settingsButton.setBounds(230, 390, 150, 40);

        add(title);
        add(startButton);
        add(helpButton);
        add(settingsButton);

        startButton.addActionListener(e -> {
            layout.show(mainPanel,"game");

            for(Component comp : mainPanel.getComponents()){
                if(comp instanceof PacMan){
                    ((PacMan) comp).startGame();
                    comp.requestFocus();
                }
            }
        });

        settingsButton.addActionListener(e -> layout.show(mainPanel,"settings"));

        helpButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Arrow Keys = Move\nEat all food\nAvoid ghosts!")
        );
    }

    //button design
    public void styleButton(JButton button){

        button.setFocusPainted(false);
        button.setFont(new Font("Arial",Font.BOLD,16));

        button.setBackground(Color.BLACK);
        button.setForeground(Color.YELLOW);

        button.setBorder(BorderFactory.createLineBorder(Color.YELLOW,2));

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    //draw background 
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        if(background != null){
            g.drawImage(background,0,0,getWidth(),getHeight(),this);
        }
    }
}