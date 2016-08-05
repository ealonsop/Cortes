/* * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pcortesgui;

import java.awt.*;
import edatos.*;

/**
 *
 * @author ealonso
 */
public class CPlancha extends Component {
  // private attributes

   TRectangulo P;
   TLista S;
   double fac;
   int dim, pos;
   int max;



  // constructor
  public CPlancha()
  {
      super();
      Actualiza(null,null,1,0);
  }

  public void Actualiza(TRectangulo P, TLista S, int dim, int pos )
  {
      this.P = P;
      this.S = S;
      this.dim = dim;
      this.pos = pos;
      if ( S != null )
         this.max = S.Cantidad();
      else
         this.max = 0;
  }
  public void cMax(int m)
  {
      max = m;
  }

  private int Conv( double v )
  {
      return ( (int)(v*fac) );
  }

  @Override
  public void paint(Graphics g) {
        //System.out.println("Dibujando...");
      //g.drawLine(0,0, 350,350);
      if ( P == null )
          return;
      int i;
      int w, h, xs, ys;
      w = 600-4;
      h = 600-4;
      xs = 2;
      ys = 2;

      if ( pos == 0 )
          dim = 1;
      
      if ( P.obtAncho() > P.obtLargo() )
          fac = (double)P.obtAncho();
      else
          fac = (double)P.obtLargo();

      double nancho,nlargo;

      fac = (double)w/(fac/dim);

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


      Color borde = new Color(0,0,0);
      Color intCorte = new Color(0,128,255);
      Color intSob = new Color(200,200,200);
      Color texto = new Color(255,255,0);
      Color textos = new Color(255,255,255);
      Color medidas = new Color(0,255,0);
      g.setColor(borde);
      g.drawRect( xs, ys, Conv(nancho), Conv(nlargo));

      TNodoAB nodo;
      TRectangulo R;

      int x1, y1, w1, h1;

      nodo=null;
      for ( i = 0; i < max-1/*S.Cantidad()-1*/; i++ )
        {

            nodo = (TNodoAB)S.Obtener(i);
            R = nodo.estado.RC;
            double vx = R.obtX()-xxs;
            double vy = R.obtY()-yys;
            if ( vx >= 0 && vx < nancho && vy >= 0 && vy < nlargo ) {
                g.setColor(borde);
                x1 =  xs + Conv(vx);
                y1 = ys + Conv(vy);
                w1 = Conv(R.obtAncho());
                h1 = Conv(R.obtLargo());
                g.drawRect(x1,y1,w1,h1);
                g.setColor(intCorte);
                g.fillRect(x1+1,y1+1,w1-1,h1-1);
                g.setColor(texto);
                g.drawString(String.valueOf(i+1), x1+w1/2, y1+h1/2+11/2);
                if ( pos != 0 ) {
                   g.setColor(medidas);
                   g.drawString(String.valueOf(R.obtAncho()), x1+w1/2, y1+11+3);
                   g.drawString(String.valueOf(R.obtLargo()), x1+5, y1+h1/2+11/2);
                }
            }

      }
      
      if ( i > 0 ) {
         nodo = (TNodoAB)S.Obtener(i);
         TLista D = nodo.estado.L;
         for ( i = 0; i < D.Cantidad(); i++ ) {
             R=(TRectangulo)D.Obtener(i);
             double vx = R.obtX()-xxs;
             double vy = R.obtY()-yys;
            if ( vx >= 0 && vx < nancho && vy >= 0 && vy < nlargo ) {
                 g.setColor(borde);
                 x1 =  xs + Conv(vx);
                 y1 = ys + Conv(vy);
                 w1 = Conv(R.obtAncho());
                 h1 = Conv(R.obtLargo());
                 g.drawRect(x1,y1,w1,h1);
                 g.setColor(intSob);
                 g.fillRect(x1+1,y1+1,w1-1,h1-1);
                 g.setColor(textos);
                 g.drawString(String.valueOf(i+1), x1+w1/2, y1+h1/2+11/2);
             }
         }
      }

  }

    @Override
  public Dimension getPreferredSize() {
       return new Dimension(600,600);
  }
}
