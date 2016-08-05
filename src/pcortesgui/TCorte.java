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
public class TCorte {
  public TLista estados;
  public TArbolBin arbol;
  public TLista objetivo;
  public TListaO cortes;
  public int nest;
  public boolean vertical, unir;

  // constructor
  public TCorte( TListaO Cortes, boolean vert, boolean unirlibres )
  {
      estados = new TListaA();
      objetivo = null;
      arbol = null;
      cortes = Cortes;
      nest = 0;
      vertical = vert;
      unir = unirlibres;
  }

  public TEstado AdicionarEstado( int C, TRectangulo RC, int ri, TLista L )
  {
      TEstado e = new TEstado(C,RC,ri,L);
      for ( int i = 0; i < estados.Cantidad(); i++ )
          if ( estados.Obtener(i).equals(e) == true)
              return e;
      estados.Adicionar(e);
      nest++;
     //System.out.println("\n"+nest+"("+e+")");
      return e;
  }

  public TLista DupLista( TLista L )
  {
      TLista A = new TListaP();
      for (int i = 0; i < L.Cantidad(); i++)
          A.Adicionar(L.Obtener(i));
      return A;
  }

  public TLista BuscarSolucionPA( TEstado e, TEstado o )
  {
      TNodoAB raiz,nodo;
      TArbolBin  araiz;
      TColaP frontera;

      objetivo = new TListaA();
      objetivo.Adicionar(o);

      raiz = new TNodoAB( e, new TArbolBin(), "", 0, 0 );
      arbol = new TArbolBin();
      arbol.CrearHoja();
      arbol.ModificarDato(raiz);
      frontera = new TColaP();
      frontera.Insertar(arbol);
      while ( true )
      {
          if ( frontera.Vacia() )
              return new TListaP();
          araiz = (TArbolBin)frontera.Eliminar();
          nodo = (TNodoAB)araiz.ObtenerDato();
          if ( esobjetivo(nodo.estado) )
              return crearsolucion(araiz);
          TLista exp = expandir(araiz);
          while ( !exp.Vacia() )
          {
              TArbolBin aux = (TArbolBin)(exp.Obtener(0));
              if ( esobjetivo( ((TNodoAB)aux.ObtenerDato()).estado ) )
                 return crearsolucion(aux);
              frontera.Insertar(aux);
              exp.Eliminar(0);
          }
      }
  }

public TLista BuscarSolucionPP( TEstado e, TEstado o  )
  {
      TNodoAB raiz,nodo;
      TArbolBin  araiz;
      TPilaP frontera;

      objetivo = new TListaA();
      objetivo.Adicionar(o);

      raiz = new TNodoAB( e, new TArbolBin(), "", 0, 0 );
      arbol = new TArbolBin();
      arbol.CrearHoja();
      arbol.ModificarDato(raiz);
      frontera = new TPilaP();
      frontera.Push(arbol);
      while ( true )
      {
          if ( frontera.Vacia() )
              return new TListaP();
          araiz = (TArbolBin)frontera.Pop();
          nodo = (TNodoAB)araiz.ObtenerDato();
          if ( esobjetivo(nodo.estado) )
              return crearsolucion(araiz);
          TLista exp = expandir(araiz);
          if ( exp == null ) return new TListaP();
          int c = exp.Cantidad();
          while ( c > 0 )
          {
              TArbolBin aux = (TArbolBin)(exp.Obtener(c-1));
              if ( esobjetivo( ((TNodoAB)aux.ObtenerDato()).estado ) )
                 return crearsolucion(aux);
              frontera.Push(aux);
              exp.Eliminar(c-1);
              c--;
          }
      }
  }

  public boolean esobjetivo( TEstado e )
  {
      for ( int i = 0; i < objetivo.Cantidad(); i++ )
      {
          TEstado ee = (TEstado)objetivo.Obtener(i);
          if ( ee.ID == e.ID && Expandible(e) )
              return true;
      }
      return false;
  }
  
  public TLista crearsolucion(TArbolBin araiz)
  {
      TLista res;
      res = new TListaP();
      while ( !araiz.Vacio() )
      {
          TNodoAB nodo = (TNodoAB)araiz.ObtenerDato();
          res.Insertar(nodo,0);
          araiz = nodo.padre;
      }
      return res;
  }

  public boolean Expandible( TEstado e )
  {
     TRectangulo R;
     if ( e.C >= cortes.Cantidad() ) return true;
     R = (TRectangulo)cortes.Obtener(e.C);
     for ( int i = 0; i < e.L.Cantidad(); i++ )
     {
        TRectangulo RL = (TRectangulo)e.L.Obtener(i);
        int ca = RL.ContieneA(R);
        if ( ca != 0 ) {
            if ( e.ri == -1 ) e.ri = i;
            return true;
        }
     }
     //System.out.println("\nNo expandible " + e );
     return false;

  }
  public boolean buscarencamino(TArbolBin araiz, TEstado e)
  {
      while ( !araiz.Vacio() )
      {
          TNodoAB nodo = (TNodoAB)araiz.ObtenerDato();
          if ( nodo.estado.ID == e.ID )
              return true;
          araiz = nodo.padre;
      }
      return false;
  }

  public void obteneraccionesposibles( TEstado e, int pa[] )
  {
      int i;
      for ( i = 0; i < 2; i++ )
          pa[i] = 0;
      if ( e.C >= cortes.Cantidad() )
          return;
      if ( Expandible(e) )
         for ( int j = 0; j < 2; j++ )
            pa[j] = 1;
  }

  private void UnirLibresV( TLista L )
  {
      int i, j;

      i = 0;
      while ( i < L.Cantidad() ) {
          TRectangulo RL = (TRectangulo)L.Obtener(i);
          for ( j = 0; j < L.Cantidad(); j++ )
              if ( i != j ) {
                 TRectangulo RD = (TRectangulo)L.Obtener(j);
                 if ( RL.obtPlancha() == RD.obtPlancha() &&
                      RL.obtAncho() == RD.obtAncho() &&
                      RL.obtX() == RD.obtX() &&
                      RD.obtY() == RL.obtY()+RL.obtLargo() )
                 { //unir verticalmente
                     RL.modLargo(RL.obtLargo()+RD.obtLargo());
                     L.Eliminar(j);
                     j = L.Cantidad();//para salir de J
                     i = -1;//empezar de nuevo
                 }
              }
          i++;
      }
  }

  private void UnirLibresH( TLista L )
  {
      int i, j;

      i = 0;
      while ( i < L.Cantidad() ) {
          TRectangulo RL = (TRectangulo)L.Obtener(i);
          for ( j = 0; j < L.Cantidad(); j++ )
              if ( i != j ) {
                 TRectangulo RD = (TRectangulo)L.Obtener(j);
                 if ( RL.obtPlancha() == RD.obtPlancha() &&
                      RL.obtLargo() == RD.obtLargo() &&
                      RL.obtY() == RD.obtY() &&
                      RD.obtX() == RL.obtX()+RL.obtAncho() )
                 { //unir verticalmente
                     /* System.out.println("Uniendo: " + RL.obtPlancha());
                      System.out.println(RL+" con "+RD);*/

                      RL.modAncho(RL.obtAncho()+RD.obtAncho());
                      L.Eliminar(j);
                      j = L.Cantidad();//para salir de J
                      i = -1;//empezar de nuevo
                 }
              }
          i++;
      }


  }

  public TEstado realizaraccion( TEstado e, int a )
  {
        int x, y, largo, ancho;
        TRectangulo re, R1, R2, R, NC;
        int ca, ri, valm, cai, exactoi, exactom, rei;

        re =  e.RC;
        ri = -1;
        ca = 0;
        cai=-1;
        valm=0;
        exactoi=0;
        rei=-1;
        exactom=0;
        if ( unir )
           if ( vertical )
              UnirLibresH( e.L );
           else
              UnirLibresV( e.L );

//        System.out.println("Realizando accion: " + a + " " + e );
//        System.out.println("Comparando con: ");
        for ( int i = 0; i < e.L.Cantidad(); i++ )
        {
           TRectangulo RL = (TRectangulo)e.L.Obtener(i);
      //     System.out.print("   " + RL);
           ca = RL.ContieneA(re);
           if ( ca != 0 )
           {
  //             System.out.println("--->Lo contiene");
               int area = RL.Area()-re.Area();
               if ( ri == -1 || valm > area ) {
                   ri = i;
                   cai = ca;
                   valm=area;
               }
               int exacto = RL.DimIgual(re);
               if ( exacto != 0 && (rei == -1 || exactom > area) ) {
                   //System.out.println("         Exacto econtrado " + e);
                   rei = i;
                   exactoi = exacto;
                   exactom = area;
               }
           }
           else
              ;// System.out.println("--->no");
        }
        if ( ri == -1 )
            return null;

        if ( rei != -1 ) {
//            System.out.println("Quedandome con el exacto...");
            e.ri = rei;
            if ( exactoi < 0 ) {
               re = re.Rotar();
               e.RC = re;
            //   System.out.println("Rotado... " + re);
            }
        }
        else {
            e.ri = ri;
            if ( cai < 0 ) {
               re = re.Rotar();
               e.RC = re;
            //   System.out.println("Rotado... " + re);
            }
        }

        R = (TRectangulo)e.L.Obtener(e.ri);
    //    System.out.println("      Se quedó con " + R);
        e.RC.modX(R.obtX());
        e.RC.modY(R.obtY());

        switch ( a )
        {
            case 0: // corte horizontal
                // superior
                x = R.obtX() + re.obtAncho();
                y = R.obtY();
                largo = re.obtLargo();
                ancho = R.obtAncho()-re.obtAncho();
                if ( largo > 0 && ancho > 0 )
                   R1 = new TRectangulo(largo,ancho,x,y,R.obtPlancha());
                else
                   R1 = null;
                // abajo
                x = R.obtX();
                y = R.obtY()+re.obtLargo();
                ancho = R.obtAncho();
                largo = R.obtLargo()-re.obtLargo();
                if ( largo > 0 && ancho > 0 )
                   R2 = new TRectangulo(largo,ancho,x,y,R.obtPlancha());
                else
                   R2 = null;
                break;
            case 1: // corte vertical
                // izquierda
                x = R.obtX();
                y = R.obtY()+re.obtLargo();
                ancho = re.obtAncho();
                largo = R.obtLargo()-re.obtLargo();
                if ( largo > 0 && ancho > 0 )
                   R1 = new TRectangulo(largo,ancho,x,y,R.obtPlancha());
                else
                   R1 = null;
                // Derecha
                x = R.obtX() + re.obtAncho();
                y = R.obtY();
                largo = R.obtLargo();
                ancho = R.obtAncho()-re.obtAncho();
                if ( largo > 0 && ancho > 0 )
                   R2 = new TRectangulo(largo,ancho,x,y,R.obtPlancha());
                else
                   R2 = null;
                break;
            default:
                return null;
        }

        TLista L;
        L = DupLista( e.L );
        L.Eliminar(e.ri);
        if ( R1 != null )
           L.Adicionar(R1);
        if ( R2 != null )
           L.Adicionar(R2);
        if ( e.C+1 < cortes.Cantidad() )
           NC = (TRectangulo)cortes.Obtener(e.C+1);
        else
           NC = new TRectangulo(0,0,0,0,0);

        return AdicionarEstado( e.C+1, NC, -1, L );
  }

  public TLista expandir(TArbolBin araiz)
  {
      TLista res;
      TNodoAB nodo;
      int pa[], i;
      TEstado o;

      res = new TListaP();

      nodo = (TNodoAB)araiz.ObtenerDato();
      pa = new int[2];// 10 acciones posibles
      obteneraccionesposibles( nodo.estado, pa );
      //System.out.println("Expandiendo: " + nodo.estado );

      for( i = 0; i < 2; i++ )
      {
          if ( pa[i] != 0 )
          {
              TEstado e = realizaraccion( nodo.estado, i );
              if ( e != null && !buscarencamino(araiz,e) )
              {
                if ( Expandible(e) )
                {
                TNodoAB n;
                //System.out.println("  Expandido a: " + i + " " + e );
                n = new TNodoAB( e, araiz, String.valueOf(i) ,
                      nodo.costo+1,
                      nodo.profundidad+1 );
                TArbolBin hijo = new TArbolBin();
/*                if ( i == 1 )
                  System.out.println("  Expandido a: " + i + " " + e );*/

                hijo.CrearHoja();
                hijo.ModificarDato(n);
                //n.accion = i == 0 ? "i" : "d";
                if ( i==0)
                  araiz.ModificarIzq(hijo);
                else
                  araiz.ModificarDer(hijo);
                if ( vertical )
                   res.Insertar(hijo, 0);
                else
                   res.Adicionar(hijo);
                }
                else
                {
                  return null;
                }
              }

          }
      }
      return res;
  }
}
