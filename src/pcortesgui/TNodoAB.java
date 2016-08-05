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
public class TNodoAB {
  public TEstado estado;
  public TArbolBin padre;
  public String accion;
  public int costo, profundidad;
  // constructor
  public TNodoAB( TEstado e, TArbolBin p, String a, int c, int pr )
  {
      estado = e;
      padre = p;
      accion = a;
      costo = c;
      profundidad = pr;
  }
}
