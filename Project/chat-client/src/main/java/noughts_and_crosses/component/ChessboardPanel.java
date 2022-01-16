package noughts_and_crosses.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import noughts_and_crosses.support.Button;
import noughts_and_crosses.support.Exchangers;

public class ChessboardPanel extends JPanel implements ActionListener {

    private int id;
    private final Socket socket;
    private Button[][] cells;
    private boolean openTurn;
    private int iyellow, jyellow; // Vị trí ô màu vàng - ô vừa mới được đánh 
    
    public ChessboardPanel(Socket socket) {  
        this.socket = socket;
        initComponents();
        initCells();
    }

    private void initCells() {
        cells = new Button[16][32];
        for(int i = 0 ; i < cells.length ; i++)
            for(int j = 0 ; j < cells[i].length ; j++) {
                cells[i][j] = new Button(i, j, Color.lightGray); 
                cells[i][j].setFont(new Font(null, Font.BOLD, 22));
                cells[i][j].addActionListener(this);
                center.add(cells[i][j]);
        }        
    }
    
    public void click(int i, int j) {
        if(cells[i][j].getText().isEmpty()) {
            if(turn.getText().equals("Turn: X")) 
                play(i, j, "X", Color.red);
            else 
                play(i, j, "O", Color.blue);             
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header = new javax.swing.JPanel();
        turn = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        center = new javax.swing.JPanel();

        setBackground(new java.awt.Color(0, 0, 0));
        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "Chessboard", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Dialog", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        setLayout(new java.awt.BorderLayout());

        header.setBackground(new java.awt.Color(0, 0, 0));
        header.setLayout(new java.awt.BorderLayout());

        turn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        turn.setForeground(java.awt.Color.red);
        turn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        turn.setText("Turn: X");
        turn.setToolTipText("");
        turn.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        header.add(turn, java.awt.BorderLayout.CENTER);

        add(header, java.awt.BorderLayout.PAGE_START);

        center.setBackground(new java.awt.Color(0, 0, 0));
        center.setLayout(new java.awt.GridLayout(16, 32));
        jScrollPane1.setViewportView(center);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void actionPerformed(ActionEvent e) {
        Button cell = (Button) e.getSource();
        if((id == 0 || id == 1) && openTurn == true && cell.getText().isEmpty()) {
            send(new int[] {cell.getI(), cell.getJ()});
            System.out.println("id hien tai = " + id);
            if(id == 0) send(new int[] {1});
            else if(id == 1) send(new int[] {0});
        }          
    }
    
    private void send(Object object) {
        try {
            Exchangers.send(socket, object);
        } catch (IOException ex) {
            Logger.getLogger(ChessboardPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void play(int i, int j, String text, Color color) {
        cells[i][j].setForeground(color);
        cells[i][j].setText(text);
        
        // Bỏ màu ô màu vàng cũ
        cells[iyellow][jyellow].setBackground(Color.WHITE); 
        // Set màu vàng cho ô vừa đánh 
        cells[i][j].setBackground(Color.YELLOW);
        iyellow = i;
        jyellow = j;
        
        if(checkWin(i, j)) {
            turn.setText(text + " win");
            openTurn = false;
        }
        else if(checkDraw()) {
            turn.setText("Draw");
            turn.setForeground(Color.YELLOW);
            openTurn = false;
        }
        else {
            if(text.equals("X")) {
                turn.setText("Turn: O");
                turn.setForeground(Color.blue);
            }
            else {
                turn.setText("Turn: X");
                turn.setForeground(Color.red);
            } 
        }
    }
    
    private void fillColorRow(int iDau, int jDau, int iCuoi, int jCuoi) {
        for(int j = jDau ; j <= jCuoi ; j++)
            cells[iDau][j].setBackground(Color.YELLOW);
    }
    
    private void fillColorColumn(int iDau, int jDau, int iCuoi, int jCuoi) {
        for(int i = iDau ; i <= iCuoi ; i++)
            cells[i][jDau].setBackground(Color.YELLOW);
    }
    
    private void fillColorDiagonaLine1(int iDau, int jDau, int iCuoi, int jCuoi) {
        for(int i = iDau, j = jDau ; i <= iCuoi && j >= jCuoi ; i++, j--) 
            cells[i][j].setBackground(Color.YELLOW);
    }
    
    private void fillColorDiagonaLine2(int iDau, int jDau, int iCuoi, int jCuoi) {
        for(int i = iDau, j = jDau ; i <= iCuoi && j <= jCuoi ; i++, j++) 
            cells[i][j].setBackground(Color.YELLOW);
    }
    
    private boolean checkDraw() {
        for (Button[] cell : cells) {
            for (Button cell1 : cell) {
                if (cell1.getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean checkWin(int hang, int cot) {
        int iMax = cells.length, jMax = cells[hang].length, i = hang, j = cot, count = 0, iDau = 0, jDau = 0, iCuoi = 0, jCuoi = 0;
        String str = cells[hang][cot].getText(); // Quân cờ tại vị trí i, j
        boolean chanDau = false, chanCuoi = false;
        
        // Case 1: Cùng hàng ⎼⎼⎼⎼⎼
        while(true) {
            if(j >= 0) {
                String str1 = cells[i][j].getText();
                if(str1.equals(str)) count++;
                else {
                    if(!str1.isEmpty())
                        chanDau = true;
                    break;
                }
                j--;
            }
            else break;
        }
        iDau = i;
        jDau = j+1;
        
        j = cot;
        while(true) {
            if(j < jMax) {
                String str1 = cells[i][j].getText();
                if(str1.equals(str)) count++;
                else {
                    if(!str1.isEmpty())
                        chanCuoi = true;
                    break;
                }
                j++;
            }
            else break;
        }
        iCuoi = i;
        jCuoi = j-1;
        System.out.println(iDau + "-" + jDau + ", " + iCuoi + "-" + jCuoi);
        if(count >= 6 && (chanDau == false || chanCuoi == false)) {
            fillColorRow(iDau, jDau, iCuoi, jCuoi);
            return true;
        }

        // Case 2: Cùng cột |
        i = hang;
        j = cot;
        count = 0;
        iDau = 0;
        jDau = 0;
        iCuoi = 0;
        jCuoi = 0;
        chanDau = false;
        chanCuoi = false;
        
        while(true) {
            if(i >= 0) {
                String str1 = cells[i][j].getText();
                if(str1.equals(str)) count++;
                else {
                    if(!str1.isEmpty())
                        chanDau = true;
                    break;
                }
                i--;
            }
            else break;
        }
        
        iDau = i+1;
        jDau = j;
        i = hang;
        while(true) {
            if(i < iMax) {
                String str1 = cells[i][j].getText();
                if(str1.equals(str)) count++;
                else {
                    if(!str1.isEmpty()) 
                        chanCuoi = true;
                    break;
                } 
                i++;
            }
            else break;
        }
        
        iCuoi = i-1;
        jCuoi = j;        
        System.out.println(iDau + "-" + jDau + ", " + iCuoi + "-" + jCuoi);
        if(count >= 6 && (chanDau == false || chanCuoi == false)) {
            fillColorColumn(iDau, jDau, iCuoi, jCuoi);
            return true;
        }        
        
        // Case 3: Cùng /
        i = hang;
        j = cot;
        count = 0;
        iDau = 0;
        jDau = 0;
        iCuoi = 0;
        jCuoi = 0;
        chanDau = false;
        chanCuoi = false;
        
        while(true) {
            if(i >= 0 && j < jMax) {
                String str1 = cells[i][j].getText();
                if(str1.equals(str)) count++;
                else {
                    if(!str1.isEmpty()) 
                        chanDau = true;
                    break;
                }
                i--; 
                j++;
            }
            else break;
        }
        
        iDau = i+1;
        jDau = j-1;        
        i = hang;
        j = cot;
        while(true) {
            if(i < iMax && j >= 0) {
                String str1 = cells[i][j].getText();
                if(str1.equals(str)) count++;
                else {
                    if(!str1.isEmpty()) 
                        chanCuoi = true;
                    break;
                }
                i++;
                j--;
            }
            else break;
        }
        
        iCuoi = i-1;
        jCuoi = j+1;
        System.out.println(iDau + "-" + jDau + ", " + iCuoi + "-" + jCuoi);        
        if(count >= 6 && (chanDau == false || chanCuoi == false)) {
            fillColorDiagonaLine1(iDau, jDau, iCuoi, jCuoi);
            return true;
        }        
        
        // Case 4: Cùng \
        i = hang;
        j = cot;
        count = 0;
        iDau = 0;
        jDau = 0;
        iCuoi = 0;
        jCuoi = 0;
        chanDau = false;
        chanCuoi = false;
        
        while(true) { 
            if(i >= 0 && j >= 0) {
                String str1 = cells[i][j].getText();
                if(str1.equals(str)) count++;
                else {
                    if(!str1.isEmpty()) 
                        chanDau = true;
                    break;
                }
                i--; 
                j--;
            }
            else break;
        }
        
        iDau = i+1;
        jDau = j+1;         
        i = hang;
        j = cot;
        while(true) {
            if(i < iMax && j < jMax) {
                String str1 = cells[i][j].getText();
                if(str1.equals(str)) count++;
                else {
                    if(!str1.isEmpty()) 
                        chanCuoi = true;
                    break;
                }
                i++;
                j++;
            }
            else break;
        }
        iCuoi = i-1;
        jCuoi = j-1;        
        System.out.println(iDau + "-" + jDau + ", " + iCuoi + "-" + jCuoi);        
        if(count >= 6 && (chanDau == false || chanCuoi == false)) {
            fillColorDiagonaLine2(iDau, jDau, iCuoi, jCuoi);
            return true;
        }         
        
        return false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOpenTurn(boolean openTurn) {
        this.openTurn = openTurn;
    }

    public String getTurn() {
        return this.turn.getText();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel center;
    private javax.swing.JPanel header;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel turn;
    // End of variables declaration//GEN-END:variables
}