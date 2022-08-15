package laberinto;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class AgenteDos extends Agent{
    // Contiene el movimiento que realizara el agente
    private String movAgente2;
    // cuando corteDos sea true, finalizara la ejecucion del comportamiento "MovimientoDos"
    private boolean corteDos,c2, encerrado = false;
    // Permite ingresar al if para poder pintar la posicion en la que se encuentra A2
    private boolean incializadorDeGraficoDos = false;
    // Varibles que contienen el valor de fila y columna
    private int fila, columna = 0;
    // En caso de ser true indicara la finalizacion del juego
    private boolean finDos = false;
    // Crea el objeto del laberinto
    private Laberinto lab = new Laberinto();
    // Posicion actual de Agente2
    private int [] posicion;
    // Variables que ayudaran a almacenar el valor de posiciones actuales de los agentes
    private int iteradorX = 0;
    private int iteradorY = 0;
    private int inicioA1x = 0;
    private int inicioA1y = 0;
    
    private int filaBis, columnaBis = 0;
    private int x1 = 0;
    private int y1 = 0;
    
    private boolean movimientoAlternativo = true;
    private boolean objeto2 = false;
    private boolean recorridoAgente1 = false;
    private boolean recalcular2 = false;
    private boolean inicializadorDeGrafica = false;
    
    // Matriz que alojara los valores de la solucion
    private int [][] matrizSolucion;
    
    // Array que contiene los movimiento no validos
    private int [] movInval = new int [4];
    // Almacena el recorrido del A1 en caso de quedar encerrado
    private String recorridoA1 = new String();
    private String caminoSucio1 = new String("");
    
    // Metodo que se ejecutara una vez creado el AgenteDos
    protected void setup(){
        addBehaviour(new MovimientoDos());
    }
    
    //Se ejecuta cuando finaliza el agente.
    protected void takeDown(){
        System.out.println("Fin AgenteDos");
    }
 
    
    /*
    Behaviour: comportamiento generico que se ejecuta hasta que se
    cumpla una condicion (y se ejecute metodo "done()").
    */
    private class MovimientoDos extends Behaviour{
       
        /* MssageTemplate es utilizado para filtrar diferentes mensajes de diferentes agentes
           El valor INFORM nos sirve para comunicar un hecho al AgenteUno
        */
        private MessageTemplate mt2 = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

        // Se sobrescribe el metodo 'action' de la clase 'Behaviour', en el se establecen los comportamiento.
        @Override
        public void action() {
        
           this.miTimer(250);
           /* Los mensajes enviados/recibidos por agentes son instancias de ACLMessage por lo que creamos una
              instancia de este
           */
           ACLMessage msgResp = myAgent.receive(mt2);
           /*
            Durante la ejecucion, del AgenteDos, los mensajes enviados/recibidos pueden tener tres ontologias diferentes:
             - movimiento: agente pudo hacer un movimiento por lo que lo comunica
             - ecerrado: el agente no tiene movimiento para realizar.
             - recorrido: el AgenteDos encontro una salida por lo que se lo comunica al AgenteUno, adicionalmente
                le envia el camino realizado.
           */
           
           // Condicional que consulta si el AgenteUno pudo moverse.
           if((msgResp != null) && (msgResp.getOntology().equals("movimiento"))){
                    /* Condicional que se ejecutara una sola vez y busca incializar las variable referidas al grafico, de esta
                        manera poder pintarlo.
                    */
                    if (incializadorDeGraficoDos == false) {
                        // Obtiene la posicion actual en la que se encuentra el AgenteDos en el laberinto
                        Laberinto.posicionInicialAgente2 = posicionActual(Laberinto.mapa, Laberinto.a2);
                        int [] posicion = posicionActual(Laberinto.mapa, Laberinto.a2);
                        // Pinta de azul su posicion actual en el "Laberinto individual".
                        Laberinto.btnMtz2[posicion[1]][posicion[0]].setBackground(Color.blue);
                        // Guardamos valores de fila y columna en variables del mismo nombre, para un trato mas eficiente.
                        fila = posicion[0];
                        columna = posicion[1];
                        // Se ocultan los botones del frame en el momento que comienza la ejecucion.
                        incializadorDeGraficoDos = true;
                        Laberinto.btnInsertarEnemigo.setEnabled(false);                        
                        Laberinto.btnInsertarAgente.setEnabled(false);
                        Laberinto.btnInsertarSalida.setEnabled(false);
                        Laberinto.comboEnemigo.setEnabled(false);
                        Laberinto.comboSalida.setEnabled(false);
                   }
    
                    // Crea un objeto que contendra la respuesta al mensaje recibido.
                   ACLMessage movResp = msgResp.createReply();
                   // Guarga, solamente, el contenido del mensaje recibido.
                   String m = msgResp.getContent();
                   if (m.equals("a") || (m.equals("s") || (m.equals("w") || (m.equals("d"))))) {
                    // El objeto de respuesta es seteado con una performative "INFORM"
                    movResp.setPerformative(ACLMessage.INFORM);
                    // Elige la proxima direccion en la que debe moverse
                      switch(direccion()){
                        case "s":
                            // Si el agente puede moverse
                            if (encerrado == false) {
                                /* Si el AgenteDos encontro una salida se envia un mensaje con el movimiento realizado y
                                   y la ontologia correspondiente, de lo contrario se envia el movimiento realizado
                                */
                                if(finDos == true){
                                    movResp.setOntology("recorrido");
                                    movAgente2 = "s";
                                    Laberinto.posicionFinalAgente2 = posicionActual(Laberinto.mapa, Laberinto.a2);
                                }else{
                                    movResp.setOntology("movimiento");      
                                    movAgente2 = "s";
                                }                                
                            }else{
                                // Comunica que no puede moverse y quedo encerrado
                                movResp.setOntology("encerrado");
                                movAgente2 = "s";
                            }
                            // Se agrega el contenido al mensaje.
                            movResp.setContent(movAgente2);
                            break;
                        
                        case "a":
                            if (encerrado == false) {
                                if (finDos == true) {
                                    movResp.setOntology("recorrido");
                                    movAgente2 = "a";
                                    Laberinto.posicionFinalAgente2 = posicionActual(Laberinto.mapa, Laberinto.a2);
                                }else{
                                    movResp.setOntology("movimiento");
                                    movAgente2 = "a";
                                }                                
                            }else{
                                movResp.setOntology("encerrado");
                                movAgente2 = "a";
                            }
                            movResp.setContent(movAgente2);
                            break;

                        case "w":
                            if (encerrado == false) {
                                if (finDos == true) {
                                    movResp.setOntology("recorrido");
                                    movAgente2 = "w";
                                    Laberinto.posicionFinalAgente2 = posicionActual(Laberinto.mapa, Laberinto.a2);
                                }else{
                                    movResp.setOntology("movimiento");
                                    movAgente2 = "w";
                                }                                
                            }else{
                                movResp.setOntology("encerrado");
                                movAgente2 = "w";
                            }
                            movResp.setContent(movAgente2);
                            break;
                            
                        case "d":
                            if(encerrado == false){
                                if (finDos == true) {
                                    movResp.setOntology("recorrido");
                                    movAgente2 = "d";
                                    Laberinto.posicionFinalAgente2 = posicionActual(Laberinto.mapa, Laberinto.a2);                                    
                                }else{
                                    movResp.setOntology("movimiento");
                                    movAgente2 = "d";
                                }
                            }else{
                                movResp.setOntology("encerrado");
                                movAgente2 = "d";
                            }
                            movResp.setContent(movAgente2);
                            break;
                   }
                             
                   this.miTimer(250);
                   // Setea los valores de todos las imagenes que representan el movimiento del agenete2 en el laberinto individual
                   Laberinto.lblArriba2.setIcon(Laberinto.imgNulo);
                   Laberinto.lblAbajo2.setIcon(Laberinto.imgNulo);
                   Laberinto.lblIzquierda2.setIcon(Laberinto.imgNulo);
                   Laberinto.lblDerecha2.setIcon(Laberinto.imgNulo);    
                   // Envia la respuesta al AgenteUno 
                   myAgent.send(movResp);
               }
         /* Condicional que consulta si el AgenteUno se pudo mover con exito.
            La diferencia entre la ontologia 'movimiento' y 'camino' es la siguiente:
                   'movimineto' hara referencia siempre a los movimiento que AgenteDos haya podido hacer con exito. Por otro lado,
                   'camino' significa que el AgenteDos quedo encerrado. Esto es asi porque AgenteDos no puede pasar dos veces
                   por la misma casilla
         */
         }else if((msgResp != null) && (msgResp.getOntology().equals("camino"))){
             // Crea una respuesta 
             ACLMessage movResp = msgResp.createReply();
             // Almacena en una variable, unicamente, el contenido del mensaje recibido (el movimiento realizado por AgenteUno)
             String m = msgResp.getContent();
             // Concatena todos los movimientos recibidos
             recorridoA1 = recorridoA1 + m;
             // Se setea el mensaje con los valores correspondientes
             movResp.setPerformative(ACLMessage.INFORM);
             movResp.setContent("a");
             movResp.setOntology("encerrado");
             // Envia el mensaje
             myAgent.send(movResp);
          // Condicional que consulta si el AgenteUno encontro una salida
         }else if((msgResp != null) && (msgResp.getOntology().equals("recorrido"))){
             // Almacena su posicion actual
            Laberinto.posicionInicialAgente2 = posicionActual(Laberinto.mapa, Laberinto.a2);
            
            // Setea variables correspondioente a la 'grafica individual del AgenteDos'
            if (inicializadorDeGrafica == false) {
                posicion = posicionActual(Laberinto.mapa, Laberinto.a2);
                fila = posicion[0];
                columna = posicion[1];
                Laberinto.btnMtz2[columna][fila].setBackground(Color.blue);
                inicializadorDeGrafica = true;
            }
            
            // Bucles anidados que buscan setear matriz booleana en false, asi evitar interferencias en los movimientos
            for (int i = 0; i < Laberinto.mapaBool.length; i++) {
                for (int j = 0; j < Laberinto.mapaBool.length; j++) {
                    Laberinto.mapaBool[i][j] = true;
                }
            }
            
            // crea matriz de solucion con la dimension del tablero
            matrizSolucion = new int [Laberinto.tamAncho][Laberinto.tamAlto];
            matrizSolucion[fila][columna] = Laberinto.a1;
                    
            // Almacena el ultimo movimiento ejecutado por el Agente1
            String recorrido = msgResp.getContent();
            recorridoA1 = recorridoA1 + recorrido;
            
            // Bucle que que se ejecutara hasta que AgenteDos llegue a destino
            while(corteDos == false){
                //
                this.continuarCaminoRecibido();
            }
            corteDos = true;           
         }
       
    }
        
        /* Funcion que elige, de manera aleatoria, la direccion hacia la cual se movera el agente.
           Esta funcion se ejecutara siempre que la ontologia recibida sea 'movimiento'.
        */ 
        public String direccion(){
            // Permite controlar el bucle while    .
            boolean corteDireccion = true;
            // Almacena el valor que devuelve la  funcion.
            String direccionRetornoDos = "";
            /*
            0: abajo
            1: izquierda
            2: arriba
            3: derecha
            */
            while(corteDireccion==true){
              // Elige un valor entre 0 y 3 que representara el movimiento ha realizar.
              int dir = (int) Math.floor(Math.random()*4);
                switch(dir){
                    case 0: //Abajo
                        movInval[0] = movInval[0] + 1;
                        // Estructura de contro que verifica la ausencia de muro, enemigo, agente uno y que la matriz de recorrido
                        // tenga en icha posicion valor 'true'.
                        if ((Laberinto.mapa[fila+1][columna] != Laberinto.m) && (Laberinto.mapa[fila+1][columna] != Laberinto.e) && (Laberinto.mapaBool[fila+1][columna] == true) && (Laberinto.mapa[fila+1][columna] != Laberinto.a1)) {
                            // Si encontro la salida termina
                            if (Laberinto.mapa[fila+1][columna] == Laberinto.s) {
                                finDos = true;
                                // Toma el valor true indicando que encontro la salida por ende termina la ejecucion del
                                // comportamiento "MovimientoDos"
                                corteDos = true;
                            }
                            // Settea con "s" el valor que se le enviara al Agente1
                            direccionRetornoDos = "s";
                            // Llama al metodo que llevara a cabo el movimiento hacia abajo.
                            actualizarAbajo();                                
                            // Settea con false para poder salir del bucle while.
                            corteDireccion = false;
                        }else{
                            if ((movInval[0] > 0) && (movInval[1] > 0) && (movInval[2] > 0) && (movInval[3] > 0)) {
                                encerrado = true;
                                corteDireccion = false;
                                direccionRetornoDos = "s";
                            }
                        }
                        break;
                    case 1: // Izquierda
                        movInval[1] = movInval[1] + 1;
                        if ((Laberinto.mapa[fila][columna-1] != Laberinto.m) && (Laberinto.mapa[fila][columna-1] != Laberinto.e) && (Laberinto.mapaBool[fila][columna-1] == true) && (Laberinto.mapa[fila][columna - 1] != Laberinto.a1)) {
                            if (Laberinto.mapa[fila][columna-1] == Laberinto.s) {
                                finDos = true;
                                corteDos = true;
                            }
                            direccionRetornoDos = "a";
                            actualizarIzquierda();
                            corteDireccion = false;                            
                        }else{
                            if ((movInval[0] > 0) && (movInval[1] > 0) && (movInval[2] > 0) && (movInval[3] > 0)) {
                                encerrado = true;
                                corteDireccion = false;
                                direccionRetornoDos = "a";
                            }                            
                        }
                        break;
                    case 2: // Arriba
                        movInval[2] = movInval[2] + 1;
                        if ((Laberinto.mapa[fila-1][columna] != Laberinto.m) && (Laberinto.mapa[fila-1][columna] != Laberinto.e) && (Laberinto.mapaBool[fila-1][columna] == true) && (Laberinto.mapa[fila-1][columna] != Laberinto.a1)) {
                            if (Laberinto.mapa[fila-1][columna] == Laberinto.s) {
                                finDos = true;
                                corteDos = true;
                            }
                            direccionRetornoDos = "w";
                            actualizarArriba();
                            corteDireccion = false;
                        }else{
                            if ((movInval[0] > 0) && (movInval[1] > 0) && (movInval[2] > 0) && (movInval[3] > 0)) {
                                encerrado = true;
                                corteDireccion = false;
                                direccionRetornoDos = "w";
                            }                            
                        }
                        break;
                    case 3: //Derecha
                        movInval[3] = movInval[3] + 1;
                        if ((Laberinto.mapa[fila][columna+1] != Laberinto.m) && (Laberinto.mapa[fila][columna+1] != Laberinto.e) && (Laberinto.mapaBool[fila][columna+1] == true) && (Laberinto.mapa[fila][columna+1] != Laberinto.a1)) {
                            if (Laberinto.mapa[fila][columna+1] == Laberinto.s) {
                                finDos = true;
                                corteDos = true;
                            }
                            direccionRetornoDos = "d";
                            actualizarDerecha();
                            corteDireccion = false;
                        }else{
                            if ((movInval[0] > 0) && (movInval[1] > 0) && (movInval[2] > 0) && (movInval[3] > 0)) {
                                encerrado = true;
                                corteDireccion = false;
                                direccionRetornoDos = "d";
                            }
                        }
                        break;          
                }
            }
            
            for (int i = 0; i < movInval.length; i++) {
                movInval[i] = 0;
            }
            
           // Retorna la letra que indica el movimiento realizado
           return direccionRetornoDos;
        }

        
        public void direccion(int dir){
            boolean corteDireccion = true;
            /*
            0: abajo
            1: izquierda
            2: arriba
            3: derecha
            */
            
            while(corteDireccion==true){
                switch(dir){
                    case 0:
                        if (!this.esMuro('s') && (!this.esEnemigo('s'))) {
                            if (this.esSalida('s') || this.esAgenteUno('s')){
                                finDos = true;
                                corteDos = true;
                                objeto2 = true;
                            }
                            actualizarAbajo();                                
                            corteDireccion = false;
                        }else if((recalcular2 == true) && (finDos == false) && (corteDos == false)){
                                // Aplicar modularidad urgente
                                filaBis = fila;
                                columnaBis = columna;
                                recorridoA1 = this.recalculando9();
                                fila = filaBis;
                                columna = columnaBis;
                                Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 5);
                                Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 2);
                                this.marcadorFinal(filaBis, columnaBis, recorridoA1, Laberinto.mapaG);
                                this.direccion(recorridoA1);
                                corteDireccion = false;
                                finDos = true;
                                System.out.println("soy abajo");
                        }else{
                            objeto2 = true;
                            corteDireccion = false;
                        }
                        break;
                    case 1: // Izquierda
                        if (!this.esMuro('a') && (!this.esEnemigo('a'))) {
                            if (this.esSalida('a') || (this.esAgenteUno('a'))){
                                finDos = true;
                                corteDos = true;
                                objeto2 = true;                                
                            }
                            actualizarIzquierda();
                            corteDireccion = false;                            
                        }else if ((recalcular2 == true) && (finDos == false) && (corteDos == false)) {
                                // Aplicar modularidad urgente
                                filaBis = fila;
                                columnaBis = columna;
                                recorridoA1 = this.recalculando9();
                                
                                fila = filaBis;
                                columna = columnaBis;
                                
                                Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 5);
                                Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 2);
                                
                                this.marcadorFinal(filaBis, columnaBis, recorridoA1, Laberinto.mapaG);
                                this.direccion(recorridoA1);
                                corteDireccion = false;
                                finDos = true; 
                                System.out.println("soy izquierda");
                        }
                        else{
                            objeto2 = true;
                            corteDireccion = false;
                        }
                        break;
                    case 2: // Arriba
                        if (!this.esMuro('w') && (!this.esEnemigo('w'))) {
                            if (this.esSalida('w') || this.esAgenteUno('w')){
                                finDos = true;
                                corteDos = true;
                                objeto2 = true;                                
                            }
                            actualizarArriba();
                            corteDireccion = false;
                        }else if ((recalcular2 == true) && (finDos == false) && (corteDos == false)) {
                               // Aplicar modularidad urgente
                               filaBis = fila;
                               columnaBis = columna;
                               recorridoA1 = this.recalculando9();
                               fila = filaBis;
                               columna = columnaBis;
                               Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 5);
                               Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 2);
                                
                               this.marcadorFinal(filaBis, columnaBis, recorridoA1, Laberinto.mapaG);
                               this.direccion(recorridoA1);
                               corteDireccion = false;
                               finDos = true; 
                               System.out.println("soy arriba");
                        }else{
                            objeto2 = true;
                            corteDireccion = false;
                        }
                        break;
                    case 3: //Derecha
                        if (!this.esMuro('d') && (!this.esEnemigo('d'))) {
                            if (this.esSalida('d') || (this.esAgenteUno('d'))){
                                finDos = true;
                                corteDos = true;
                                objeto2 = true;                                
                            }
                            actualizarDerecha();
                            corteDireccion = false;
                        }else if ((recalcular2 == true) && (finDos == false) && (corteDos == false)) {
                                // Aplicar modularidad urgente
                                filaBis = fila;
                                columnaBis = columna;
                                recorridoA1 = this.recalculando9();
                                fila = filaBis;
                                columna = columnaBis;
                                Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 5);
                                Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 2);
                                this.marcadorFinal(filaBis, columnaBis, recorridoA1, Laberinto.mapaG);
                                this.direccion(recorridoA1);
                                corteDireccion = false;
                                finDos = true;
                                System.out.println("derecha");
                        }else{
                            objeto2 = true;
                            corteDireccion = false;
                        }
                        break;          
                }
            }
        }        
        
        /*Selecciona una direccion previamente establecida.
            Este metodo se ejecutara cuando A2 haya finalizado su recorrido y A1 se encuentre con un obstaculo.
        */
        public void direccion(String recorridoFinal){
            
            for (int i = 0; i < recorridoFinal.length() - 1; i++) {
                switch(recorridoFinal.charAt(i)){
                    case 'w':
                        this.actualizarArriba();
                        this.miTimer(150);
                        break;
                    case 's':
                        this.actualizarAbajo();
                        this.miTimer(150);
                        break;
                    case 'a':
                        this.actualizarIzquierda();
                        this.miTimer(150);
                        break;
                    case 'd':
                        this.actualizarDerecha();
                        this.miTimer(150);
                        break;
                }
            }
        }        
        
        // Timer
        public void miTimer(int tiempo){
            try {
                Thread.sleep(tiempo);
            } catch (InterruptedException ex) {
                    Logger.getLogger(AgenteDos.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
        
        // Consulta si hay un muro
        public boolean esMuro(char movimiento){
            boolean muro = false;
            switch(movimiento){
                case 'w':
                    if (Laberinto.mapa[fila - 1][columna] == Laberinto.m) {
                        muro = true;
                    }
                    break;
                case 's':
                    if (Laberinto.mapa[fila + 1][columna] == Laberinto.m) {
                        muro = true;
                    }
                    break;
                case 'a':
                    if (Laberinto.mapa[fila][columna - 1] == Laberinto.m) {
                        muro = true;
                    }
                    break;
                case 'd':
                    if (Laberinto.mapa[fila][columna + 1] == Laberinto.m) {
                        muro = true;
                    }
                    break;
            }
            return muro;
        }
        
        // Consulta si hay un enemigo
        public boolean esEnemigo(char movimiento){
            boolean enemigo = false;
            switch(movimiento){
                case 'w':
                    if (Laberinto.mapa[fila - 1][columna] == Laberinto.e) {
                        enemigo = true;
                    }
                    break;
                case 's':
                    if (Laberinto.mapa[fila + 1][columna] == Laberinto.e) {
                        enemigo = true;
                    }
                    break;
                case 'a':
                    if (Laberinto.mapa[fila][columna - 1] == Laberinto.e) {
                        enemigo = true;
                    }
                    break;
                case 'd':
                    if (Laberinto.mapa[fila][columna + 1] == Laberinto.e) {
                        enemigo = true;
                    }
                    break;
            }
            
            return enemigo;
        }
        
        // Consulta si hay una salida
        public boolean esSalida(char movimiento){
            boolean salida = false;
            switch(movimiento){
                case 'w':
                    if (Laberinto.mapa[fila - 1][columna] == Laberinto.s) {
                        salida = true;
                    }
                    break;
                case 's':
                    if (Laberinto.mapa[fila + 1][columna] == Laberinto.s) {
                        salida = true;
                    }
                    break;
                case 'a':
                    if (Laberinto.mapa[fila][columna - 1] == Laberinto.s) {
                        salida = true;
                    }
                    break;
                case 'd':
                    if (Laberinto.mapa[fila][columna + 1] == Laberinto.s) {
                        salida = true;
                    }
                    break;
            }
            return salida;
        }
        
        // Consulta si es pasillo
        public boolean esPasillo(char movimiento){
            boolean pasillo = false;
            switch(movimiento){
                case 'w':
                    if (Laberinto.mapa[fila - 1][columna] == Laberinto.p) {
                        pasillo = true;
                    }
                    break;
                case 's':
                    if (Laberinto.mapa[fila + 1][columna] == Laberinto.p) {
                        pasillo = true;
                    }
                    break;
                case 'a':
                    if (Laberinto.mapa[fila][columna - 1] == Laberinto.p) {
                        pasillo = true;
                    }
                    break;
                case 'd':
                    if (Laberinto.mapa[fila][columna + 1] == Laberinto.p) {
                        pasillo = true;
                    }
                    break;
            }
            return pasillo;
        }
        
        // Consulta si es Agente dos
        public boolean esAgenteUno(char movimiento){
            boolean agenteUno = false;
            switch(movimiento){
                case 'w':
                    if (Laberinto.mapa[fila - 1][columna] == Laberinto.a1) {
                        agenteUno = true;
                    }
                    break;
                case 's':
                    if (Laberinto.mapa[fila + 1][columna] == Laberinto.a1) {
                        agenteUno = true;
                    }
                    break;
                case 'a':
                    if (Laberinto.mapa[fila][columna - 1] == Laberinto.a1) {
                        agenteUno = true;
                    }
                    break;
                case 'd':
                    if (Laberinto.mapa[fila][columna + 1] == Laberinto.a1) {
                        agenteUno = true;
                    }
                    break;
            }
            return agenteUno;        
        }
        
        // Calcula distancia desde posicion actual a posicion inicial de A2 y posicion de llegada.
        public boolean seguirRecorrido(){
            boolean continuarRecorridoDos = true;
            // posicion actual A2
            int [] posicionA2 = new int [2];
            posicionA2 = posicionActual(Laberinto.mapa, Laberinto.a2);
       
            // Calculo los movimiento que debe hacer para llegar a la posicion final del A1
            int filaFinalA1 = Math.abs(Laberinto.posicionFinalAgente1[0] - posicionA2[0]);
            int columnaFinalA1 = Math.abs(Laberinto.posicionFinalAgente1[1] - posicionA2[1]);
            // Calcula los movimiento que debe hace para llegar a la posicion inicial del A1
            int filaInicioA1 = Math.abs(Laberinto.posicionInicialAgente1[0] - posicionA2[0]);
            int columnaInicioA1 = Math.abs(Laberinto.posicionInicialAgente1[1] - posicionA2[1]);
            
            // Si el camino a la posicion inicial de A1 es menor al camino final de A1 sigue la trayectoria,
            // sino cambia de camino y se dirige hacia su posicion final
            if ((filaInicioA1 + columnaInicioA1) < (filaFinalA1 + columnaFinalA1)) {
                continuarRecorridoDos = true;
            }else{
                continuarRecorridoDos = false;
            }
 
            return continuarRecorridoDos;
        }        
        
        // Funcion que busca un camino alternativo hacia el objetivo, lo lleva a cabo aplicando un algoritmo de busqueda.
        public String recalculando9() {
            int valorVariable, elemento = 0;
            int[] valorMovimiento = new int[4];
            boolean[] boolMovimiento = new boolean[4];

            String caminoFinal = "";
            while ((Laberinto.mapa[fila][columna] > -1) && (Laberinto.mapa[fila][columna] != 870) && (Laberinto.mapa[fila][columna] != 910) && (corteDos == false)) {
                Laberinto.mapaBool[fila][columna] = false;

                for (int i = 0; i < valorMovimiento.length; i++) {
                    valorMovimiento[i] = 0;
                    boolMovimiento[i] = true;
                }

                if ((Laberinto.mapa[fila - 1][columna] == Laberinto.a1) || (Laberinto.mapa[fila - 1][columna] == Laberinto.s)) {
                    matrizSolucion[fila - 1][columna] = 5;
                    Laberinto.mapa[fila - 1][columna] = Laberinto.a2;
                    Laberinto.mapaG[columna][fila - 1].setIcon(Laberinto.analizar);
                    valorMovimiento[2] = 5;
                    caminoFinal = caminoFinal + 'w';
                    caminoSucio1 = caminoSucio1 + 'w';
                    corteDos = true;
                } else if (Laberinto.mapa[fila - 1][columna] > -1) {
                    if (Laberinto.mapaBool[fila - 1][columna] == true) {
                        if ((x1 - fila) < 0) {
                            matrizSolucion[fila - 1][columna] = 5;
                            Laberinto.mapa[fila - 1][columna] = Laberinto.a2;
                            Laberinto.mapaG[columna][fila - 1].setIcon(Laberinto.analizar);
                            valorMovimiento[2] = 5;
                            caminoSucio1 = caminoSucio1 + 'w';

                        } else {
                            matrizSolucion[fila - 1][columna] = 2;
                            Laberinto.mapaG[columna][fila - 1].setIcon(Laberinto.analizarAlternativa);
                            Laberinto.mapa[fila - 1][columna] = Laberinto.a2;
                            valorMovimiento[2] = 2;
                            caminoSucio1 = caminoSucio1 + 'w';
                        }
                        this.miTimer(100);
                    } else {
                        boolMovimiento[2] = false;
                    }
                }

                if ((Laberinto.mapa[fila + 1][columna] == Laberinto.a1) || (Laberinto.mapa[fila + 1][columna] == Laberinto.s)) {
                    matrizSolucion[fila + 1][columna] = 5;
                    Laberinto.mapa[fila + 1][columna] = Laberinto.a2;
                    Laberinto.mapaG[columna][fila + 1].setIcon(Laberinto.analizar);
                    valorMovimiento[0] = 5;
                    caminoFinal = caminoFinal + 's';
                    caminoSucio1 = caminoSucio1 + 's';
                    corteDos = true;
                } else if (Laberinto.mapa[fila + 1][columna] > -1) {
                    if (Laberinto.mapaBool[fila + 1][columna] == true) {
                        if ((x1 - fila) >= 0) {
                            matrizSolucion[fila + 1][columna] = 5;
                            Laberinto.mapa[fila + 1][columna] = Laberinto.a2;
                            Laberinto.mapaG[columna][fila + 1].setIcon(Laberinto.analizar);
                            valorMovimiento[0] = 5;
                            caminoSucio1 = caminoSucio1 + 's';

                        } else {
                            matrizSolucion[fila + 1][columna] = 2;
                            Laberinto.mapaG[columna][fila + 1].setIcon(Laberinto.analizarAlternativa);
                            Laberinto.mapa[fila + 1][columna] = Laberinto.a2;
                            valorMovimiento[0] = 2;
                            caminoSucio1 = caminoSucio1 + 's';
                        }
                        this.miTimer(100);
                    } else {
                        boolMovimiento[0] = false;
                    }
                }
                if ((Laberinto.mapa[fila][columna - 1] == Laberinto.a1) || (Laberinto.mapa[fila][columna - 1] == Laberinto.s)) {
                    matrizSolucion[fila][columna - 1] = 5;
                    Laberinto.mapa[fila][columna - 1] = Laberinto.a2;
                    Laberinto.mapaG[columna - 1][fila].setIcon(Laberinto.analizar);
                    valorMovimiento[1] = 5;
                    caminoFinal = caminoFinal + 'a';
                    caminoSucio1 = caminoSucio1 + 'a';
                    corteDos = true;
                } else if ((Laberinto.mapa[fila][columna - 1] > - 1)) {
                    if (Laberinto.mapaBool[fila][columna - 1] == true) {
                        if ((y1 - columna) < 0) {
                            matrizSolucion[fila][columna - 1] = 5;
                            Laberinto.mapa[fila][columna - 1] = Laberinto.a2;
                            Laberinto.mapaG[columna - 1][fila].setIcon(Laberinto.analizar);
                            valorMovimiento[1] = 5;
                            caminoSucio1 = caminoSucio1 + 'a';
                        } else {
                            matrizSolucion[fila][columna - 1] = 2;
                            Laberinto.mapaG[columna - 1][fila].setIcon(Laberinto.analizarAlternativa);
                            Laberinto.mapa[fila][columna - 1] = Laberinto.a2;
                            valorMovimiento[1] = 2;
                            caminoSucio1 = caminoSucio1 + 'a';
                        }
                        this.miTimer(100);
                    } else {
                        boolMovimiento[1] = false;
                    }
                }
                if ((Laberinto.mapa[fila][columna + 1] == Laberinto.a1) || (Laberinto.mapa[fila][columna + 1] == Laberinto.s)) {
                    matrizSolucion[fila][columna + 1] = 5;
                    Laberinto.mapa[fila][columna + 1] = Laberinto.a2;
                    Laberinto.mapaG[columna + 1][fila].setIcon(Laberinto.analizar);
                    valorMovimiento[3] = 5;
                    caminoFinal = caminoFinal + 'd';
                    caminoSucio1 = caminoSucio1 + 'd';
                    corteDos = true;
                } else if (Laberinto.mapa[fila][columna + 1] > -1) {
                    if (Laberinto.mapaBool[fila][columna + 1] == true) {
                        if ((y1 - columna) >= 0) {
                            matrizSolucion[fila][columna + 1] = 5;
                            Laberinto.mapa[fila][columna + 1] = Laberinto.a2;
                            Laberinto.mapaG[columna + 1][fila].setIcon(Laberinto.analizar);
                            valorMovimiento[3] = 5;
                            caminoSucio1 = caminoSucio1 + 'd';
                        } else {
                            matrizSolucion[fila][columna + 1] = 2;
                            Laberinto.mapaG[columna + 1][fila].setIcon(Laberinto.analizarAlternativa);
                            Laberinto.mapa[fila][columna + 1] = Laberinto.a2;
                            valorMovimiento[3] = 2;
                            caminoSucio1 = caminoSucio1 + 'd';
                        }
                        this.miTimer(100);
                    } else {
                        boolMovimiento[3] = false;
                    }

                }

                valorVariable = valorMovimiento[0];

                for (int i = 1; i < valorMovimiento.length; i++) {
                    if (valorMovimiento[i] > valorVariable) {
                        elemento = i;
                        valorVariable = valorMovimiento[elemento];
                    }
                }

                if (((boolMovimiento[0] == false) || Laberinto.mapa[fila + 1][columna] < 0) && ((boolMovimiento[1] == false) || Laberinto.mapa[fila][columna - 1] < 0) && ((boolMovimiento[2] == false) || Laberinto.mapa[fila - 1][columna] < 0) && ((boolMovimiento[3] == false) || Laberinto.mapa[fila][columna + 1] < 0)) {
                    int p = caminoFinal.length() - 1;

                    switch (caminoFinal.charAt(p)) {
                        case 'w':
                            fila = fila + 1;
                            caminoFinal = caminoFinal.substring(0, p);
                            break;
                        case 's':
                            fila = fila - 1;
                            caminoFinal = caminoFinal.substring(0, p);
                            break;
                        case 'a':
                            columna = columna + 1;
                            caminoFinal = caminoFinal.substring(0, p);
                            break;
                        case 'd':
                            columna = columna - 1;
                            caminoFinal = caminoFinal.substring(0, p);
                            break;
                    }
                } else {
                    switch (elemento) {
                        case 0:
                            fila = fila + 1;
                            caminoFinal = caminoFinal + 's';
                            Laberinto.mapaBool[fila][columna] = false;
                            break;
                        case 1:
                            columna = columna - 1;
                            caminoFinal = caminoFinal + 'a';
                            Laberinto.mapaBool[fila][columna] = false;
                            break;
                        case 2:
                            fila = fila - 1;
                            caminoFinal = caminoFinal + 'w';
                            Laberinto.mapaBool[fila][columna] = false;
                            break;
                        case 3:
                            columna = columna + 1;
                            caminoFinal = caminoFinal + 'd';
                            Laberinto.mapaBool[fila][columna] = false;
                            break;
                    }
                }
                elemento = 0;
                valorVariable = 0;

            }

            if ((Laberinto.mapa[fila][columna] == Laberinto.a1) || (Laberinto.mapa[fila][columna] == 910)) {
                Laberinto.mapaG[columna][fila].setIcon(Laberinto.analizar);
            }

            return caminoFinal;
        }
        
        // Marca el camino final que realizara el AgenteDos en caso de que, previamente, haya colisionado con algun obstaculo
        public void marcadorFinal(int filaFinal, int columnaFinal, String recorrido, JLabel [][] mapaL){
            String recorridoFinal = recorrido.substring(0, recorrido.length());
            int iterador = 0;
            int ff = filaFinal;
            int cf = columnaFinal;
            boolean pasillo = true;
            
            // Ejecutara el bucle cinco veces
            while(iterador < 5){
                filaFinal = ff;
                columnaFinal = cf;
                pasillo = !pasillo;
                for (int i = 0; i < recorrido.length()- 1; i++) {
                    switch(recorrido.charAt(i)){
                        case 'w':
                            filaFinal = filaFinal - 1;
                            /* Si la variable pasillo es True pintara, en la grafica, un pasillo. Por otro lado, cuando sea
                                false remarcara cada casilla con color amarillo.
                            */
                            if(pasillo){
                                mapaL[columnaFinal][filaFinal].setIcon(Laberinto.pasillo);
                               
                            }else{
                                mapaL[columnaFinal][filaFinal].setIcon(Laberinto.analizar);
                                this.miTimer(20);
                            }
                            break;
                        case 's':
                            filaFinal = filaFinal + 1;
                            if(pasillo){
                                mapaL[columnaFinal][filaFinal].setIcon(Laberinto.pasillo);
                                
                            }else{
                                mapaL[columnaFinal][filaFinal].setIcon(Laberinto.analizar);
                                this.miTimer(20);
                            }
                            break;
                        case 'a':
                            columnaFinal = columnaFinal - 1;
                            if(pasillo){
                                mapaL[columnaFinal][filaFinal].setIcon(Laberinto.pasillo);
                              
                            }else{
                                mapaL[columnaFinal][filaFinal].setIcon(Laberinto.analizar);
                                this.miTimer(20);
                            }
                            break;
                        case 'd':
                            columnaFinal = columnaFinal + 1;
                            if(pasillo){
                                mapaL[columnaFinal][filaFinal].setIcon(Laberinto.pasillo);
                              
                            }else{
                                mapaL[columnaFinal][filaFinal].setIcon(Laberinto.analizar);
                                this.miTimer(20);
                            }
                            break;
                    }                

                }
                iterador = iterador + 1;
            }

        }        
        
        // Realiza calculos para determinar si debe seguir por su camino actual o debe cambiar de direccion.
        // * Cambiar de direcion significa dirigirse hacia donde comenzo el AgenteUno, asi despues siguir su camino.
        public void continuarCaminoRecibido(){
                objeto2 = false;
                // Fila y columna de la posicion final de A1
                x1 = Laberinto.posicionFinalAgente1[0];
                y1 = Laberinto.posicionFinalAgente1[1];
               
                // Guarda la posicion actual de A2
                int [] posActual2 = posicionActual(Laberinto.mapa, Laberinto.a2);
                int filaAlternativa2 = posActual2[0];
                int columnaAlternativa2 = posActual2[1];
                
                // A2 se mueve en filas
                while((x1 != fila) && (objeto2 == false)){
                       recalcular2 = true;
                       this.miTimer(250);
                       if ((x1-fila)<0) {
                            this.direccion(2);
                       }else if((x1 - fila) > 0){                      
                            this.direccion(0);
                       }
                }                
                        
                // A2 se mueve en columnas
                int iteradorFinalY2 = 0;
                while((y1 != columna) && (objeto2 == false)){
                       this.miTimer(250);
                       if ((y1-columna)<0) {
                            this.direccion(1);
                       }else if((y1 - columna) > 0){
                                this.direccion(3);
                       }           
                }        
        }
        
        // Limpia la matriz booleana
        public void limpiarMatrizBoleana(boolean [][] matrizBool){
            for (int i = 1; i < matrizBool.length - 1; i++) {
                for (int j = 1; j < matrizBool.length - 1; j++) {
                    matrizBool[i][j] = true;
               }
            }
        }
        
        // Realiza movimiento hacia abajo y actualiza todas las variables correspondientes
        public void actualizarAbajo(){
            // Actualiza la variable global fila
            fila = fila + 1;
            // Acccede a la matriz mapa y actualiza la posicion del agente.
            Laberinto.mapa[fila][columna] = Laberinto.a2;
            Laberinto.mapa[fila - 1][columna] = 0;
            //Modifica matriz booleana de recorridos
            Laberinto.mapaBool[fila][columna] = true;
            Laberinto.mapaBool[fila - 1][columna] = false;
            //Pinta movimiento en el frame de A2
            Laberinto.btnMtz2[columna][fila].setBackground(Color.blue);
            Laberinto.lblAbajo2.setIcon(Laberinto.imgAbajo);
            // Pinta moviento de Laberinto principal
            Laberinto.mapaG[columna][fila - 1].setIcon(Laberinto.pasillo2);
            Laberinto.mapaG[columna][fila].setIcon(Laberinto.agenteDos);
        }
        
        // Realiza movimiento hacia izquierda y actualiza todas las variables correspondientes        
        public void actualizarIzquierda(){
            columna = columna - 1;
            Laberinto.mapa[fila][columna] = Laberinto.a2;
            Laberinto.mapa[fila][columna + 1] = 0;
            //Modifica matriz de recorridos
            Laberinto.mapaBool[fila][columna] = true;
            Laberinto.mapaBool[fila][columna + 1] = false;
            //Pinta movimiento en el frame de A2
            Laberinto.btnMtz2[columna][fila].setBackground(Color.blue);
            Laberinto.lblIzquierda2.setIcon(Laberinto.imgIzquierda);
            // Pinta moviiento de Laberinto principal
            Laberinto.mapaG[columna + 1][fila].setIcon(Laberinto.pasillo2);
            Laberinto.mapaG[columna][fila].setIcon(Laberinto.agenteDos);
        }
        
        // Realiza movimiento hacia arriba y actualiza todas las variables correspondientes        
        public void actualizarArriba(){
            fila = fila - 1;
            Laberinto.mapa[fila][columna] = Laberinto.a2;
            Laberinto.mapa[fila + 1][columna] = 0;
            //Modifica matriz de recorridos
            Laberinto.mapaBool[fila][columna] = true;
            Laberinto.mapaBool[fila + 1][columna] = false;
            //Pinta movimiento en el laberinto de A2
            Laberinto.btnMtz2[columna][fila].setBackground(Color.blue);
            Laberinto.lblArriba2.setIcon(Laberinto.imgArriba);
            // Pinta moviento de Laberinto principal
            Laberinto.mapaG[columna][fila + 1].setIcon(Laberinto.pasillo2);
            Laberinto.mapaG[columna][fila].setIcon(Laberinto.agenteDos); 
        }
        
        // Realiza movimiento hacia derecha y actualiza todas las variables correspondientes        
        public void actualizarDerecha(){
            columna = columna + 1;
            Laberinto.mapa[fila ][columna] = Laberinto.a2;
            Laberinto.mapa[fila][columna - 1] = 0;
            //Modifica matriz de recorridos
            Laberinto.mapaBool[fila][columna] = true;
            Laberinto.mapaBool[fila][columna - 1] = false;
            //Pinta movimiento en el frame de A2
            Laberinto.btnMtz2[columna][fila].setBackground(Color.blue);
            Laberinto.lblDerecha2.setIcon(Laberinto.imgDerecha);
            // Pinta moviiento de Laberinto principal
            Laberinto.mapaG[columna - 1][fila].setIcon(Laberinto.pasillo2);
            Laberinto.mapaG[columna][fila].setIcon(Laberinto.agenteDos);
        }
                
        // Funcion que si devuelve 'true' indicara la finalizacion del comportamiento "MovimientoDos".
        @Override
        public boolean done() {     
            return corteDos;  
        }
    
    
    }
    // Obtenie la posicion actual de un agente en un laberinto
    public int [] posicionActual(int [][] laberinto, int agente){
        int par []  = new int [2];   
        for (int i = 0; i < laberinto.length; i++) {
            for (int j = 0; j < laberinto.length; j++) {
                if (laberinto[i][j] == agente) {
                   par[0]= i;
                   par[1]= j;
                }   
            }
        }
        return par;
    }
    
}

    