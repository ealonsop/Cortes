/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pcortesgui;

import edatos.*;

/**
 *
 * @author ealonso
 */
public class cmpRectangulo implements TComparar {
  // private attributes

  // constructor
  public int Compara( Object a, Object b )
  {
      TRectangulo ra, rb;
      ra = (TRectangulo)a;
      rb = (TRectangulo)b;

      int r = ra.obtLargo()-rb.obtLargo(); //alto
      if ( r == 0 )
          r = ra.obtAncho()-rb.obtAncho();
      return r;
  }
}
