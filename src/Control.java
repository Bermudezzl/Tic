
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.geom.Point2D;
import javax.swing.JOptionPane;

public class Control implements MouseListener {

   final int size = 3;
   int[][] tablero = new int[size][size];
   int boton;
   Point punto;
   int ganador;
   boolean turno = true;
   boolean botonpulsado = false;
   ArrayList<myRectangle> zonapulsable = new ArrayList<myRectangle>();

   // constructor
   public Control() {
      this.crearTablero();
   }

   public void crearTablero() {
      for (int i = 0; i < tablero[0].length; i++) {
         for (int j = 0; j < tablero.length; j++) {
            tablero[i][j] = -1;
         }
      }
   }

   public int calcularTablero() {
      if ((tablero[0][0] != -1) && (tablero[0][0] == tablero[1][1]) && (tablero[1][1] == tablero[2][2])) {// diagonal
                                                                                                          // principal
         return tablero[1][1];
      }
      if ((tablero[2][0] != -1) && (tablero[2][0] == tablero[1][1]) && (tablero[1][1] == tablero[0][2])) {// diagonal
                                                                                                          // principal
         return tablero[1][1];
      }
      int n = this.calcularLinea();
      int m = this.calcularColumuna();
      if (n != -1) {
         return n;
      }
      if (m != -1) {
         return m;
      }

      return -1;
   }

   public boolean fin() {
      return (this.isCompleto()) || (this.calcularTablero() != -1);
   }

   public boolean isCompleto() {
      boolean condicion = true;
      for (int i = 0; i < tablero[0].length; i++) {// recorremos el tablero
         for (int j = 0; j < tablero.length; j++) {
            if (tablero[i][j] == -1) {
               condicion = false;
            }
         }
      }
      return condicion;
   }

   public int calcularLinea() {
      boolean condicion = false;
      int i = 0;
      int resul = -1;
      while ((condicion == false) && (i < tablero[0].length)) {
         if ((tablero[0][i] != -1) && (tablero[0][i] == tablero[1][i]) && (tablero[1][i] == tablero[2][i])) {// diagonal
                                                                                                             // principal
            resul = tablero[0][i];
            condicion = true;
         }
         i++;
      }
      if (condicion == true) {
         return resul;
      } else
         return -1;
   }


   // ARTIFICIAL INTELLIGENCE Russel Norving
   /*
    * función DECISIÓN-MINIMAX(estado) devuelve una acción
    * variables de entrada: estado, estado actual del juego
    * v ; MAX-VALOR(estado)
    * devolver la acción de SUCESORES(estado) con valor v
    * función MAX-VALOR(estado) devuelve un valor utilidad
    * si TEST-TERMINAL(estado) entonces devolver UTILIDAD(estado)
    * v ; 
    * para un s en SUCESORES(estado) hacer
    * v ; MAX(v, MIN-VALOR(s))
    * devolver v
    * función MAX-VALOR(estado) devuelve un valor utilidad
    * si TEST-TERMINAL(estado) entonces devolver UTILIDAD(estado)
    * v ; 
    * para un s en SUCESORES(estado) hacer
    * v ; MIN(v, MAX-VALOR(s))
    * devolver v
    */

   // Pseudocodigo
   /*
    * MiniMax()
    * best.mv = [not yet defined]
    * best.score = -99999
    * For each legal move m
    * {
    * make move m.mv on Board
    * m.score = MIN
    * if (m.score > best.score) then best = m
    * retract move m.mv on Board
    * }
    * Make move best.mv
    * ----------------------------------------
    * MAX() if (game over) return EVAL-ENDING
    * else if (max depth) return EVAL
    * else best.score = -99999
    * for each computer legal move m
    * { make move m.mv on Board
    * m.score = MIN
    * if (m.score > best.score) then best = m
    * retract move m.mv on Board
    * }
    * return best.score
    * 
    * ----------------------------------------
    * MIN() if (game over) return EVAL-ENDING
    * else if (max depth) return EVAL
    * else best.score = 99999
    * for each human legal move m.mv
    * { make move m.mv on Board
    * m.score = MAX
    * if (m.score < best.score) then best = m
    * retract move m.mv on Board
    * }
    * return best.score
    */

   public void minmax() {
      int filaOptima = 0;
      int columnaOptima = 0;
      int valorOptimo = -99999;

      // Iteramos sobre todas las celdas del tablero
      for (int i = 0; i < tablero[0].length; i++) {
         for (int j = 0; j < tablero.length; j++) {
            // Verificamos si la celda está vacía
            if (tablero[i][j] == -1) {
               tablero[i][j] = 1;// Intentamos la jugada para la computadora
               int aux = min();// Evaluamos la jugada
               tablero[i][j] = -1;// Deshacemos la jugada

               // Actualizamos la mejor jugada si la encontramos
               if (aux > valorOptimo) {
                  valorOptimo = aux;
                  filaOptima = i;
                  columnaOptima = j;
               }
            }
         }
      }

      tablero[filaOptima][columnaOptima] = 1;// Realizamos la jugada óptima para la computadora
   }

   public int min() {
      // Verificamos si el juego ha terminado
      if (this.fin()) {
         // Evaluamos la utilidad del estado terminal
         if (this.calcularTablero() != -1) {
            return 1;
         } else {
            return 0;
         }
      }

      int valorOptimo = 99999;

      // Iteramos sobre todas las celdas del tablero
      for (int i = 0; i < tablero[0].length; i++) {
         for (int j = 0; j < tablero.length; j++) {
            // Verificamos si la celda está vacía
            if (tablero[i][j] == -1) {
               tablero[i][j] = 0;// Intentamos la jugada para el jugador humano
               int aux = max();// Evaluamos la jugada
               tablero[i][j] = -1;// Deshacemos la jugada

               // Actualizamos el valor óptimo si lo encontramos
               if (aux < valorOptimo) {
                  valorOptimo = aux;
               }
            }
         }
      }

      return valorOptimo;// Devolvemos el valor óptimo para el jugador humano
   }

   public int max() {
      // Verificamos si el juego ha terminado
      if (this.fin()) {
         // Evaluamos la utilidad del estado terminal
         if (this.calcularTablero() != -1) {
            return -1;
         } else {
            return 0;
         }
      }

      int valorOptimo = -99999;

      // Iteramos sobre todas las celdas del tablero
      for (int i = 0; i < tablero[0].length; i++) {
         for (int j = 0; j < tablero.length; j++) {
            // Verificamos si la celda está vacía
            if (tablero[i][j] == -1) {
               tablero[i][j] = 1;// Intentamos la jugada para la computadora
               int aux = min();// Evaluamos la jugada
               tablero[i][j] = -1;// Deshacemos la jugada

               // Actualizamos el valor óptimo si lo encontramos
               if (aux > valorOptimo) {
                  valorOptimo = aux;
               }
            }
         }
      }

      return valorOptimo;// Devolvemos el valor óptimo para la computadora
   }

   public int calcularColumuna() {
      for (int i = 0; i < tablero.length; i++) {
         if ((tablero[i][0] != -1) && (tablero[i][0] == tablero[i][1]) && (tablero[i][1] == tablero[i][2])) {// diagonal
                                                                                                             // principal
            return tablero[i][0];
         }
      }
      return -1;
   }

   public void ejecutarFrame() {
      if (!this.fin()) {
         if (turno == true) {// si me toca
            if (this.botonpulsado) {
               if (this.getCasilla(punto)) {// si se ppuede pulsar esa casilla la marca y devuelve true si se ha podido
                                            // marcar una casilla
                  botonpulsado = false;
                  turno = false;
               }
            } else {

            }
         } else {
            this.minmax();
            turno = true;
         }
      } else {
         int n = this.calcularTablero();
         String ganador = "";
         if (n == 1) {
            ganador = "PC";
         } else if (n == 0) {
            ganador = "Usuario";
         } else {
            ganador = "Empate";
         }
         JOptionPane.showMessageDialog(null, "ha ganadoo el Jugador" + ganador);
         this.crearTablero();
      }
   }

   public boolean getCasilla(Point punto) {
      Point2D p2d = punto;
      boolean condicion = false;
      for (myRectangle mrect : this.getLcasillas()) {
         if (mrect.getRect().contains(p2d)) {
            System.out.print("tablero[" + mrect.getPosx() + "][" + mrect.getPosy() + "]==");
            if (tablero[mrect.getPosx()][mrect.getPosy()] == -1) {
               tablero[mrect.getPosx()][mrect.getPosy()] = 0;
               condicion = true;
            }
         }
      }
      return condicion;
   }

   public ArrayList<myRectangle> getLcasillas() {
      return zonapulsable;
   }

   public void setLcasillas(ArrayList<myRectangle> zonapulsable) {
      this.zonapulsable = zonapulsable;
   }

   public int[][] getTablero() {
      return tablero;
   }

   public void setTablero(int[][] tablero) {
      this.tablero = tablero;
   }

   // metodos abstractos
   @Override
   public void mouseClicked(MouseEvent e) {

   }

   @Override
   public void mousePressed(MouseEvent e) {
      punto = e.getLocationOnScreen();
      botonpulsado = true;
      /*
       * punto = e.getPoint();
       * boton = e.getButton();
       * System.out.println("estas presionando este boton "+boton);
       * this.getCasilla(punto);
       */
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      botonpulsado = false;
   }

   @Override
   public void mouseEntered(MouseEvent e) {

   }

   @Override
   public void mouseExited(MouseEvent e) {

   }

}
