import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

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
                g2.fillRect(0,0, getWidth()*2, getHeight());
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