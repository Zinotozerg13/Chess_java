package main;
import javax.swing.JFrame;


public class Main {
    public static void main(String[] args) {
JFrame window= new JFrame("Chess");
window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
window.setResizable(false);

game_panel gp=new game_panel();
window.add(gp);
window.pack();
window.setVisible(true);
window.setLocationRelativeTo(null);
gp.LaunchGame();
    }
}