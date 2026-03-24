import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener {
    // Game constants
    private final int rowCount = GameConstants.ROW_COUNT;
    private final int columnCount = GameConstants.COLUMN_COUNT;
    private final int tileSize = GameConstants.TILE_SIZE;
    private final int boardWidth = GameConstants.BOARD_WIDTH;
    private final int boardHeight = GameConstants.BOARD_HEIGHT;

    // Images
    private final Image wallImage;
    private final Image blueGhostImage;
    private final Image orangeGhostImage;
    private final Image pinkGhostImage;
    private final Image redGhostImage;
    private final Image pacmanUpImage;
    private final Image pacmanDownImage;
    private final Image pacmanLeftImage;
    private final Image pacmanRightImage;
    private final Image cherryImage;
    private final Image scaredGhostImage;
    private final Image[] ghostImages;

    // Game map
    private final String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };

    // Game entities
    private List<Wall> walls;
    private List<Food> foods;
    private List<Ghost> ghosts;
    private Player pacman;
    private Food cherry;

    // Game state
    private final Timer gameLoop;
    private final char[] directions = {'U', 'D', 'L', 'R'};
    private final Random random = new Random();
    private int score = 0;
    private int lives = 3;
    private int level = 1;
    private boolean gameOver = false;
    private boolean poweredUp = false;
    private int powerUpTimer = 0;
    private final int POWER_UP_DURATION = 200; // frames
    private final List<Point> emptyPositions = new ArrayList<>();

    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //load images
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        cherryImage = new ImageIcon(getClass().getResource("./cherry.png")).getImage();
        cherry = new Food(cherryImage, 9 * tileSize, 11 * tileSize, tileSize, tileSize);
        scaredGhostImage = new ImageIcon(getClass().getResource("./scaredGhost.png")).getImage();
        ghostImages = new Image[]{blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage};

        loadMap();  
        
        SoundManager.playSound("pacman_beginning.wav");

        for (Ghost ghost : ghosts) {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
        //how long it takes to start timer, milliseconds gone between frames
        gameLoop = new Timer(GameConstants.DELAY, this); //20fps
        gameLoop.start();
    }

    public void loadMap() {
        walls = new ArrayList<>();
        foods = new ArrayList<>();
        ghosts = new ArrayList<>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                switch (tileMapChar) {
                    case 'X' -> {
                        //block wall
                        Wall wall = new Wall(wallImage, x, y, tileSize, tileSize);
                        walls.add(wall);
                    }
                    case 'b' ->                         {
                            //blue ghost
                            Ghost ghost = new Ghost(blueGhostImage, scaredGhostImage, x, y, tileSize, tileSize);
                            ghosts.add(ghost);
                        }
                    case 'o' ->                         {
                            //orange ghost
                            Ghost ghost = new Ghost(orangeGhostImage, scaredGhostImage, x, y, tileSize, tileSize);
                            ghosts.add(ghost);
                        }
                    case 'p' ->                         {
                            //pink ghost
                            Ghost ghost = new Ghost(pinkGhostImage, scaredGhostImage, x, y, tileSize, tileSize);
                            ghosts.add(ghost);
                        }
                    case 'r' ->                         {
                            //red ghost
                            Ghost ghost = new Ghost(redGhostImage, scaredGhostImage, x, y, tileSize, tileSize);
                            ghosts.add(ghost);
                        }
                    case 'P' -> //pacman
                        pacman = new Player(pacmanRightImage, x, y, tileSize, tileSize);
                    case ' ' -> //empty space
                        emptyPositions.add(new Point(x, y));
                    default -> {
                    }
                }
            }
        }

        // Add extra ghosts based on level
        int numExtra = Math.min(level - 1, 4);
        for (int i = 0; i < numExtra; i++) {
            if (emptyPositions.isEmpty()) break;
            int idx = random.nextInt(emptyPositions.size());
            Point p = emptyPositions.remove(idx);
            Image img = ghostImages[random.nextInt(4)];
            Ghost extra = new Ghost(img, scaredGhostImage, p.x, p.y, tileSize, tileSize);
            ghosts.add(extra);
        }

        // Place cherry randomly on an empty tile and remove that tile from food target positions
        placeCherry();

        // Add food to remaining empty positions
        for (Point p : emptyPositions) {
            Food food = new Food(null, p.x + 14, p.y + 14, 4, 4);
            foods.add(food);
        }
    }

    private void placeCherry() {
        // Place cherry randomly on an empty tile and remove that tile from food target positions
        if (!emptyPositions.isEmpty()) {
            int cherryIdx = random.nextInt(emptyPositions.size());
            Point cherryPos = emptyPositions.remove(cherryIdx);
            cherry = new Food(cherryImage, cherryPos.x, cherryPos.y, tileSize, tileSize);
        } else {
            // Fallback position in case map has no remaining empty tiles
            cherry = new Food(cherryImage, 9 * tileSize, 11 * tileSize, tileSize, tileSize);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        Image pacmanImg = pacman.getImage();
        g.drawImage(pacmanImg, pacman.getX(), pacman.getY(), pacman.getWidth(), pacman.getHeight(), null);

        //draw cherry if it exists
        if (cherry != null) {
            g.drawImage(cherry.getImage(), cherry.getX(), cherry.getY(), cherry.getWidth(), cherry.getHeight(), null);
        }

        for (Ghost ghost : ghosts) {
            if (!ghost.isEaten()) {
                g.drawImage(ghost.getImage(), ghost.getX(), ghost.getY(), ghost.getWidth(), ghost.getHeight(), null);
            }
        }

        for (Wall wall : walls) {
            g.drawImage(wall.getImage(), wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight(), null);
        }

        g.setColor(Color.yellow);
        for (Food food : foods) {
            g.fillRect(food.getX(), food.getY(), food.getWidth(), food.getHeight());
        }
        //score
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
        else {
            g.drawString("Level: " + level + " x" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
    }

    public void move() {
        // Update desired direction at tile center for smooth transitions
        if (pacman.isAligned(tileSize) && pacman.getDesiredDirection() != pacman.getDirection()) {
            pacman.updateDirection(pacman.getDesiredDirection());
            switch (pacman.getDirection()) {
                case 'U' -> pacman.setImage(pacmanUpImage);
                case 'D' -> pacman.setImage(pacmanDownImage);
                case 'L' -> pacman.setImage(pacmanLeftImage);
                case 'R' -> pacman.setImage(pacmanRightImage);
            }
        }

        pacman.move();

        //check cherry collision
        if (cherry != null && collision(pacman, cherry)) {
            score += 100;
            cherry = null;
            poweredUp = true;
            powerUpTimer = POWER_UP_DURATION;
            for (Ghost g : ghosts) {
                g.setScared(true);
            }
            
            SoundManager.playSound("power.wav");
        }

        //check wall collisions
        for (Wall wall : walls) {
            if (collision(pacman, wall)) {
                pacman.setX(pacman.getX() - pacman.getVelocityX());
                pacman.setY(pacman.getY() - pacman.getVelocityY());
                break;
            }
        }

        //check ghost collisions
        for (Ghost ghost : ghosts) {
            if (collision(ghost, pacman)) {
                if (poweredUp) {
                    score += 800; // 4x multiplier (4x 200)
                    ghost.eat();
                    // Optional: play sound for eating ghost
                    // SoundManager.playSound("pacman_eatghost.wav");
                } else {
                    SoundManager.playSound("dead.wav");
                    lives -= 1;
                    if (lives == 0) {
                        gameOver = true;
                        return;
                    }
                    resetPositions();
                }
            }

            // Ensure all ghosts keep moving, do not get stuck at velocity zero
            if (ghost.getVelocityX() == 0 && ghost.getVelocityY() == 0) {
                ghost.changeDirectionRandomly();
            }

            // Add more random movement (small chance to change direction each frame)
            if (random.nextDouble() < 0.30) {
                ghost.changeDirectionRandomly();
            }

            ghost.move();
            for (Wall wall : walls) {
                if (collision(ghost, wall) || ghost.getX() <= 0 || ghost.getX() + ghost.getWidth() >= boardWidth) {
                    ghost.setX(ghost.getX() - ghost.getVelocityX());
                    ghost.setY(ghost.getY() - ghost.getVelocityY());
                    ghost.changeDirectionRandomly();
                }
            }
        }

        // Update eaten ghosts
        for (Ghost ghost : ghosts) {
            ghost.updateEaten();
        }

        //check food collision
        Food foodEaten = null;
        for (Food food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 10;
                SoundManager.playSound("pacman_chomp.wav");
            }
        }
      


        foods.remove(foodEaten);

        if (foods.isEmpty()) {
            level++;
            loadMap();
            resetPositions();
        }

        // Handle power-up timer
        if (powerUpTimer > 0) {
            powerUpTimer--;
            if (powerUpTimer == 0) {
                poweredUp = false;
                for (Ghost g : ghosts) {
                    g.setScared(false);
                }
                
                SoundManager.playSound("cherry.wav");
                placeCherry();
            }
        }
    }

    public boolean collision(Entity a, Entity b) {
        return  a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }

    public void resetPositions() {
        pacman.reset();
        pacman.stop();
        poweredUp = false;
        powerUpTimer = 0;
        for (Ghost ghost : ghosts) {
            ghost.reset();
            ghost.changeDirectionRandomly();
            ghost.setScared(false);
            ghost.resetEaten();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        char newDir = pacman.getDirection();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> newDir = 'U';
            case KeyEvent.VK_DOWN -> newDir = 'D';
            case KeyEvent.VK_LEFT -> newDir = 'L';
            case KeyEvent.VK_RIGHT -> newDir = 'R';
        }

        pacman.setDesiredDirection(newDir);

        if (pacman.isAligned(tileSize)) {
            pacman.updateDirection(newDir);
        }

        switch (newDir) {
            case 'U' -> pacman.setImage(pacmanUpImage);
            case 'D' -> pacman.setImage(pacmanDownImage);
            case 'L' -> pacman.setImage(pacmanLeftImage);
            case 'R' -> pacman.setImage(pacmanRightImage);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Game Over. Restart?",
                "Game Over",
                JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                loadMap();
                resetPositions();
                lives = 3;
                score = 0;
                gameOver = false;
                gameLoop.start();
            } else {
                System.exit(0);
            }
            SoundManager.playSound("pacman_death.wav");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pacman Game");
        PacMan game = new PacMan();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
