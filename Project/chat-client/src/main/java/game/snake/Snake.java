package game.snake;

import game.main.Main;
import game.support.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Snake extends JDialog implements KeyListener {
    
    private String tutorial = "Sử dụng phim ▲ để di chuyển lên"
                            + "\nSử dụng phím ▼ để di chuyển xuống"
                            + "\nSử dụng phím ◀ để di chuyển sang trái"
                             + "\nSử dụng phím ▶ để di chuyển sang phải";   
    private Main main;
    private int r = 28, c = 50;
    private Button[][] cells = new Button[r][c];
    
    private Random random = new Random();
    private int max = 100;
    private int a[][] = new int[max][max];
    private int sizeSnake = 3;
    private int xSnake[] = new int[max * max];
    private int ySnake[] = new int[max * max];
    private int xFood, yFood;
    private int direction = 1;
    private Color color[] = new Color[] {Color.black, Color.yellow, Color.GREEN, Color.red};
    private Timer timer;
    private boolean endGame = false;
    private int moveX[] = {-1, 0, 1, 0};
    private int moveY[] = {0, 1, 0, -1};
    
    public Snake(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        main = (Main) parent;
        init();
        JOptionPane.showMessageDialog(this, tutorial, "Tutorial", JOptionPane.INFORMATION_MESSAGE);
        
        this.setIconImage(new ImageIcon(getClass().getResource("/image_snake.png")).getImage());
    }

    private void init() {
        // Init map
        for(int i = 0 ; i < r; i++)
            for(int j = 0 ; j < c ; j++) {
                cells[i][j] = new Button(Color.black);
                cells[i][j].addKeyListener(this);
                board.add(cells[i][j]);
        }        
        
        //Init snake
        a[0][0] = 1;    xSnake[0] = 0;    ySnake[0] = 0;
        a[0][1] = 1;    xSnake[1] = 0;    ySnake[1] = 1;
        a[0][2] = 2;    xSnake[2] = 0;    ySnake[2] = 2;
        createFood();
        updateColor();
        
        // Init speed
        timer = new Timer(100, (ActionEvent e) -> {run();});
    }

    private void createFood() {
        int t = 0;
        int x[] = new int[max*max];
        int y[] = new int[max*max]; 
        for(int i = 0 ; i < r ; i++)
            for(int j = 0 ; j < c ; j++)
                if(a[i][j] == 0) {
                    x[t] = i;
                    y[t++] = j;
                }
        if(t == 0) endGame = true;
        else {
            t = random.nextInt(t);
            xFood = x[t];
            yFood = y[t];
            a[xFood][yFood] = 3;            
        }
    }
    
    private void updateColor() {
        for(int i = 0; i < r; i++)
            for(int j = 0; j < c; j++)
                cells[i][j].setBackground(color[a[i][j]]);
    }
    
    private void run() {
        a[xSnake[sizeSnake - 1]][ySnake[sizeSnake - 1]] = 1;
        xSnake[sizeSnake] = xSnake[sizeSnake - 1] + moveX[direction];
	ySnake[sizeSnake] = ySnake[sizeSnake - 1] + moveY[direction];
        
        if(xSnake[sizeSnake] < 0)    xSnake[sizeSnake] = r - 1;
        if(xSnake[sizeSnake] == r)   xSnake[sizeSnake] = 0;
        if(ySnake[sizeSnake] < 0)    ySnake[sizeSnake] = c - 1;
        if(ySnake[sizeSnake] == c)   ySnake[sizeSnake] = 0;
        
        if(a[xSnake[sizeSnake]][ySnake[sizeSnake]] == 1) {
            timer.stop();
            score.setText("Game over");
            endGame = true;
        }
        else {
            a[xSnake[0]][ySnake[0]] = 0;
            if(xFood == xSnake[sizeSnake] && yFood == ySnake[sizeSnake]) {
                a[xSnake[0]][ySnake[0]] = 1;
                sizeSnake++;
                createFood();
                score.setText("Score: " + sizeSnake);
                if(timer.getDelay() > 0) timer.setDelay(timer.getDelay()-1);
            }
            else {
                for(int i = 0 ; i < sizeSnake ; i++) {
                    xSnake[i] = xSnake[i+1];
                    ySnake[i] = ySnake[i+1];
                }
            }
            a[xSnake[sizeSnake-1]][ySnake[sizeSnake-1]] = 2;
            updateColor();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        optionPanel = new javax.swing.JPanel();
        newGame = new javax.swing.JButton();
        score = new javax.swing.JLabel();
        board = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Snake");
        setResizable(false);

        optionPanel.setBackground(new java.awt.Color(0, 0, 0));
        optionPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        newGame.addKeyListener(this);
        score.addKeyListener(this);
        optionPanel.setLayout(new javax.swing.BoxLayout(optionPanel, javax.swing.BoxLayout.Y_AXIS));

        newGame.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        newGame.setText("New game");
        newGame.setAlignmentX(JButton.CENTER_ALIGNMENT);
        newGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGameActionPerformed(evt);
            }
        });
        optionPanel.add(newGame);

        score.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        score.setForeground(java.awt.Color.red);
        score.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        score.setText("Score: 0");
        score.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        score.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        optionPanel.add(score);

        getContentPane().add(optionPanel, java.awt.BorderLayout.PAGE_START);

        board.setBackground(new java.awt.Color(0, 0, 0));
        board.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        board.setLayout(new java.awt.GridLayout(28, 50));
        getContentPane().add(board, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(1517, 814));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void newGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGameActionPerformed
        dispose();
        main.getSnakeBt().doClick();
    }//GEN-LAST:event_newGameActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel board;
    private javax.swing.JButton newGame;
    private javax.swing.JPanel optionPanel;
    private javax.swing.JLabel score;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyPressed(KeyEvent e) {
        if(endGame == false) {
            if(e.getKeyCode() == KeyEvent.VK_UP && direction != 2) 
                direction = 0;
            else if(e.getKeyCode() == KeyEvent.VK_RIGHT && direction != 3) 
                direction = 1;
            else if(e.getKeyCode() == KeyEvent.VK_DOWN && direction != 0) 
                direction = 2;
            else if(e.getKeyCode() == KeyEvent.VK_LEFT && direction != 1) 
                direction = 3;
            timer.start();
        } 
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}    
}