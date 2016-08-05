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
public class cmpRectangulo3 implements TComparar {
  // private attributes

  // constructor
  public int Compara( Object a, Object b )
  {
      TRectangulo ra, rb;
      ra = (TRectangulo)a;
      rb = (TRectangulo)b;

      double da, db, dr;
      da = (double)ra.obtAncho()/ra.obtLargo();
      db = (double)rb.obtAncho()/rb.obtLargo();

      dr = da - db;
      if ( Math.abs(dr) <= 0.001 )
          return 0;
      else
          return da > db ? -1 : 1;
  }
}
