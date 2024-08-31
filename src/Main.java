import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        int boardHeight = 512;
        int boardWidth = 1024;

        JFrame frame = new JFrame("Cat Jumper");
        frame.setSize(boardWidth, boardHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Game cityJumper = new Game();
        cityJumper.setFocusable(true);
        frame.add(cityJumper);
        frame.pack(); // with this, set dimensions are only the screen space (not including title bar)
        cityJumper.requestFocus();
        frame.setVisible(true);
    }


}