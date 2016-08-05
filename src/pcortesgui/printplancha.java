package pcortesgui;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ealonso
 */

import java.awt.print.*;
import java.awt.*;


import edatos.*;


public class printplancha implements Printable   {
  // private attributes
   TRectangulo P;
   TLista S;
   double fac;
   int dim, pos;

  // constructor
  public printplancha(TRectangulo P, TLista S, int dim, int pos)
  {
      this.P = P;
      this.S = S;
      this.dim = dim;
      this.pos = pos;
  }

  private int Conv( double v )
  {
      return ( (int)(v*fac) );
  }

  public int print(Graphics g, PageFormat pf, int page)
            throws PrinterException
  {
      if ( P == null )
          return NO_SUCH_PAGE;
      if ( page > 0 )
          return NO_SUCH_PAGE;



      int i;
      int w, h, xs, ys, iwid, ihei;
      iwid = (int)pf.getImageableWidth();
      ihei = (int)pf.getImageableHeight();
      w = iwid-4;
      h = ihei-4;
      xs = (int)pf.getImageableX();
      ys = (int)pf.getImageableY();

      if ( pos == 0 )
          dim = 1;

      if ( P.obtAncho() > P.obtLargo() )
          fac = (double)P.obtAncho();
      else
          fac = (double)P.obtLargo();

      double nancho,nlargo;

      if ( w > h )
         fac = (double)h/(fac/dim);
      else
         fac = (double)w/(fac/dim);

      //System.out.println("width: " + w + "  height: " +  h);
      //return NO_SUCH_PAGE;

      nancho = P.obtAncho()/dim;
      nlargo = P.obtLargo()/dim;
      int f,c;
      double xxs, yys;
      if ( pos == 0 ) {
          xxs = 0;
          yys = 0;
      }
      else {
          f = (pos-1)/dim;
          c = (pos-1)%dim;
          xxs = c*nancho;
          yys = f*nlargo;
      }


      g.drawRect( xs, ys, Conv(nancho), Conv(nlargo));

      TNodoAB nodo;
      TRectangulo R;

      int x1, y1, w1, h1;

      nodo=null;
      for ( i = 0; i < S.Cantidad()-1; i++ )
        {

            nodo = (TNodoAB)S.Obtener(i);
            R = nodo.estado.RC;
            double vx = R.obtX()-xxs;
            double vy = R.obtY()-yys;
            if ( vx >= 0 && vx < nancho && vy >= 0 && vy < nlargo ) {
                x1 =  xs + Conv(vx);
                y1 = ys + Conv(vy);
                w1 = Conv(R.obtAncho());
                h1 = Conv(R.obtLargo());
                g.drawRect(x1,y1,w1,h1);
                //g.fillRect(x1+1,y1+1,w1-1,h1-1);
                g.drawString(String.valueOf(i+1), x1+w1/2, y1+h1/2+11/2);
                if ( pos != 0 ) {
                   g.drawString(String.valueOf(R.obtAncho()), x1+w1/2, y1+11+3);
                   g.drawString(String.valueOf(R.obtLargo()), x1+5, y1+h1/2+11/2);
                }
            }

      }

      Font ff = g.getFont();
      Font f2;
      f2 = ff.deriveFont(ff.ITALIC);
      g.setFont(f2);
      if ( i > 0 ) {
         nodo = (TNodoAB)S.Obtener(i);
         TLista D = nodo.estado.L;
         for ( i = 0; i < D.Cantidad(); i++ ) {
             R=(TRectangulo)D.Obtener(i);
             double vx = R.obtX()-xxs;
             double vy = R.obtY()-yys;
            if ( vx >= 0 && vx < nancho && vy >= 0 && vy < nlargo ) {
                 x1 =  xs + Conv(vx);
                 y1 = ys + Conv(vy);
                 w1 = Conv(R.obtAncho());
                 h1 = Conv(R.obtLargo());
                 g.drawRect(x1,y1,w1,h1);
                 //g.fillRect(x1+1,y1+1,w1-1,h1-1);
                 g.drawString(String.valueOf(i+1), x1+w1/2, y1+h1/2+11/2);
             }
         }
      }
    g.setFont(ff);
    return PAGE_EXISTS;

  }


}
