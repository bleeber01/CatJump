import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener, KeyListener{
    // Board Dimensions
    int boardHeight = 512;
    int boardWidth = 1024;

    //Character model
    Character cat;

    //Obstacle list
    ObstacleList obs;
    Obstacle obstacle;

    //Game Logic
    int gravity = 1;
    int scrollSpeed = 8; // equivalent to character speed
    int scrollPosition = 0;
    int difficulty = 0;
    boolean gameOver = false;
    int floor = 450;
    int score = 0;
    boolean leftFoot = false;

    List<Integer> scoreList = new ArrayList<>(); // reverse order of score (last digit in first index)

    Game() {
        setPreferredSize(new Dimension(boardWidth, boardHeight)); // dimension encapsulates w and h in a single obj
        setFocusable(true);
        addKeyListener(this);

        //load images
        loadImages();

        // starts game timers and playable character
        startGame();

    }

    public void startGame() {
        //instantiate playable character with x and y position relative to the board, a width, height, and velocity
        cat = new Character(50, 400, 60, 50, scrollSpeed);

        //game loop
        Timer gameLoop = new javax.swing.Timer(1000/60, this); // delay = 60fps
        gameLoop.start();

        obs = new ObstacleList();

        //starts obstacle timer
        genObstacle();

        //starts character walking model timer
        walk();
    }


    public void paintComponent(Graphics g) {
        //System.out.println("draw"); // game loop debugger
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //background
        backgroundInFrame();
        g.drawImage(backgroundImg, this.scrollPosition += -cat.getVelocityX(), 0,null);
        if (leftFoot) {
            g.drawImage(characterImg, cat.getCharacterX(), cat.getCharacterY(), cat.getWidth(), cat.getHeight(), null);
        } else {
            g.drawImage(characterWalkImg, cat.getCharacterX(), cat.getCharacterY(), cat.getWidth(), cat.getHeight(), null);
        }


        // update the obstacles in list and checks for collisions between character and each obstacle
        for (int i = 0; i < obs.getSize(); i++) {
            Obstacle temp = obs.getIndex(i); // returns the object held at that index
            temp.setPosition(temp.getPosition()-scrollSpeed);
            g.drawImage(obstacleImg, temp.getPosition(),floor, 60, 75, null);
            checkCollision(cat, temp);
        }

        //score
        drawScore(g);

        //g.drawImage(findNumberImg(score), 472, 10, 50, 50, null);

        // GameOver Screen
        if (gameOver) {
            g.drawImage(gameOverImg, 312, 35, 400, 400, null);
        }

    }

    // checks that the background is in frame, if false, re-frames the background by resetting the
    // background placement in JFrame
    public void backgroundInFrame() {
        if (scrollPosition <= boardWidth - 5120) {
            this.scrollPosition = 0;
            //System.out.println("reframe");
        }
    }

    public void drawScore(Graphics g) {
        if (score < 10) {
            g.drawImage(findNumberImg(score), 472, 10, 50, 50, null);
        } else if (score < 100) {
            System.out.println(scoreList.get(1));
            System.out.println(scoreList.get(2));
            g.drawImage(findNumberImg(scoreList.get(1)), 444, 10, 50, 50, null);
            g.drawImage(findNumberImg(scoreList.get(0)), 500, 10, 50, 50, null);
        }
    }


    // tries to generate an obstacle pseudo-randomly (probability based) out of frame
    // every delay interval, checking that there are no other obstacles within a min distance
    public void genObstacle(){
        // obstacle timer
        Timer obstacleInterval = new javax.swing.Timer(100, e -> {
            // this is the action listener using lambda:

            int probability = Math.max(25-difficulty-10, 2); // 1 in 25 chance; increases with difficulty to a cap of 1/2
            Random r = new Random();
            boolean n = r.nextInt(probability) == 0;
            if (n) {
                int outOfFrame = 1100; // out of frame distance
                int minDistance = 500; // min distance between obstacles
                if (!obs.obstacles.isEmpty()) {
                    if (outOfFrame - obs.getLast().getPosition() > minDistance) { // ensures min distance between obstacles
                        obstacle = new Obstacle(outOfFrame); // places obstacle just out of frame
                        placeObstacle(obstacle);
                    }
                } else { // no obstacles exist yet
                    obstacle = new Obstacle(outOfFrame);
                    placeObstacle(obstacle);
                }
            }
        });
        obstacleInterval.start();
    }

    // adds an obstacle to the ObstacleList to be placed in the next frame update
    public void placeObstacle(Obstacle o) {
        obs.add(o);
    }

    public void walk(){
        Timer walkTimer = new javax.swing.Timer(3200/scrollSpeed, new ActionListener() {

            // if the character is not in the air (on the ground): switch feet and walk.
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cat.getCharacterY() == 462) {
                    leftFoot = !leftFoot;
                }
            }
        });
        walkTimer.start();

    }


    // checks if a collision has occurred between character c and obstacle o, sets gameOver to true if collision has occurred;
    // additionally checks if the character passed the obstacle successfully
   public void checkCollision(Character c, Obstacle o) {
        // accuracy adjusters
       int w1 = 17;
       int h1 = 19;
        // Character hit-box
        int cHitBoxW1 = c.getCharacterX(); // x value of left edge of c's hit-box
        int cHitBoxW2 = cHitBoxW1 + c.getWidth(); // x value of right edge of c's hit-box
        int cHitBoxH1 = c.getCharacterY() - h1; // y value of bottom edge of c's hit-box
        int cHitBoxH2 = c.getCharacterY() - c.getHeight(); // y value of top edge of c's hit-box

       // Obstacle hit-box
       int oHitBoxW1 = o.getPosition() + w1; // x value of left edge of o's hit-box
       int oHitBoxW2 = o.getPosition() + o.getWidth() - w1; // x value of right edge of o's hit-box
       int oHitBoxH1 = floor; // y value of bottom edge of o's hit-box
       int oHitBoxH2 = oHitBoxH1 - o.getHeight() + h1; // y value of top edge of o's hit-box

       // collision logic
        if (cHitBoxH1 >= oHitBoxH2 &&                       // c is not higher than top of o
                c.getWidth() >= oHitBoxW1 - cHitBoxW1 &&    // c has at least reached o for x
                cHitBoxW1 <= oHitBoxW2){                    // c has not passed o
            gameOver = true;
        } else if (cHitBoxW1 >= oHitBoxW2 && !o.isPassed()) { // increment score if character passes a new object
            this.score++;
            scoreToList();
            o.passed();
        }
    }

    // ends the game and triggers score report
    public void endGame() {
        //System.out.println(score);
    }

    public void scoreToList() {
        int n = score;
        if (n != 0) {
            while (n > 0) {
                int temp = n % 10; // extracts last digit
                scoreList.add(temp);
                //System.out.println(scoreList.getLast());
                n = n / 10; // removes last digit by shifting
            }
        }
    }

    public Image findNumberImg(int i) {
        if (i == 1) {
            return oneImg;
        } else if (i == 2) {
            return twoImg;
        } else if (i == 3) {
            return threeImg;
        } else if (i == 4) {
            return fourImg;
        } else if (i == 5) {
            return fiveImg;
        } else if (i == 6) {
            return sixImg;
        } else if (i == 7) {
            return sevenImg;
        } else if (i == 8) {
            return eightImg;
        } else if (i == 0) {
            return zeroImg;
        } else {
            return nineImg;
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {

        if (!gameOver) {
            cat.move(gravity);
            repaint();
        } else {
            endGame();
        }

    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() > 0) {
            cat.jump();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyReleased(KeyEvent e) {

    }

    //Images
    Image backgroundImg;
    Image characterImg;

    Image characterWalkImg;
    Image obstacleImg;
    Image gameOverImg;
    Image zeroImg;
    Image oneImg;
    Image twoImg;
    Image threeImg;
    Image fourImg;
    Image fiveImg;
    Image sixImg;
    Image sevenImg;
    Image eightImg;
    Image nineImg;

    public void loadImages() {
        backgroundImg = new ImageIcon(getClass().getResource("assets/city_background_resized.png")).getImage();
        characterImg = new ImageIcon(getClass().getResource("assets/cat.png")).getImage();
        characterWalkImg = new ImageIcon(getClass().getResource("assets/cat_walk.png")).getImage();
        obstacleImg = new ImageIcon(getClass().getResource("assets/trash.png")).getImage();
        gameOverImg = new ImageIcon(getClass().getResource("assets/gameover.png")).getImage();
        zeroImg = new ImageIcon(getClass().getResource("assets/0.png")).getImage();
        oneImg = new ImageIcon(getClass().getResource("assets/1.png")).getImage();
        twoImg = new ImageIcon(getClass().getResource("assets/2.png")).getImage();
        threeImg = new ImageIcon(getClass().getResource("assets/3.png")).getImage();
        fourImg = new ImageIcon(getClass().getResource("assets/4.png")).getImage();
        fiveImg = new ImageIcon(getClass().getResource("assets/5.png")).getImage();
        sixImg = new ImageIcon(getClass().getResource("assets/6.png")).getImage();
        sevenImg = new ImageIcon(getClass().getResource("assets/7.png")).getImage();
        eightImg = new ImageIcon(getClass().getResource("assets/8.png")).getImage();
        nineImg = new ImageIcon(getClass().getResource("assets/9.png")).getImage();
    }

}


