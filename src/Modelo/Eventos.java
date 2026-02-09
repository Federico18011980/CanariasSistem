package Modelo;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author feder
 */
public class Eventos {

    public void textKeyPress(KeyEvent evt) {
// este método permite solo caracteres        
// declaramos una variable y le asignamos un evento
        char car = evt.getKeyChar();
        if ((car < 'a' || car > 'z') && (car < 'A' || car > 'Z')
                && (car != (char) KeyEvent.VK_BACK_SPACE) && (car != (char) KeyEvent.VK_SPACE)) {
            evt.consume();
        }
    }

//    public void numberKeyPress(KeyEvent evt) {
////Este método permite solo números         
//// declaramos una variable y le asignamos un evento
//        char car = evt.getKeyChar();
//        if ((car < '0' || car > '9') && (car != (char) KeyEvent.VK_BACK_SPACE)) {
//            evt.consume();
//        }
//    }

    public void numberDecimalKeyPress(KeyEvent evt, JTextField textField) {
 //Este método permite solo decimales       
// declaramos una variable y le asignamos un evento
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && textField.getText().contains(".") && (car != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        } else if ((car < '0' || car > '9') && (car != '.') && (car != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        }
    }
    
    

    public void numberKeyPress(KeyEvent evt) {
        char c = evt.getKeyChar();

        // Permite borrar
        if (c == KeyEvent.VK_BACK_SPACE) {
            return;
        }

        // Permite solo números y un único '.' o ','
        if (!Character.isDigit(c) && c != '.' && c != ',') {
            evt.consume();
            return;
        }

        JTextField txt = (JTextField) evt.getSource();

        // Evita doble '.' o ','
        if ((c == '.' || c == ',') && 
           (txt.getText().contains(".") || txt.getText().contains(","))) {
            evt.consume();
        }
    }



}
