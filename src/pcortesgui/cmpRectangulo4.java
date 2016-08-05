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
public class cmpRectangulo4 implements TComparar {
  // private attributes

  // constructor
  public int Compara( Object a, Object b )
  {
      TRectangulo ra, rb;
      ra = (TRectangulo)a;
      rb = (TRectangulo)b;

      int da, db, dr;
      da = ra.obtAncho()*ra.obtLargo();
      db = rb.obtAncho()*rb.obtLargo();

      dr = da - db;
      if ( dr == 0 )
          return 0;
      else
          return da < db ? -1 : 1;
  }
}
