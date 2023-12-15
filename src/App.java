import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends JFrame implements Runnable {

    int width = 800; // Ancho de la ventana
    int height = 800; // Alto de la ventana
    int tcelda = 150; // Tamaño de una celda del tablero
    int desplazamiento = 170; // Desplazamiento del tablero
    Control control; // Objeto de la clase Control para gestionar el juego

    // Imágenes de la cruz y el círculo
    Image cross = this.cargarImagen("cross.png");
    Image circle = this.cargarImagen("circle.png");

    // Creación de un BufferedImage para dibujar en él
    BufferedImage bi = new BufferedImage(this.width, this.height, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D g2d = (Graphics2D) bi.getGraphics();

    // ArrayList para almacenar las zonas pulsables del tablero
    ArrayList<myRectangle> zonapulsable = new ArrayList<myRectangle>();

    // Método para cargar una imagen desde un archivo
    public Image cargarImagen(String nombre) {
        Image image = new ImageIcon(this.getClass().getResource(nombre)).getImage();
        return image;
    }

    // Método para crear las zonas pulsables del tablero
    public void crearZonaPulsable(Graphics g) {
        for (int i = 0; i < control.tablero[0].length; i++) {
            for (int j = 0; j < control.tablero.length; j++) {
                Rectangle2D rect = new Rectangle(desplazamiento + (i * tcelda), desplazamiento + (j * tcelda), tcelda,
                        tcelda);
                myRectangle mrect = new myRectangle(rect, i, j);
                this.zonapulsable.add(mrect);
            }
        }
    }

    // Método para pintar las casillas del tablero
    public void pintarCasillas(Graphics g) {
        Graphics2D g2d2 = (Graphics2D) g;
        for (myRectangle mrect : control.getLcasillas()) {
            g2d2.setColor(Color.WHITE);
            g2d2.draw(mrect.getRect());
        }
    }

    // Método para pintar el fondo de la ventana
    public void píntarFondo(Graphics g) {
        for (int i = 0; i < 100; i++) {
            Color c = new Color(180, 180, 180); // Color de fondo
            g.setColor(c);
            g.fillRect(0, i * 8, this.width, this.height);
        }
    }

    // Método para pintar los símbolos (cruz y círculo) en el tablero
    public void pintarSimbolo(Graphics g) {
        for (int i = 0; i < control.tablero[0].length; i++) {
            for (int j = 0; j < control.tablero.length; j++) {
                if (control.tablero[i][j] == 0) {
                    g.drawImage(circle, desplazamiento + (i * tcelda), desplazamiento + (j * tcelda), tcelda, tcelda,
                            this);
                }
                if (control.tablero[i][j] == 1) {
                    g.drawImage(cross, desplazamiento + (i * tcelda), desplazamiento + (j * tcelda), tcelda, tcelda,
                            this);
                }
            }
        }
    }

    // Método para pintar la ventana
    @Override
    public void paint(Graphics g) {
        this.píntarFondo(g2d);
        this.pintarCasillas(g2d);
        this.pintarSimbolo(g2d);
        g.drawImage(bi, 10, 10, this.width, this.height, this);
    }

    // Constructor de la clase App
    public App() {
        this.setSize(this.height, this.width); // Establecer el tamaño de la ventana
        this.setVisible(true); // Hacer visible la ventana
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // Cerrar la aplicación al cerrar la ventana
        this.setTitle("EL JUEGO DEL GATO"); // Título de la ventana
        control = new Control(); // Crear un objeto Control para gestionar el juego
        this.crearZonaPulsable(g2d); // Crear las zonas pulsables del tablero
        control.setLcasillas(zonapulsable); // Establecer las zonas pulsables en el objeto Control
        this.addMouseListener(control); // Agregar un MouseListener al objeto Control
        Thread hilo = new Thread(this); // Crear un hilo para ejecutar el juego
        hilo.start(); // Iniciar el hilo
    }

    // Método main para ejecutar el juego
    public static void main(String[] args) throws Exception {
        System.out.println("Iniciando juego");
        App juego = new App(); // Crear un objeto App para iniciar el juego
    }

    // Método que se ejecuta en el hilo del juego
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(150); // Pausa de 150 milisegundos
            } catch (InterruptedException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
            control.ejecutarFrame(); // Ejecutar el frame del juego en el objeto Control
            repaint(); // Volver a pintar la ventana
        }
    }
}