import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class Renderer3D {

    public static void main(String[] args) {
        // create a Window
        JFrame window = new JFrame(); 
        Container pane = window.getContentPane();
        pane.setLayout(new BorderLayout()); 

        // Slider
        JSlider horizontalSlider = new JSlider(0, 360, 180);
        pane.add(horizontalSlider, BorderLayout.SOUTH); 
        JSlider verticalSlider = new JSlider(SwingConstants.VERTICAL, 0, 180, 90); 
        pane.add(verticalSlider, BorderLayout.EAST);

        // Rendering Class
        JPanel renderWindow = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.WHITE);
                g2.fillRect(0,0, getWidth(), getHeight());

                ArrayList<Triangle> Square = new ArrayList<Triangle>();
                Square.add(new Triangle(new Vertex(100, 0, 0),
                                        new Vertex(0, 100, 0),
                                        new Vertex(100, 0, 100),
                                        Color.RED));
        
                // draw loop
                g2.translate(getWidth() / 2, getHeight() / 2);
                g2.setColor(Color.BLACK);
                for (Triangle t : Square) {
                    Path2D path = new Path2D.Double();
                    path.moveTo(t.v1.x, t.v1.y);
                    path.lineTo(t.v2.x, t.v2.y);
                    path.lineTo(t.v3.x, t.v3.y);
                    path.closePath();
                    g2.draw(path);
                }
            }
        };
        pane.add(renderWindow, BorderLayout.CENTER);
        window.setSize(1600, 900);
        window.setVisible(true);
    }
    
    // Vertex Class & Constructor
    class Vertex {
        double x;
        double y;
        double z;
        Vertex(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
    
    // Triangle Class & Constructor
    class Triangle {
        Vertex v1;
        Vertex v2;
        Vertex v3;
        Color color;
        Triangle(Vertex v1, Vertex v2, Vertex v3, Color color) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.color = color;
        }
    }
}