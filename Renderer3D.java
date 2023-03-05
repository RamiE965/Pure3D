import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class Renderer3D {
    public static void main(String[] args) {
        // Select Object to draw & Initialization
        ArrayList<Triangle> pyramid = new ArrayList<Triangle>();
        ArrayList<Triangle> triangle = new ArrayList<Triangle>();
        ArrayList<Triangle> Square = new ArrayList<Triangle>();
        ArrayList<Triangle> shapeTemp = new ArrayList<Triangle>();
        System.out.println("Please Select The Shape You Would Like To Draw: \n1. Triangle \n2. Square \n3. Tetrahedron");
        Scanner input = new Scanner(System.in);
        switch(input.nextInt()) {
            case 1 -> shapeTemp = triangle;
            case 2 -> shapeTemp = Square;
            case 3 -> shapeTemp = pyramid;
        }
        final ArrayList<Triangle> shape = shapeTemp;
        input.close();
        
        // Window intialization
        JFrame window = new JFrame(); 
        Container pane = window.getContentPane();
        pane.setLayout(new BorderLayout()); 

        // Slider intialization
        JSlider horizontalSlider = new JSlider(-180, 180, 0);
        pane.add(horizontalSlider, BorderLayout.SOUTH); 
        JSlider verticalSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0); 
        pane.add(verticalSlider, BorderLayout.EAST);
        
        // Rendering Class 
        JPanel renderWindow = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g; // hacky bit
                g2.setColor(Color.WHITE);
                g2.fillRect(0,0, getWidth(), getHeight());

                // Triangle Object
                triangle.add(new Triangle(new Vertex(0, 0, 100),
                                        new Vertex(0, 100, 0),
                                        new Vertex(100, 0, 0),
                                        Color.RED));
            
                // Square Object
                Square.add(new Triangle(new Vertex(0, 0, 0),
                                        new Vertex(0, 100, 0),
                                        new Vertex(100, 0, 0),
                                        Color.RED));
                Square.add(new Triangle(new Vertex(100, 0, 0),
                                        new Vertex(100, 100, 0),
                                        new Vertex(0, 100, 0),
                                        Color.BLUE));
                
                // Pyramid Object
                pyramid.add(new Triangle(new Vertex(100, 100, 100),
                            new Vertex(-100, -100, 100),
                            new Vertex(-100, 100, -100),
                            Color.BLACK));
                pyramid.add(new Triangle(new Vertex(100, 100, 100),
                            new Vertex(-100, -100, 100),
                            new Vertex(100, -100, -100),
                            Color.RED));
                pyramid.add(new Triangle(new Vertex(-100, 100, -100),
                            new Vertex(100, -100, -100),
                            new Vertex(100, 100, 100),
                            Color.GREEN));
                pyramid.add(new Triangle(new Vertex(-100, 100, -100),
                            new Vertex(100, -100, -100),
                            new Vertex(-100, -100, 100),
                            Color.BLUE));
        
                // Slider Logic
                double horizontal = Math.toRadians(horizontalSlider.getValue());
                Matrix3 horizontalTransform = new Matrix3(new double[] {
                    Math.cos(horizontal), 0, -Math.cos(horizontal),
                    0, 1, 0,
                    Math.sin(horizontal), 0, Math.sin(horizontal)
                });
        
                double vertical = Math.toRadians(verticalSlider.getValue());
                Matrix3 verticalTransform = new Matrix3(new double[] {
                    1, 0, 0,
                    0, Math.cos(vertical), Math.sin(vertical),
                    0, -Math.sin(vertical), Math.cos(vertical)
                });
                Matrix3 transform = horizontalTransform.multiply(verticalTransform);
                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                // draw loop
                for (Triangle t : shape) {
                    Vertex v1 = transform.transform(t.v1);
                    Vertex v2 = transform.transform(t.v2);
                    Vertex v3 = transform.transform(t.v3);

                    v1.x += getWidth() / 2;
                    v1.y += getHeight() / 2;
                    v2.x += getWidth() / 2;
                    v2.y += getHeight() / 2;
                    v3.x += getWidth() / 2;
                    v3.y += getHeight() / 2;

                    // compute rectangular bounds for triangle
                    int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
                    int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
                    int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
                    int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

                    double triangleArea = ((v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x));

                    for (int y = minY; y <= maxY; y++) {
                        for (int x = minX; x <= maxX; x++) {
                            double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
                            double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
                            double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
                            if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) { // what
                                img.setRGB(x, y, t.color.getRGB());
                            }
                        }
                    }
                }
                g2.drawImage(img, 0, 0, null);
            }
        };

        // Slider event listeners
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

    // Matrix3 class with constructor +  matrix mult. method and transformation method
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
                        result[row * 3 + col] += this.values[row * 3 + i] * matrix.values[i * 3 + col]; // p smart what can i say
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