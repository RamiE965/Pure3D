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
                Graphics2D g2 = (Graphics2D) g; // hacky bit
                g2.setColor(Color.WHITE);
                g2.fillRect(0,0, getWidth(), getHeight());

                ArrayList<Triangle> Square = new ArrayList<Triangle>();
                Square.add(new Triangle(new Vertex(0, 0, 0),
                                        new Vertex(0, 100, 0),
                                        new Vertex(100, 0, 0),
                                        Color.RED));
                Square.add(new Triangle(new Vertex(100, 0, 0),
                                        new Vertex(100, 100, 0),
                                        new Vertex(0, 100, 0),
                                        Color.RED));
        
                // draw loop
                g2.translate(getWidth() / 2, getHeight() / 2);
                g2.setColor(Color.BLACK);
                


                double heading = Math.toRadians(horizontalSlider.getValue());
                Matrix3 horizontalTransform = new Matrix3(new double[] {
                    Math.cos(heading), 0, -Math.cos(heading),
                    0, 1, 0,
                    Math.sin(heading), 0, Math.sin(heading)
                });
        
                for (Triangle t : Square) {
                    Path2D path = new Path2D.Double();
                    double offSetY = (t.v1.y + t.v2.y)/2;
                    Vertex v1 = horizontalTransform.transform(t.v1);
                    Vertex v2 = horizontalTransform.transform(t.v2);
                    Vertex v3 = horizontalTransform.transform(t.v3);
                    path.moveTo(v1.x, v1.y-offSetY);
                    path.lineTo(v2.x, v2.y-offSetY);
                    path.lineTo(v3.x, v3.y-offSetY);
                    path.closePath();
                    g2.draw(path);
                }

                
                Matrix3 headingTransform = new Matrix3(new double[] {
                    Math.cos(heading), 0, Math.sin(heading),
                    0, 1, 0,
                    -Math.sin(heading), 0, Math.cos(heading)
                });

                double verticalSlide = Math.toRadians(verticalSlider.getValue());
                Matrix3 verticalTransform = new Matrix3(new double[] {
                    1, 0, 0,
                    0, Math.cos(verticalSlide), Math.sin(verticalSlide),
                    0, -Math.sin(verticalSlide), Math.cos(verticalSlide)
                });
                Matrix3 transform = headingTransform.multiply(verticalTransform);
        
                
                
            }
        };


        horizontalSlider.addChangeListener(e -> renderWindow.repaint());
        verticalSlider.addChangeListener(e -> renderWindow.repaint());
        
        pane.add(renderWindow, BorderLayout.CENTER);

        window.setSize(1600, 900);
        window.setVisible(true);
        
    }
    
    // Vertex Class & Constructor
    public static class Vertex {
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
    public static class Triangle {
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

    // defines a 3x3 Matrix Class with transform method
    // and multiplication method and a constructor
    public static class Matrix3 {
        double[] values;

        Matrix3(double[] values) {
            this.values = values;
        }

        Matrix3 multiply(Matrix3 matrix) {
            double[] result = new double[9];
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    for (int i = 0; i < 3; i++) {
                        result[row * 3 + col] += this.values[row * 3 + i] + matrix.values[i * 3 + col]; // p smart what can i say
                    }
                }
            }
            return new Matrix3(result);
        }
        Vertex transform(Vertex vertex) {
            return new Vertex(
                vertex.x * values[0] + vertex.y * values[3] + vertex.z * values[6],
                vertex.x * values[1] + vertex.y * values[4] + vertex.z * values[7], 
                vertex.x * values[2] + vertex.y * values[5] + vertex.z * values[8]
            );
        }
    }
}