import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class DigitPanel extends JPanel implements MouseListener, ActionListener, MouseMotionListener{
    Timer timer;
    double[][] screen;
    boolean down = false;
    String cur = "";
    public DigitPanel(){
        screen = new double[28][28];

        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        timer = new Timer(1000/60,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        setBackground(new Color(255,255,255));
        for(int i = 0;i<28;i++){
            for(int j = 0;j<28;j++){
                g.setColor(new Color((int)((1-screen[i][j])*255),(int)((1-screen[i][j])*255),(int)((1-screen[i][j])*255)));
                g.fillRect(i*20,j*20,20,20);

            }
        }
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(new Color(0,0,0));
        g.drawString(cur,20,560);
    }

    public void actionPerformed(ActionEvent e){
        cur = ""+main.getMax(main.nn.calculate(convertImage()));
        repaint();
    }

    public void mouseExited(MouseEvent e){

    }

    public void mouseEntered(MouseEvent e){

    }

    public void mouseReleased(MouseEvent e){
        down = false;

    }

    private void paintAt(MouseEvent e){
        int cx = e.getX()/20, cy = e.getY()/20;
        boolean erase = SwingUtilities.isRightMouseButton(e);
        for(int dx=-1; dx<=1; dx++){
            for(int dy=-1; dy<=1; dy++){
                int x = cx+dx, y = cy+dy;
                if(x<0 || x>=28 || y<0 || y>=28){ 
                    continue;
                }
                double strength = .25;
                if(dx == 0){
                    if(dy == 0){
                        strength = 1;
                    }
                    else{
                        strength = .5;
                    }
                }
                else if(dy == 0){
                    strength = .5;
                }
                if(erase){
                    screen[x][y] = 0;
                }
                else{
                    screen[x][y] = Math.min(1, screen[x][y] + strength);
                }
            }
        }
    }

    public void mousePressed(MouseEvent e){ 
        if(SwingUtilities.isMiddleMouseButton(e)){
            for(int i = 0;i<28;i++){
                for(int j = 0;j<28;j++){
                    screen[i][j] = 0;
                }
            }
        }
        else{
            down = true; 
            paintAt(e); 
        }
    }

    public void mouseDragged(MouseEvent e){
        if(!SwingUtilities.isMiddleMouseButton(e)){
            paintAt(e); 
        }
    }

    public void mouseClicked(MouseEvent e){

    }

    public void mouseMoved(MouseEvent e){
    }

    public double[] convertImage(){
        double total=0, mx=0, my=0;
        for(int x=0;x<28;x++) for(int y=0;y<28;y++){
                total += screen[x][y]; mx += x*screen[x][y]; my += y*screen[x][y];
            }
        double[] nums = new double[784];
        if(total == 0) return nums;
        int sx = (int)Math.round(13.5 - mx/total);
        int sy = (int)Math.round(13.5 - my/total);
        for(int x=0;x<28;x++) for(int y=0;y<28;y++){
                int srcX = x - sx, srcY = y - sy;
                if(srcX>=0 && srcX<28 && srcY>=0 && srcY<28)
                    nums[y*28+x] = screen[srcX][srcY];
            }
        return nums;
    }
}