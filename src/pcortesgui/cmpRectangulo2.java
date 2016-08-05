/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pcortesgui;

import edatos.*;
/**
 *
 * @author laboratorio
 */
public class cmpRectangulo2 implements TComparar {
  // private attributes

  // constructor
  public int Compara( Object a, Object b )
  {
      TRectangulo ra, rb;
      ra = (TRectangulo)a;
      rb = (TRectangulo)b;

      int r = ra.obtAncho()-rb.obtAncho(); //ancho
      if ( r == 0 )
          r = ra.obtLargo()-rb.obtLargo();
      return r;
  }
}
