package de.vsy.client.gui.utility;

import java.awt.Component;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ComponentInputRemover {
  private ComponentInputRemover(){}

  public static void clearInput(Component comp){

    if (comp != null) {
      final var compList = comp.getParent().getComponents();

      for (var i = compList.length - 1; i >= 0; i--) {

        if (compList[i] instanceof JTextField) {
          ((JTextField) compList[i]).setText("");
        } else if (compList[i] instanceof JTextArea) {
          ((JTextArea) compList[i]).setText("");
        }
      }
    }
  }

}
