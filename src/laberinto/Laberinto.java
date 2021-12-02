package laberinto;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Laberinto implements ActionListener{

   

    /////////////////////////
    public static int[] mov = {0, 1, 2, 3};
    public static int mov1, mov2, mov3, mov4 = 0;
    public static int contador = 0;
    public static final int m = -10;
    public static final int p = 0;
    public static final int a1 = 870;
    public static final int a2 = 900;
    public static final int e = -50;
    public static final int s = 910;
    // Almacena posicion inicial y final de ambos agentes.
    public static int [] posicionInicialAgente1 = new int [2];
    public static int [] posicionInicialAgente2 = new int [2];
    public static int [] posicionFinalAgente1 = new int [2];
    public static int [] posicionFinalAgente2 = new int [2];
    
    static int tamAlto = 12;
    static int tamAncho = 12;
    
//    public static int [][] mapa = new int [tamAlto][tamAncho];
    public static int[][] mapa = {{  m, m, m, m, m, m, m, m, m, m, m, m},
                                    {m, s, s, s, s, s, 0, 0, 0, 0, 0, m},
                                    {m, 0, a2, 0, 0, e, 0, 0, 0, 0, 0, m},
                                    {m, e, e, e, e, e, 0, 0, 0, 0, 0, m},
                                    {m, 0, 0, 0, 0, 0, 0, e, 0, 0, 0, m},
                                    {m, 0, 0, 0, 0, 0, 0, e, 0, 0, 0, m},
                                    {m, e, e, e, e, e, e, 0, 0, 0, 0, m},
                                    {m, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, m},
                                    {m, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, m},
                                    {m, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, m},
                                    {m, 0, 0, 0, 0, 0, 0, 0, 0, 0, a1, m},
                                    {m, m, m, m, m, m, m, m, m, m, m, m}
    };
 
    public static boolean[][] mapaBool = {{false, false, false, false, false, false, false, false, false, false, false, false},
                                            {false, true, true, true, true, true, true, true, true, true, true, false},
                                            {false, true, true, true, true, true, true, true, true, true, true, false},
                                            {false, true, true, true, true, true, true, true, true, true, true, false},
                                            {false, true, true, true, true, true, true, true, true, true, true, false},
                                            {false, true, true, true, true, true, true, true, true, true, true, false},
                                            {false, true, true, true, true, true, true, true, true, true, true, false},
                                            {false, true, true, true, true, true, true, true, true, true, true, false},
                                            {false, true, true, true, true, true, true, true, true, true, true, false},
                                            {false, true, true, true, true, true, true, true, true, true, true, false},
                                            {false, true, true, true, true, true, true, true, true, true, true, false},
                                            {false, false, false, false, false, false, false, false, false, false, false, false}
                                            };



    public static JFrame frame;
    public static JPanel pnl;
    public static JPanel pnlMapa;
    public static JPanel pnlTablero;
    
    public static JButton btnInsertarEnemigo;
    public static JButton btnInsertarAgente;
    public static JButton btnInsertarSalida;

    public static JComboBox comboEnemigo;
    public static JComboBox comboSalida;
    private int cantidadEnemigo;
    private int cantidadSalida;
    
    public static JLabel[][] mapaG;
    public static ImageIcon imgPasillo;
    public static ImageIcon imgPasillo1;
    public static ImageIcon imgPasillo2;
    public static ImageIcon imgMuro;
    public static ImageIcon imgAgenteUno;
    public static ImageIcon imgAgenteDos;
    public static ImageIcon imgEnemigo;
    public static ImageIcon imgSalida;
    public static ImageIcon imgAnalizar;
    public static ImageIcon imgAnalizarAlternativa;
 
    public static Icon agenteUno;
    public static Icon agenteDos;
    public static Icon enemigo;
    public static Icon salida;
    public static Icon pasillo;
    public static Icon pasillo1;
    public static Icon pasillo2;
    public static Icon muro;
    public static Icon analizar;
    public static Icon analizarAlternativa;
    public static int dimension;
    public static JLabel lbl;

    JTextField txt;
    
    AgenteDos agente2;
    
    ////////////// Frame ////////////////////////
    
    static JFrame frmAgente1;
    static JPanel pnlAgente1;
    static JLabel lblDerecha1, lblIzquierda1, lblAbajo1, lblArriba1, lblNulo1;  
    static ImageIcon imgIzquierda;
    static ImageIcon imgDerecha;
    static ImageIcon imgArriba;
    static ImageIcon imgAbajo;
    static ImageIcon imgNulo;
    
  
//    static JButton [][] btnMtz1 = new JButton[tamAlto][tamAncho] ;
    static JButton [][] btnMtz1 = new JButton[12][12] ;
    
    static JFrame frmAgente2;
    static JPanel pnlAgente2;
    static JLabel mapaAgente2;
    static JLabel lblDerecha2, lblIzquierda2, lblAbajo2, lblArriba2, lblNulo2;
//    static JButton [][] btnMtz2 = new JButton[tamAlto][tamAncho] ;
    static JButton [][] btnMtz2 = new JButton[12][12] ;
    // Dimensiones del Frame Principal
    static int frameAlto = 700;
    static int frameAncho = 700;
    // Posicion del Frame principal
    static int frameX = 10;
    static int frameY = 10;
    // Tamaño imagen
    static int imgAlto = 0;
    static int imgAncho = 0;
    static int hints = 20;
    /////////////////////////////////////////////
    
    public Laberinto() {
        
        frame = new JFrame();
        // (450, 520)
        
//        frame.setSize(frameAlto, frameAncho);
        frame.setSize(450, 520);
        frame.setTitle("Laberinto");
        //(150, 200)
//        frame.setLocation(frameX, frameY);
        frame.setLocation(150, 200);
        frame.setBackground(Color.yellow);
        frame.setResizable(false);
        
        pnl = new JPanel();
        // (100, 200)
//        pnl.setSize((int)(frameAlto * 0.5) , (int)(frameAncho * 0.5));
        pnl.setSize(100 , 200);
        pnl.setBackground(new Color(126, 121, 120 ));
        
        
        pnlMapa = new JPanel();
        pnlMapa.setLayout(null);
        // (30, 70)
//        pnlMapa.setLocation((int)(frameX * 10),(int)(frameY * 10));
        pnlMapa.setLocation(30, 70);
        // (385, 390)
//        pnlMapa.setSize((int)(frameAlto * 0.80), (int)(frameAncho * 0.80));
        pnlMapa.setSize(385, 390);
        pnlMapa.setBackground(Color.gray);
        
//        imgAlto = (int)(pnlMapa.getHeight() / mapa.length );
//        imgAncho = (int)(pnlMapa.getWidth() / mapa.length);
        
        pnlTablero = new JPanel();
        pnlTablero.setLayout(null);
        // (0, 5)
        pnlTablero.setLocation(0, 5);
        // (450, 50)
        pnlTablero.setSize(450, 50);
        pnlTablero.setBackground(Color.red);
        btnInsertarEnemigo = new JButton("Enemigos");
        btnInsertarSalida = new JButton("Salidas");
        btnInsertarAgente = new JButton("Agentes");
        // Config boton enemigos
        // (95, 35)
        btnInsertarEnemigo.setSize(95, 35);
        btnInsertarEnemigo.setBackground(Color.white);
        // (5, 5)
        btnInsertarEnemigo.setLocation(5, 5);
        // Config boton salidas
        //(95, 35)
        btnInsertarSalida.setSize(95, 35);
        btnInsertarSalida.setBackground(Color.white);
        // (160, 5)
        btnInsertarSalida.setLocation(160, 5);
        // Config boton agentes
        // (90, 35)
        btnInsertarAgente.setSize(90, 35);
        btnInsertarAgente.setBackground(Color.white);
        // (310, 5)
        btnInsertarAgente.setLocation(310, 5);
        // Config ComboBox
        comboEnemigo = new JComboBox();
        comboSalida = new JComboBox();
        // Agrega item al combo enemigo desplegable
        int cantEnemi = 16;
        int valEnemi = 0;
        while(valEnemi <= cantEnemi){
            comboEnemigo.addItem(valEnemi);
            valEnemi = valEnemi + 1;
        }
        // Config de ComboBox enemigo
        //(40, 35)
        comboEnemigo.setSize(40, 35);
        //(100, 5)
        comboEnemigo.setLocation(100, 5);
        comboEnemigo.setBackground(Color.white);

        // Agrego item al combo salida desplegable
        int cantSal = 10;
        int valSal = 1;
        while(valSal <= cantSal){
            comboSalida.addItem(valSal);            
            valSal = valSal + 1;
        }
        // Config de ComboBox Salida
        // (40, 35)
        comboSalida.setSize(40, 35);
        // (255, 5)
        comboSalida.setLocation(255, 5);
        comboSalida.setBackground(Color.white);
        
        // Agrega listener a boton Enemigo.
        btnInsertarEnemigo.addActionListener(this);
        // Agrega listener a boton Agente.
        btnInsertarAgente.addActionListener(this);
        // Agrega listener a boton Salida
        btnInsertarSalida.addActionListener(this);
        
        // Imagenes de mapa
        imgPasillo = new ImageIcon("src/imagenes/0.png");
        imgPasillo1 = new ImageIcon("src/imagenes/01.png");
        imgPasillo2 = new ImageIcon("src/imagenes/02.png");
        imgMuro = new ImageIcon("src/imagenes/somIzqui.png");
        imgAgenteUno = new ImageIcon("src/imagenes/agenteUno.png");
        imgAgenteDos = new ImageIcon("src/imagenes/agenteDos.png");
        imgEnemigo = new ImageIcon("src/imagenes/enemigo.png");
        imgSalida = new ImageIcon("src/imagenes/salida.png");
        imgAnalizar = new ImageIcon("src/imagenes/0a.png");
        imgAnalizarAlternativa = new ImageIcon("src/imagenes/0aa.png");
        
        // (30, 30, 20)
        // imgAlto, imgAcho, hints
        // width, hight, hints
        pasillo = new ImageIcon(imgPasillo.getImage().getScaledInstance(30, 30, 20));
        pasillo1 = new ImageIcon(imgPasillo1.getImage().getScaledInstance(30, 30, 20));
        pasillo2 = new ImageIcon(imgPasillo2.getImage().getScaledInstance(30, 30, 20));
        muro = new ImageIcon(imgMuro.getImage().getScaledInstance(30, 30, 20));
        agenteUno = new ImageIcon(imgAgenteUno.getImage().getScaledInstance(30, 30, 20));
        agenteDos = new ImageIcon(imgAgenteDos.getImage().getScaledInstance(30, 30, 20));
        salida = new ImageIcon(imgSalida.getImage().getScaledInstance(30, 30, 20));
        enemigo = new ImageIcon(imgEnemigo.getImage().getScaledInstance(30, 30, 20));
        analizar = new ImageIcon(imgAnalizar.getImage().getScaledInstance(30, 30, 20));
        analizarAlternativa = new ImageIcon(imgAnalizarAlternativa.getImage().getScaledInstance(30, 30, 20));
        
        
        mapaG = new JLabel[mapa.length][mapa.length];
        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa.length; j++) {
                mapaG[i][j] = new JLabel();
                if ((i == 0)||(i == 11)||(j == 0)||(j == 11)) {
                    mapaG[i][j].setIcon(muro);
                    // ((i*32), (j*32), 40, 40)
                    // x , y, alto, ancho
//                    mapaG[i][j].setBounds((int)(i*imgAlto), (int)(j*imgAncho), (int)imgAlto, (int)imgAncho);
                    mapaG[i][j].setBounds((i*32), (j*32), 40, 40);
                }else{
                    switch(mapa[j][i]){
                        case p:
                            mapaG[i][j].setIcon(pasillo);
                            //((i*32), (j*32), 40, 40)
//                            mapaG[i][j].setBounds((int)(i*imgAlto), (int)(j*imgAncho), (int)imgAlto, (int)imgAncho);
                            mapaG[i][j].setBounds((i*32), (j*32), 40, 40);
                            break;
                        case m:
                            mapaG[i][j].setIcon(pasillo1);
//                            mapaG[i][j].setBounds((int)(i*imgAlto), (int)(j*imgAncho), (int)imgAlto, (int)imgAncho);
                            mapaG[i][j].setBounds((i*32), (j*32), 40, 40);
                            break;
                        case 2:
                            mapaG[i][j].setIcon(pasillo2);
//                            mapaG[i][j].setBounds((int)(i*imgAlto), (int)(j*imgAncho), (int)imgAlto, (int)imgAncho);
                            mapaG[i][j].setBounds((i*32), (j*32), 40, 40);
                            break;
                        case e:
                            mapaG[i][j].setIcon(enemigo);
//                            mapaG[i][j].setBounds((int)(i*imgAlto), (int)(j*imgAncho), (int)imgAlto, (int)imgAncho);
                            mapaG[i][j].setBounds((i*32), (j*32), 40, 40);
                            break;
                        case a1:
                            mapaG[i][j].setIcon(agenteUno);
                            mapaG[i][j].setBounds((i*32), (j*32), 40, 40);
                            break;
                        case a2:
                            mapaG[i][j].setIcon(agenteDos);
                            mapaG[i][j].setBounds((i*32), (j*32), 40, 40);
                            break;
                        case s:
                            mapaG[i][j].setIcon(salida);
                            mapaG[i][j].setBounds((i*32), (j*32), 40, 40);
                            break;
                    }

                }
                pnlMapa.add(mapaG[i][j]);
            }
        }

        pnlTablero.add(comboEnemigo);
        pnlTablero.add(comboSalida);
        pnlTablero.add(btnInsertarEnemigo);
        pnlTablero.add(btnInsertarSalida);
        pnlTablero.add(btnInsertarAgente);
        frame.add(pnlTablero);
        frame.add(pnlMapa);
        frame.add(pnl);
        frame.setVisible(true);
        agenteGrafico();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    
    // Crea los graficos de cada agente.
    public void agenteGrafico(){    
        frmAgente1 = new JFrame();
        frmAgente1.setBounds(630, 200, 280, 280);
        frmAgente1.getContentPane().setBackground(Color.BLACK);
        frmAgente1.setResizable(false);
        frmAgente1.setTitle("Agente Uno");
       
        frmAgente2 = new JFrame();
        frmAgente2.setBounds(950, 200, 280, 280);
        frmAgente2.getContentPane().setBackground(Color.BLACK);
        frmAgente2.setResizable(false);
        frmAgente2.setTitle("Agente Dos");
       
        pnlAgente1 = new JPanel();
        pnlAgente1.setLayout(null);
        pnlAgente1.setBounds(0, 100, 80, 80);
        pnlAgente1.setBackground(Color.white);
            
        pnlAgente2 = new JPanel();
        pnlAgente2.setLayout(null);
        pnlAgente2.setBounds(100, 0, 50, 50);
        pnlAgente2.setBackground(Color.white);
          
            
        //Label que soportaran las flechas de A1
        lblArriba1 = new JLabel();
        lblAbajo1 = new JLabel();
        lblIzquierda1 = new JLabel();
        lblDerecha1 = new JLabel();
            
        //Imagenes de flechas que indican el movimiento realizado
        imgIzquierda = new ImageIcon("src/imagenes/izquierdaP.png");
        imgDerecha = new ImageIcon("src/imagenes/derechaP.png");
        imgArriba = new ImageIcon("src/imagenes/arribaP.png");
        imgAbajo = new ImageIcon("src/imagenes/abajoP.png");
        imgNulo = new ImageIcon("src/imagenes/null.png");
            
        //Se cargan las imagenes para A1
        lblDerecha1.setIcon(imgNulo);
        lblDerecha1.setBounds(212, 120, 45, 45);
        pnlAgente1.add(lblDerecha1);
            
        lblIzquierda1.setIcon(imgNulo);
        lblIzquierda1.setBounds(5, 120, 45, 45);
        pnlAgente1.add(lblIzquierda1);
            
        lblArriba1.setIcon(imgNulo);
        lblArriba1.setBounds(110, 0, 45, 45);
        pnlAgente1.add(lblArriba1);
            
        lblAbajo1.setIcon(imgNulo);
        lblAbajo1.setBounds(110, 205, 45, 45);
        pnlAgente1.add(lblAbajo1);
            
        // Label que soportan las flechas de A2
        lblArriba2 = new JLabel();
        lblAbajo2 = new JLabel();
        lblIzquierda2 = new JLabel();
        lblDerecha2 = new JLabel();
            
        // Se cargan imagenes para A2
        lblDerecha2.setIcon(imgNulo);
        lblDerecha2.setBounds(212, 120, 45, 45);
        pnlAgente2.add(lblDerecha2);
            
        lblIzquierda2.setIcon(imgNulo);
        lblIzquierda2.setBounds(5, 120, 45, 45);
        pnlAgente2.add(lblIzquierda2);
            
        lblArriba2.setIcon(imgNulo);
        lblArriba2.setBounds(110, 0, 45, 45);
        pnlAgente2.add(lblArriba2);
            
        lblAbajo2.setIcon(imgNulo);
        lblAbajo2.setBounds(110, 205, 45, 45);
        pnlAgente2.add(lblAbajo2);

            
            // se crea matriz de Agente1
            for (int i = 0; i < btnMtz1.length; i++) {
                for (int j = 0; j < btnMtz1.length; j++) {
                    btnMtz1[i][j] = new JButton();
                    btnMtz1[i][j].setBounds(40+(i * 14), 40+(j * 14), 15, 15);
                    btnMtz1[i][j].setBackground(Color.lightGray);
                    pnlAgente1.add(btnMtz1[i][j]);
                }
            }
            for (int i = 0; i < btnMtz2.length; i++) {
                for (int j = 0; j < btnMtz2.length; j++) {
                    btnMtz2[i][j] = new JButton();
                    btnMtz2[i][j].setBounds(40+(i * 14), 40+(j * 14), 15, 15);
                    btnMtz2[i][j].setBackground(Color.lightGray);
                    pnlAgente2.add(btnMtz2[i][j]);                                        
                }
            }
        frmAgente1.add(pnlAgente1);
        frmAgente2.add(pnlAgente2);
        frmAgente1.setVisible(true);
        frmAgente2.setVisible(true);
    }
    
    // Inserta elementos de manera aleatoria
    public void insertarElemento(JLabel [][] mapaL, int [][] mapaI, int cantidad, int elemento){
        while(cantidad > 0){
            int fila = (int) Math.floor(Math.random()* 11) + 1;
            int columna = (int) Math.floor(Math.random()* 11) + 1;
            if (mapaI[fila][columna] == 0) {
                if (elemento == this.e) {
                    mapaI[fila][columna] = this.e;
                    mapaL[columna][fila].setIcon(enemigo);                    
                }else if(elemento == this.s){
                    mapaI[fila][columna] = this.s;
                    mapaL[columna][fila].setIcon(salida);
                }else if(elemento == this.a1){
                    mapaI[fila][columna] = this.a1;
                    mapaL[columna][fila].setIcon(agenteUno);
                }else if(elemento == this.a2){
                    mapaI[fila][columna] = this.a2;
                    mapaL[columna][fila].setIcon(agenteDos);
                }
                // ((columna*32), (fila*32), 40, 40)
//                mapaL[columna][fila].setBounds((int)(fila*imgAlto), (int)(columna * imgAncho), (int)imgAlto, (int)imgAncho);
                mapaL[columna][fila].setBounds((columna*32), (fila*32), 40, 40);
            }else{
                cantidad = cantidad + 1;
            }
            cantidad = cantidad - 1;
        }
           
    }
    
    public static void limpiarMapa(JLabel [][] mapaL, int [][]mapaI, int elemento){
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                if (mapaI[i][j] == elemento) {
                    mapaI[i][j] = 0;
                    mapaL[j][i].setIcon(pasillo);
                    // ((j*32), (i*32), 40, 40)
//                    mapaL[j][i].setBounds((int)(i*imgAlto), (int)(j*imgAncho), (int)imgAlto, (int)imgAncho);
                    mapaL[j][i].setBounds((j*32), (i*32), 40, 40);
                }
                
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btnInsertarEnemigo) {
            // Borro todos los enemigos del mapa
            this.limpiarMapa(this.mapaG, this.mapa, this.e);
            // Dibujo los enemigos
            cantidadEnemigo = (int)comboEnemigo.getSelectedItem();
            this.insertarElemento(this.mapaG, this.mapa, cantidadEnemigo, this.e);
        }else if(event.getSource() == btnInsertarSalida){
            // Borro todas las salidas del mapa
            this.limpiarMapa(this.mapaG, this.mapa, this.s);
            // Dibujo los enemigos
            cantidadSalida = (int)comboSalida.getSelectedItem();
            this.insertarElemento(this.mapaG, this.mapa, cantidadSalida, this.s);
        }else if (event.getSource() == btnInsertarAgente) {
            // Borro los agentes del mapa
            this.limpiarMapa(this.mapaG, this.mapa, this.a1);
            this.limpiarMapa(this.mapaG, this.mapa, this.a2);
            // Dibujo los agentes en el mapa
            this.insertarElemento(this.mapaG, this.mapa, 1, this.a1);
            this.insertarElemento(this.mapaG, this.mapa, 1, this.a2);
        }
        
    }



  


  

    
}