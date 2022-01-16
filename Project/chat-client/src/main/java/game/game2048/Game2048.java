package game.game2048;

import game.main.Main;
import game.support.Button;
import game.support.Pair;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


public class Game2048 extends javax.swing.JDialog implements KeyListener {

    private String tutorial = "Sử dụng phim ▲ để di chuyển lên"
                            + "\nSử dụng phím ▼ để di chuyển xuống"
                            + "\nSử dụng phím ◀ để di chuyển sang trái"
                             + "\nSử dụng phím ▶ để di chuyển sang phải";
    private Main main;
    private int r = 4, c = 4;
    private Button[][] bt = new Button[r][c];
    private Pair<Integer, Boolean>[][] a = new Pair[r][c]; 
    private Random rd = new Random();
    private Map<Integer, Color> map = new HashMap<>();
    
    public Game2048(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        main = (Main) parent;
        newGame.addKeyListener(this);
        score.addKeyListener(this);
        for(int i = 0 ; i < r ; i++)
            for(int j = 0 ; j < c ; j++) {
                bt[i][j] = new Button(Color.LIGHT_GRAY);
                bt[i][j].addKeyListener(this);
                board.add(bt[i][j]);
                a[i][j] = new Pair<>(0, true);
            }  
        createNumber();
        createNumber();
        update();
        JOptionPane.showMessageDialog(this, tutorial, "Tutorial", JOptionPane.INFORMATION_MESSAGE);
        
        this.setIconImage(new ImageIcon(getClass().getResource("/image_2048.png")).getImage());
    }
    
    private void createNumber() {
        List<Pair<Integer, Integer>> p = new ArrayList<>();
        for(int i = 0 ; i < bt.length ; i++)
            for(int j = 0 ; j < bt[i].length ; j++) 
                if(a[i][j].getKey() == 0)
                    p.add(new Pair<>(i, j));
        if(!p.isEmpty()) {
            Pair<Integer, Integer> p1 = p.get(rd.nextInt(p.size()));
            int value = rd.nextInt(20);
            if(value == 0) value = 4;
            else value = 2;
            a[p1.getKey()][p1.getValue()].setKey(value);            
        }
    }
    
    private void update() {
        int max = 0;
        for(int i = 0 ; i < r ; i++)
            for(int j = 0 ; j < c ; j++) {
                bt[i][j].setText("");  
                bt[i][j].setBackground(Color.white);
                a[i][j].setValue(true);
                if(a[i][j].getKey() != 0)  {
                    int key = a[i][j].getKey();
                    bt[i][j].setText("" + key);
                    max = Math.max(max, key);
                    if(!map.containsKey(key)) map.put(key, createColor());
                    bt[i][j].setBackground(map.get(key));
                    bt[i][j].setFont(new Font(null, Font.BOLD, 150-bt[i][j].getText().length()*14));
                }    
            }
        score.setText("Score: " + max);
    }
    
    private Color createColor() {
        int red = rd.nextInt(100) + 156;
        int blue = rd.nextInt(100) + 156;
        int green = rd.nextInt(100) + 156;
        return new Color(red, green, blue);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        board = new javax.swing.JPanel();
        optionPanel = new javax.swing.JPanel();
        newGame = new javax.swing.JButton();
        score = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("2048");

        board.setLayout(new java.awt.GridLayout(4, 4));
        getContentPane().add(board, java.awt.BorderLayout.CENTER);

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

        score.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        score.setForeground(java.awt.Color.red);
        score.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        score.setText("Score: 2");
        score.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        score.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        optionPanel.add(score);

        getContentPane().add(optionPanel, java.awt.BorderLayout.PAGE_START);

        setSize(new java.awt.Dimension(914, 823));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void newGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGameActionPerformed
        dispose();
        main.getG2048().doClick();
    }//GEN-LAST:event_newGameActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel board;
    private javax.swing.JButton newGame;
    private javax.swing.JPanel optionPanel;
    private javax.swing.JLabel score;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyPressed(KeyEvent e) {
        if(endGame() == false) {
            Pair<Integer, Boolean>[][] b = copy();
            if(e.getKeyCode() == KeyEvent.VK_DOWN) down();
            if(e.getKeyCode() == KeyEvent.VK_UP) up();
            if(e.getKeyCode() == KeyEvent.VK_LEFT) left();
            if(e.getKeyCode() == KeyEvent.VK_RIGHT) right();
            if(!equals(b)) createNumber();
            update();
        }
        else score.setText("Game over");
    }  
    
    private boolean endGame() {
        for(int i = 0 ; i < r ; i++)
            for(int j = 0 ; j < c ; j++) {
                if(a[i][j].getKey() == 0) return false;
                if(j + 1 < c && a[i][j].getKey().equals(a[i][j+1].getKey())) return false;
                if(j - 1 >= 0 && a[i][j].getKey().equals(a[i][j-1].getKey())) return false;
                if(i+1 < r && a[i][j].getKey().equals(a[i+1][j].getKey())) return false;
                if(i-1 >= 0 && a[i][j].getKey().equals(a[i-1][j].getKey())) return false;
            }   
        return true;
    }
    
    private Pair<Integer, Boolean>[][] copy() {
        Pair<Integer, Boolean>[][] b =  new Pair[r][c]; 
        for(int i = 0 ; i < r ; i++)
            for(int j = 0 ; j < c ; j++)
                b[i][j] = new Pair<>(a[i][j]);
        return b;
    }
    
    private boolean equals(Pair<Integer, Boolean>[][] b) {
        for(int i = 0 ; i < r ; i++)
            for(int j = 0 ; j < c ; j++)
                if(!a[i][j].getKey().equals(b[i][j].getKey()))
                    return false;
        return true;
    }
    
    private void down() {
        for(int j = 0 ; j < c ; j++) 
            for(int i = r-2 ; i >= 0 ; i--)
                if(a[i][j].getKey() != 0)
                    for(int k = i+1 ; k < r ; k++) {
                        if(a[k][j].getKey() == 0) 
                            a[k][j].copyOf(a[k-1][j]);
                        else if(a[k][j].getKey().equals(a[k-1][j].getKey()) && a[k][j].getValue() == true && a[k-1][j].getValue() == true) {
                            a[k][j].setKey(a[k][j].getKey()*2);
                            a[k][j].setValue(false);
                        } 
                        else break;
                        a[k-1][j].setKey(0);
                    }                        
    }

    private void up() {
        for(int j = 0 ; j < c ; j++) 
            for(int i = 1 ; i < r ; i++)
                if(a[i][j].getKey() != 0) 
                    for(int k = i-1 ; k >= 0 ; k--) {
                        if(a[k][j].getKey() == 0) 
                            a[k][j].copyOf(a[k+1][j]);
                        else if(a[k][j].getKey().equals(a[k+1][j].getKey()) && a[k][j].getValue() == true && a[k+1][j].getValue() == true) {
                            a[k][j].setKey(a[k][j].getKey()*2);
                            a[k][j].setValue(false);
                        } 
                        else break;
                        a[k+1][j].setKey(0);
                    }                                
    }

    private void left() {
        for(int i = 0 ; i < r ; i++)
            for(int j = 1 ; j < c ; j++)
                if(a[i][j].getKey() != 0)
                    for(int k = j-1 ; k >= 0 ; k--) {
                        if(a[i][k].getKey() == 0) 
                            a[i][k].copyOf(a[i][k+1]);
                        else if(a[i][k].getKey().equals(a[i][k+1].getKey()) && a[i][k].getValue() == true && a[i][k+1].getValue() == true) {
                            a[i][k].setKey(a[i][k].getKey()*2);
                            a[i][k].setValue(false);
                        } 
                        else break;
                        a[i][k+1].setKey(0);
                    }                                     
    }

    private void right() {
        for(int i = 0 ; i < r ; i++)
            for(int j = c-2 ; j >= 0 ; j--)
                if(a[i][j].getKey() != 0)
                    for(int k = j+1 ; k < c ; k++) {
                        if(a[i][k].getKey() == 0) 
                            a[i][k].copyOf(a[i][k-1]);
                        else if(a[i][k].getKey().equals(a[i][k-1].getKey()) && a[i][k].getValue() == true && a[i][k-1].getValue() == true) {
                            a[i][k].setKey(a[i][k].getKey()*2);
                            a[i][k].setValue(false);
                        } 
                        else break;
                        a[i][k-1].setKey(0);
                    }                                
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}