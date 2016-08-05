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
public class TEstado {
    public int C;
    public TRectangulo RC;
    public TLista L;
    public int ri;
    public long ID;
  // private attributes

  // constructor
  public TEstado( int C, TRectangulo RC, int ri, TLista L )
  {
      this.C = C;
      this.RC = new TRectangulo(RC);
      this.ID = C;
      this.L = L;
      this.ri = ri;
  }
  public String toString()
  {
      String x;
      x = C + " RC: "  + RC + " R= " + L.Obtener(ri);
      for (int i=0; i<L.Cantidad();i++)
      {
       TRectangulo RL = (TRectangulo)L.Obtener(i);
       x = x + " " + RL.obtLargo()+"x"+RL.obtAncho();
      }
      return x;
  }
  public boolean equals( Object o )
  {
      TEstado e = (TEstado)o;
      if ( C != e.C )
          return false;
/*      if ( ri != e.ri )
          return false;*/
      if ( L.Cantidad() != e.L.Cantidad() )
          return false;
      for ( int i = 0; i < L.Cantidad(); i++ )
          if ( L.Obtener(i).equals(e.L.Obtener(i)) == false)
              return false;
      return true;

  }
}
