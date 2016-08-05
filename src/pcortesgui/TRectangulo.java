/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pcortesgui;

/**
 *
 * @author ealonso
 */
public class TRectangulo {
  // private attributes
    private int largo, ancho, x, y, pl;

  // constructor
  public TRectangulo(int largo, int ancho, int x, int y, int pl)
  {
      this.largo = largo;
      this.ancho = ancho;
      this.x = x;
      this.y = y;
      this.pl = pl;
  }
  public TRectangulo( TRectangulo R )
  {
      this.largo = R.obtLargo();
      this.ancho = R.obtAncho();
      this.x = R.obtX();
      this.y = R.obtY();
      this.pl = R.obtPlancha();
  }

  public int Area()
  {
      return this.largo*this.ancho;
  }
  public TRectangulo Rotar()
  {
      return new TRectangulo( this.ancho, this.largo, this.x, this. y, this.pl );
  }
  public int obtLargo()
  {
      return this.largo;
  }
  public int obtAncho()
  {
      return this.ancho;
  }
  public int obtX()
  {
      return this.x;
  }
  public int obtY()
  {
      return this.y;
  }
  public int obtPlancha()
  {
      return this.pl;
  }
  public void modX( int x )
  {
      this.x=x;
  }
  public void modY( int y )
  {
      this.y=y;
  }
  public void modAncho( int ancho )
  {
      this.ancho = ancho;
  }
  public void modLargo( int largo )
  {
      this.largo = largo;
  }
  public void modPlancha( int pl )
  {
      this.pl = pl;
  }

  public int ContieneA( TRectangulo R )
  {
      if ( this.largo >= R.obtLargo() &&
           this.ancho >= R.obtAncho() )
          return 1;
      else
      if ( this.ancho >= R.obtLargo() &&
           this.largo >= R.obtAncho() )
          return -1;
      else
          return 0;
  }
  public int DimIgual( TRectangulo R )
  {
      int x;
      //System.out.print("comparando: " + this + " con " + R);
      if ( this.largo == R.obtLargo() || this.ancho == R.obtAncho() )
          x=1;
      else
      if ( this.largo == R.obtAncho() || this.ancho == R.obtLargo() )
          x=-1;
      else
          x=0;
      //System.out.println(x != 0 ? " --->SIII" : " no");
      return x;
  }
  public String toString()
  {
      return x + "," + y+ "," + largo + "," + ancho;
  }
  public boolean equals( Object o )
  {
      TRectangulo r=(TRectangulo)o;
      return x==r.obtX() &&
             y==r.obtY() &&
             largo == r.obtLargo() &&
             ancho == r.obtAncho();
  }
}
