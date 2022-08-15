
package laberinto;
    
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.awt.Color;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class AgenteUno extends Agent{
    
    char movRanx;
    String movAgente;
    String recorridoA2 = new String();
    String recorridoRecalculandoA2 = new String();
    String caminoSucio2 = new String("");
    
    boolean corteUno, objeto = false;
    boolean cambioPos = true;
    boolean recorridoAgente2 = false;
    boolean recalcular = false;
    boolean incializadorDeGraficoUno = false;
    boolean cercaAgenteAliado = false;
    boolean caminoExtra = true;
    
    int iteradorX = 0;
    int iteradorY = 0;
    int inicioA2x = 0;
    int inicioA2y = 0;
    
    int x2 = 0;
    int y2 = 0;
    
    int obstaculo = 0;
    String recorridoAlternativo = "";
    
    int filaActualRecalculando = 0;
    int columnaActualRecalculando = 0;
    int filaObjetivoRecalculando = 0;
    int columnaObjetivoRecalculando = 0;
    
    boolean movimientoArribaCancelado = false;
    boolean movimientoAbajoCancelado = false;
    boolean movimientoIzquierdaCancelado = false;
    boolean movimientoDerechaCancelado = false;
    
    // Almacena el recorrido alternativo asi poder recorrerlo de forma inversa si es necesario
    private String camino = "";
    
    private int filaBis, columnaBis = 0;
    private int fila, columna = 0;
    private int filaMatrizSolucion, columnaMatrizSolucion = 0;
    // movimiento hacia derecha                                                                                                            
    private boolean derecha = false;
    //posicion incial de A1
    int [] pos;
    
    // Matriz que alojara los valores de la solucion
    int [][] matrizSolucion;
    
    int [] posicion2;
    
    // indica la finalizacion del juego
    private boolean finUno = false;
    
    // Inicia el agente
    protected void setup(){
        addBehaviour(new MovimientoUno());
    }
      
    // Finaliza el agente
    protected void takeDown(){
        System.out.println("El Agente Uno finalizó su ejecución");     
    }
    
    private class MovimientoUno extends Behaviour{
        
        
    
    
        private MessageTemplate mt1 = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        
       @Override
        public void action() {
            
            this.miTimer(250);

            ACLMessage msgResp1 = myAgent.receive(mt1);
            if((msgResp1 != null) && (msgResp1.getOntology().equals("movimiento"))){
                   ACLMessage movResp = msgResp1.createReply();
                   String m = msgResp1.getContent();
                   if ((m.equalsIgnoreCase("a")) || (m.equalsIgnoreCase("s")) || (m.equalsIgnoreCase("w")) || (m.equalsIgnoreCase("d"))) {
                        recorridoA2 = recorridoA2 + m;
                        movResp.setPerformative(ACLMessage.INFORM);
                        movResp.setOntology("movimiento");
                        
                        if (incializadorDeGraficoUno == false) {
                            Laberinto.posicionInicialAgente1 = posicionActual(Laberinto.mapa, Laberinto.a1);
                            pos = posicionActual(Laberinto.mapa, Laberinto.a1);
                            fila = pos[0];
                            columna = pos[1];
                            Laberinto.btnMtz1[columna][fila].setBackground(Color.green);
                            incializadorDeGraficoUno = true;
                        }
                       
                            // solo mueve a izquierda y derecha
                            switch(movimiento()){
                                case 'a':
                                    if (finUno == true) {
                                        corteUno = true;
                                        movResp.setOntology("recorrido");
                                        movAgente = "a";
                                    }else{
                                        movResp.setOntology("movimiento");
                                        movAgente = "a";
                                    }
                                    movResp.setContent(movAgente);
                                    break;
                                case 'd':
                                    if (finUno == true) {
                                        corteUno = true;
                                        movResp.setOntology("recorrido");
                                        movAgente = "d";
                                    }else{
                                        movResp.setOntology("movimiento");
                                        movAgente = "d";
                                    }
                                    movResp.setContent(movAgente);
                                    break;
                            }
                      

                        try {
                              Thread.sleep(250);
                        } catch (InterruptedException ex) {
                              Logger.getLogger(AgenteDos.class.getName()).log(Level.SEVERE, null, ex);
                        }
                
                        Laberinto.lblArriba1.setIcon(Laberinto.imgNulo);
                        Laberinto.lblAbajo1.setIcon(Laberinto.imgNulo);
                        Laberinto.lblIzquierda1.setIcon(Laberinto.imgNulo);
                        Laberinto.lblDerecha1.setIcon(Laberinto.imgNulo);  
                        myAgent.send(movResp);
                    }
                   
            }else if((msgResp1 != null) && (msgResp1.getOntology().equals("recorrido"))){
                    
                    if (incializadorDeGraficoUno == false) {
                        pos = posicionActual(Laberinto.mapa, Laberinto.a1);
                        fila = pos[0];
                        columna = pos[1];
                        Laberinto.btnMtz1[columna][fila].setBackground(Color.green);
                        incializadorDeGraficoUno = true;
                    }
                    for (int i = 0; i < Laberinto.mapaBool.length; i++) {
                        for (int j = 0; j < Laberinto.mapaBool.length; j++) {
                            Laberinto.mapaBool[i][j] = true;
                        }
                    }
                    // crea matriz de solucion con la dimension del tablero
                    matrizSolucion = new int [Laberinto.tamAncho][Laberinto.tamAlto];
                    matrizSolucion[fila][columna] = Laberinto.a2;
                    
                    // Almacena todos los movimientos ejecutados por el Agente2
                    String recorrido = msgResp1.getContent();
                    recorridoA2 = recorridoA2 + recorrido; 
                    
                    while(corteUno == false){
                        boolean continuar1 = this.seguirRecorrido();
                        this.continuarCaminoRecibido(continuar1);
                    }
                    corteUno = true;
                    
            }else if((msgResp1 != null) && (msgResp1.getOntology().equals("encerrado"))){
                
                   
                    if (incializadorDeGraficoUno == false) {
                        Laberinto.posicionInicialAgente1 = posicionActual(Laberinto.mapa, Laberinto.a1);
                        pos = posicionActual(Laberinto.mapa, Laberinto.a1);
                        fila = pos[0];
                        columna = pos[1];
                        Laberinto.btnMtz1[columna][fila].setBackground(Color.green);
                        incializadorDeGraficoUno = true;
                    }
                    
                   ACLMessage movResp = msgResp1.createReply();
                   movResp.setPerformative(ACLMessage.INFORM);
 
                   this.miTimer(250);                
                   
                    switch(this.direccion()){
                        case "w":
                            if (corteUno == true) {
                                movResp.setOntology("recorrido");
                                Laberinto.posicionFinalAgente1 = posicionActual(Laberinto.mapa, Laberinto.a1);
                            }else{
                                movResp.setOntology("camino");
                            }
                            movAgente = "w";
                            movResp.setContent(movAgente);
                            System.out.println(movResp.getContent());
                            break;
                            
                        case "s":
                            if (corteUno == true) {
                                movResp.setOntology("recorrido");
                                Laberinto.posicionFinalAgente1 = posicionActual(Laberinto.mapa, Laberinto.a1);
                            }else{
                                movResp.setOntology("camino");
                            }
                            movAgente = "s";
                            movResp.setContent(movAgente);
                            System.out.println(movResp.getContent());                            
                            break;
                            
                        case "a":
                            if (corteUno == true) {
                                movResp.setOntology("recorrido");
                                Laberinto.posicionFinalAgente1 = posicionActual(Laberinto.mapa, Laberinto.a1);      
                            }else{
                                movResp.setOntology("camino");
                            }
                            movAgente = "a";
                            movResp.setContent(movAgente);    
                            System.out.println(movResp.getContent());                            
                            break;
                            
                        case "d":
                            if (corteUno == true) {
                                movResp.setOntology("recorrido");
                                Laberinto.posicionFinalAgente1 = posicionActual(Laberinto.mapa, Laberinto.a1);  
                            }else{
                                movResp.setOntology("camino");
                            }                            
                            movAgente = "d";
                            movResp.setContent(movAgente);   
                            System.out.println(movResp.getContent());                            
                            break;
                    }
                    
                    this.miTimer(250);
                    System.out.println(movResp.getOntology());
                    myAgent.send(movResp); 
            }            
               
        }
 
        // Ejecuta movimiento a izquierda o derecha.
        public char movimiento(){
            /*
                ACA ESTA EL PROBLEMA
            */
                movRanx= 'd';
                boolean bandera = true;
                if ((Laberinto.mapa[fila][columna + 1] != Laberinto.m) && (Laberinto.mapa[fila][columna + 1] != Laberinto.e) && (Laberinto.mapa[fila][columna + 1] != Laberinto.a1)&& (Laberinto.mapa[fila][columna + 1] != Laberinto.a2) && (derecha == false)) {
                    movRanx = 'd';
                    derecha = true;
                    actualizarDerecha();
                    bandera = false;
                }else{
                    movRanx = 'a';
                    derecha = false;
                    actualizarIzquierda();
                    bandera = false;
                } 
                return movRanx;
            }
        
        /* Selecciona una direccion de manera aleatoria.
            Este motodo se ejecutara mientras A2 permanece encerrado
        */
        public String direccion(){
            boolean corteDireccion = true;
            String direccionRetornoUno = "";
            /*
            0: abajo
            1: izquierda
            2: arriba
            3: derecha
            */
            
            while(corteDireccion==true){
                int dir = (int) Math.floor(Math.random()*4); 
                switch(dir){
                    case 0: //Abajo
                        if ((Laberinto.mapa[fila+1][columna] != Laberinto.m) && (Laberinto.mapa[fila+1][columna] != Laberinto.e) && (Laberinto.mapa[fila+1][columna] != Laberinto.a2)) {
                            if ((Laberinto.mapa[fila+1][columna] == Laberinto.s)){
                                finUno = true;
                                corteUno = true;
                                objeto = true;
                            }
                            direccionRetornoUno = "s";
                            this.actualizarAbajo();                                
                            corteDireccion = false;
                        }
                        break;
                    case 1: // Izquierda
                        if ((Laberinto.mapa[fila][columna-1] != Laberinto.m) && (Laberinto.mapa[fila][columna-1] != Laberinto.e) && (Laberinto.mapa[fila][columna - 1] != Laberinto.a2)) {
                            if ((Laberinto.mapa[fila][columna-1] == Laberinto.s)){
                                finUno = true;
                                corteUno = true;
                                objeto = true;
                            }
                            direccionRetornoUno = "a";
                            this.actualizarIzquierda();
                            corteDireccion = false;                           
                        }
                        break;
                    case 2: // Arriba
                        if ((Laberinto.mapa[fila-1][columna] != Laberinto.m) && (Laberinto.mapa[fila-1][columna] != Laberinto.e) && (Laberinto.mapa[fila-1][columna] != Laberinto.a2)) {
                            if ((Laberinto.mapa[fila-1][columna] == Laberinto.s)){
                                finUno = true;
                                corteUno = true;
                                objeto = true;                                
                            }
                            direccionRetornoUno = "w";
                            this.actualizarArriba();
                            corteDireccion = false;
                        }
                        break;
                    case 3: //Derecha
                        if ((Laberinto.mapa[fila][columna+1] != Laberinto.m) && (Laberinto.mapa[fila][columna+1] != Laberinto.e) && (Laberinto.mapa[fila][columna+1] != Laberinto.a2)) {
                            if ((Laberinto.mapa[fila][columna+1] == Laberinto.s)){
                                finUno = true;
                                corteUno = true;
                                objeto = true;
                            }
                            direccionRetornoUno = "d";
                            this.actualizarDerecha();
                            corteDireccion = false;
                        }
                        break;          
                }
            }
            return direccionRetornoUno;
        }
        
        /* Selecciona una direccion previamente establecida.
            Este metodo se ejecutara, solamante, cuando A2 haya finalizado el recorrido.
        */
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
                        if (!this.esMuro('s') && !this.esEnemigo('s')) {
                            if (this.esSalida('s') || (this.esAgenteDos('s'))){
                                finUno = true;
                                corteUno = true;
                                objeto = true;
                            }
                            actualizarAbajo();                                
                            corteDireccion = false;
                        }else if((recalcular == true) && (finUno == false) && (corteUno == false)){
                              
                                filaBis = fila;
                                columnaBis = columna;
                                recorridoRecalculandoA2 = this.recalculando9();
                                System.out.println("sali de reclutando, soy S");
                                fila = filaBis;
                                columna = columnaBis;
                                Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 5);
                                Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 2);
                                this.marcadorFinal(filaBis, columnaBis, recorridoRecalculandoA2, Laberinto.mapaG);
                                this.direccion(recorridoRecalculandoA2);
                                System.out.println(recorridoRecalculandoA2.toString());
                                // tengo que seguir el camino recibido por el agente A2
                                // Si me queda mas cerca donde empezo el A2
//                                if (caminoExtra == true) {
//                                    this.direccion(recorridoA2);
//                                    System.out.println(recorridoA2.toString());
//                                }
                                objeto = true;
                                corteDireccion = false;
                        }else{
                            objeto = true;
                            corteDireccion = false;
                        }
                        break;
                    case 1: // Izquierda
                        if (!this.esMuro('a') && (!this.esEnemigo('a'))) {
                            if (this.esSalida('a') || (this.esAgenteDos('a'))){
                                finUno = true;
                                corteUno = true;
                                objeto = true;                                   
                            }
                            actualizarIzquierda();
                            corteDireccion = false;                            
                        }else if ((recalcular == true) && (finUno == false) && (corteUno == false)) {
//                                iteradorY = iteradorY + 1;
                                filaBis = fila;
                                columnaBis = columna;
                                recorridoRecalculandoA2 = this.recalculando9();
                                System.out.println("sali de reclutando, soy A");
                                fila = filaBis;
                                columna = columnaBis;
                                Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 5);
                                Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 2);
                                this.marcadorFinal(filaBis, columnaBis, recorridoRecalculandoA2, Laberinto.mapaG);
                                this.direccion(recorridoRecalculandoA2);
                                             System.out.println(recorridoRecalculandoA2.toString());                   
                                // tengo que seguir el camino recibido por el agente A2
                                // Si me queda mas cerca donde empezo el A2
//                                if (caminoExtra == true) {
//                                    this.direccion(recorridoA2);
//                                    System.out.println(recorridoA2.toString());
//                                }    
                                objeto =true;
                                corteDireccion = false;
                        }
                        else{
                            objeto = true;
                            corteDireccion = false;
                        }
                        break;
                    case 2: // Arriba
                        if (!this.esMuro('w') && (!this.esEnemigo('w'))) {
                            if (this.esSalida('w') || this.esAgenteDos('w')){
                                finUno = true;
                                corteUno = true;
                                objeto = true;                                         
                            }
                            actualizarArriba();
                            corteDireccion = false;
                        }else if ((recalcular == true) && (finUno == false) && (corteUno == false)) {
//                                iteradorX = iteradorX + 1;
                                filaBis = fila;
                                columnaBis = columna;
                                recorridoRecalculandoA2 = this.recalculando9();
                                System.out.println("sali de reclutando, soy W");
                                fila = filaBis;
                                columna = columnaBis;
                                Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 5);
                                Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 2);
                                this.marcadorFinal(filaBis, columnaBis, recorridoRecalculandoA2, Laberinto.mapaG);
                                this.direccion(recorridoRecalculandoA2);
                                     System.out.println(recorridoRecalculandoA2.toString());                           
                                // tengo que seguir el camino recibido por el agente A2
                                // Si me queda mas cerca donde empezo el A2
//                                if (caminoExtra==true) {
//                                    this.direccion(recorridoA2);
//                                    System.out.println(recorridoA2.toString());
//                                }
                                objeto = true;
                                corteDireccion = false;
                        }else{
                            objeto = true;
                            corteDireccion = false;
                        }
                        break;
                    case 3: //Derecha
                        if (!this.esMuro('d') && (!this.esEnemigo('d'))) {
                            if (this.esSalida('d') || (this.esAgenteDos('d'))){
                                finUno = true;
                                corteUno = true;
                                objeto = true;                                       
                            }
                            actualizarDerecha();
                            corteDireccion = false;
                        }else if ((recalcular == true) && (finUno == false) && (corteUno == false)) {
//                                iteradorY = iteradorY + 1;
                                filaBis = fila;
                                columnaBis = columna;
                                recorridoRecalculandoA2 = this.recalculando9();
                                System.out.println("sali de reclutando, soy D");
                                fila = filaBis;
                                columna = columnaBis;
                                Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 5);
                                Laberinto.limpiarMapa(Laberinto.mapaG, matrizSolucion, 2);
                                this.marcadorFinal(filaBis, columnaBis, recorridoRecalculandoA2, Laberinto.mapaG);
                                this.direccion(recorridoRecalculandoA2);
                                               System.out.println(recorridoRecalculandoA2.toString());                 
                                // tengo que seguir el camino recibido por el agente A2
                                // Si me queda mas cerca donde empezo el A2
//                                if (caminoExtra == true) {
//                                    this.direccion(recorridoA2);
//                                    System.out.println(recorridoA2.toString());
//                                }
                                objeto = true;
                                corteDireccion = false;
                        }else{
                            objeto = true;
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
        
        // Marcar camino final
        public void marcadorFinal(int filaFinal, int columnaFinal, String recorrido, JLabel [][] mapaL){
            String recorridoFinal = recorrido.substring(1, recorrido.length());
            int iterador = 0;
            int ff = filaFinal;
            int cf = columnaFinal;
            boolean pasillo = true;
            
            while(iterador < 5){
                filaFinal = ff;
                columnaFinal = cf;
                pasillo = !pasillo;
                for (int i = 0; i < recorrido.length()- 1; i++) {
                    switch(recorrido.charAt(i)){
                        case 'w':
                            filaFinal = filaFinal - 1;
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
        
        // Limpia el laberinto de los caminos alternativos
        public void LimpiarLaberinto(String caminoSucio, int filaSucia, int columnaSucia){
            
            for (int i = 0; i < caminoSucio.length() - 1; i++) {
                System.out.println("camino sucio: " + caminoSucio.toString());
                switch(caminoSucio.charAt(i)){
                    case 'w':
                        filaSucia = filaSucia - 1;
                        Laberinto.mapaG[columnaSucia][filaSucia].setIcon(Laberinto.pasillo);
                        break;
                    case 's':
                        filaSucia = filaSucia + 1;
                        Laberinto.mapaG[columnaSucia][filaSucia].setIcon(Laberinto.pasillo);
                        break;
                    case 'a':
                        columnaSucia = columnaSucia - 1;
                        Laberinto.mapaG[columnaSucia][filaSucia].setIcon(Laberinto.pasillo);
                        break;
                    case 'd':
                        columnaSucia = columnaSucia + 1;
                        Laberinto.mapaG[columnaSucia][filaSucia].setIcon(Laberinto.pasillo);
                        break;
                }
            }
        }
        
        // Calcula distancia desde posicion actual a posicion inicial de A1 y posicion de llegada.
        public boolean seguirRecorrido(){            
            boolean continuarRecorrido = true;
            // posicion actual A1
            int [] posicionA1 = new int [2];
            posicionA1 = posicionActual(Laberinto.mapa, Laberinto.a1);
            // Calculo los movimiento que debe hacer para llegar a la posicion final del A2
            int filaFinalA2 = Math.abs(Laberinto.posicionFinalAgente2[0] - posicionA1[0]);
            int columnaFinalA2 = Math.abs(Laberinto.posicionFinalAgente2[1] - posicionA1[1]);
            // Calcula los movimiento que debe hace para llegar a la posicion inicial del A2
            int filaInicioA2 = Math.abs(Laberinto.posicionInicialAgente2[0] - posicionA1[0]);
            int columnaInicioA2 = Math.abs(Laberinto.posicionInicialAgente2[1] - posicionA1[1]);
            
            // Si el camino a la posicion inicial de A2 es menor al camino final de A2 sigue la trayectoria,
            // sino cambia de camino y se dirige hacia su posicion final
            if ((filaInicioA2 + columnaInicioA2) < (filaFinalA2 + columnaFinalA2)) {
                continuarRecorrido = true;
            }else{
                continuarRecorrido = false;
            }
 
            return continuarRecorrido;
        }
        
        // Funcion que busca un camino alternativo hacia el objetivo
        public String recalculando9(){
            
          int valorVariable, elemento = 0;
          int [] valorMovimiento = new int [4];
          boolean [] boolMovimiento = new boolean[4];
          
          String caminoFinal = "";
          while((Laberinto.mapa[fila][columna] > -1) && (Laberinto.mapa[fila][columna] != Laberinto.a2) && (Laberinto.mapa[fila][columna] != 910) && (corteUno == false)){
              Laberinto.mapaBool[fila][columna] = false;
              
              for (int i = 0; i < valorMovimiento.length; i++) {
                  valorMovimiento[i] = 0;
                  boolMovimiento[i] = true;
              }
              
            if((Laberinto.mapa[fila - 1][columna] == Laberinto.a2) || (Laberinto.mapa[fila - 1][columna] == Laberinto.s)){
                matrizSolucion[fila - 1][columna] = 5;
                Laberinto.mapa[fila - 1][columna] = Laberinto.a1;
                Laberinto.mapaG[columna][fila - 1].setIcon(Laberinto.analizar);
                valorMovimiento[2] = 5;
                caminoFinal = caminoFinal + 'w';
                caminoSucio2 = caminoSucio2 + 'w';
                corteUno = true;
            }else if (Laberinto.mapa[fila - 1][columna] > -1){
               if(Laberinto.mapaBool[fila - 1][columna] == true){
                    if ((x2-fila) < 0) {
                            matrizSolucion[fila - 1][columna] = 5;
                            Laberinto.mapa[fila - 1][columna] = Laberinto.a1;
                            Laberinto.mapaG[columna][fila - 1].setIcon(Laberinto.analizar);
                            valorMovimiento[2] = 5;
                            caminoSucio2 = caminoSucio2 + 'w';
                            
                        }else{
                            matrizSolucion[fila - 1][columna] = 2;
                            Laberinto.mapaG[columna][fila - 1].setIcon(Laberinto.analizarAlternativa);
                            Laberinto.mapa[fila - 1][columna] = Laberinto.a1;
                            valorMovimiento[2] = 2;
                            caminoSucio2 = caminoSucio2 + 'w';
                        }
                        this.miTimer(100);
                }else{
                   boolMovimiento[2] = false;
               }
            }
            
            if((Laberinto.mapa[fila + 1][columna] == Laberinto.a2) || (Laberinto.mapa[fila + 1][columna] == Laberinto.s)){
                matrizSolucion[fila + 1][columna] = 5;
                    Laberinto.mapa[fila + 1][columna] = Laberinto.a1;
                Laberinto.mapaG[columna][fila + 1].setIcon(Laberinto.analizar);
                valorMovimiento[2] = 5;
                caminoFinal = caminoFinal + 's';
                caminoSucio2 = caminoSucio2 + 's';
                corteUno = true;
            }else if (Laberinto.mapa[fila + 1][columna] > -1) {
                if ( Laberinto.mapaBool[fila + 1][columna] == true) {
                    if ((x2 - fila) >= 0) {
                            matrizSolucion[fila + 1][columna] = 5;
                            Laberinto.mapa[fila + 1][columna] = Laberinto.a1;
                            Laberinto.mapaG[columna][fila + 1].setIcon(Laberinto.analizar);
                            valorMovimiento[0] = 5;
                            caminoSucio2 = caminoSucio2 +  's';
                            
                        }else{
                            matrizSolucion[fila + 1][columna] = 2;
                            Laberinto.mapaG[columna][fila + 1].setIcon(Laberinto.analizarAlternativa);
                            Laberinto.mapa[fila + 1][columna] = Laberinto.a1;
                            valorMovimiento[0] = 2;
                            caminoSucio2 = caminoSucio2 + 's';
                        }
                        this.miTimer(100);
                }else{
                    boolMovimiento[0] = false;
                }
            }
            if((Laberinto.mapa[fila][columna - 1] == Laberinto.a2) || (Laberinto.mapa[fila][columna - 1] == Laberinto.s)){
                matrizSolucion[fila][columna - 1] = 5;
                    Laberinto.mapa[fila][columna - 1] = Laberinto.a1;
                    Laberinto.mapaG[columna - 1][fila].setIcon(Laberinto.analizar);
                    valorMovimiento[2] = 5;
                    caminoFinal = caminoFinal + 'a';
                    caminoSucio2 = caminoSucio2 + 'a';
                corteUno = true;
            }else if ((Laberinto.mapa[fila][columna - 1] > - 1)){
                if(Laberinto.mapaBool[fila][columna - 1] == true){
                   if ((y2-columna)<0) {
                       matrizSolucion[fila][columna - 1] = 5;
                       Laberinto.mapa[fila][columna - 1] = Laberinto.a1;
                       Laberinto.mapaG[columna - 1][fila].setIcon(Laberinto.analizar);
                       valorMovimiento[1] = 5;
                       caminoSucio2 = caminoSucio2 + 'a';
                   }else{
                       matrizSolucion[fila][columna - 1] = 2;
                       Laberinto.mapaG[columna - 1][fila].setIcon(Laberinto.analizarAlternativa);
                       Laberinto.mapa[fila][columna - 1] = Laberinto.a1;
                       valorMovimiento[1] = 2;
                       caminoSucio2 = caminoSucio2 + 'a';
                   }
                   this.miTimer(100);
                }else{
                    boolMovimiento[1] = false;
                }
            }
            if((Laberinto.mapa[fila][columna + 1] == Laberinto.a2) || (Laberinto.mapa[fila][columna + 1] == Laberinto.s)){
                matrizSolucion[fila][columna + 1] = 5;
                    Laberinto.mapa[fila][columna + 1] = Laberinto.a2;
                    Laberinto.mapaG[columna + 1][fila].setIcon(Laberinto.analizar);
                    valorMovimiento[2] = 5;
                    caminoFinal = caminoFinal + 'd';
                    caminoSucio2 = caminoSucio2 + 'd';
                corteUno = true;
            }else if (Laberinto.mapa[fila][columna + 1] > -1){
                if (Laberinto.mapaBool[fila][columna + 1] == true) {
                    if ((y2 - columna) >= 0) {
                        matrizSolucion[fila][columna + 1] = 5;
                        Laberinto.mapa[fila][columna + 1] = Laberinto.a1;
                        Laberinto.mapaG[columna + 1][fila].setIcon(Laberinto.analizar);
                        valorMovimiento[3] = 5;
                        caminoSucio2 = caminoSucio2 + 'd';
                    }else{
                        matrizSolucion[fila][columna + 1] = 2;
                        Laberinto.mapaG[columna + 1][fila].setIcon(Laberinto.analizarAlternativa);
                        Laberinto.mapa[fila][columna + 1] = Laberinto.a1;
                        valorMovimiento[3] = 2;
                        caminoSucio2 = caminoSucio2 + 'd';
                    }
                    this.miTimer(100);
                }else{
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
            
            if (((boolMovimiento[0] == false) || Laberinto.mapa[fila + 1][columna] < 0 ) && ((boolMovimiento[1] == false) || Laberinto.mapa[fila][columna - 1] < 0) && ((boolMovimiento[2] == false) || Laberinto.mapa[fila - 1][columna] < 0) && ((boolMovimiento[3] == false) || Laberinto.mapa[fila][columna + 1] < 0 )){
                int p = caminoFinal.length() - 1;
                
                switch(caminoFinal.charAt(p)){
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
            }else{
                switch(elemento){
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
         
          if ((Laberinto.mapa[fila][columna] == Laberinto.a2) || (Laberinto.mapa[fila][columna] == 910) ) {
              Laberinto.mapaG[columna][fila].setIcon(Laberinto.analizar);
              
          }
         
          return caminoFinal;
        }
                     
        // Ejecuta los movimientos recibido 
        public void continuarCaminoRecibido(boolean continuar1){
            // Es true mientras sea mas cerca la salida recibida.
            boolean corteContinuar = continuar1;
            // Si es mas eficiente el camino recibido, lo sigue. De lo contrario, lo cambia.
            if (continuar1 == true) {
                // Guarda la posicion donde arranco el A2
                x2 = Laberinto.posicionInicialAgente2[0];
                y2 = Laberinto.posicionInicialAgente2[1];
                // Guarda la posicion actual de A1
                int [] posActual1 = posicionActual(Laberinto.mapa, Laberinto.a1);
                fila = posActual1[0];
                columna = posActual1[1];              
                /*
                    ACA ESTA EL PROBLEMA
                */
                System.out.println("sigo mi camino");
                recorridoAgente2 = true;
                recalcular = true;
                
                // A1 se mueve en filas              
                while((x2 != fila) && (objeto == false) && (continuar1 == true)){
                    this.miTimer(250);
                           
                    if ((x2-fila)<0) {
                        this.direccion(2);
                    }else if((x2 - fila) > 0){
                             this.direccion(0);
                    }
                    continuar1 = this.seguirRecorrido();
                }
                // A1 se mueve en columnas              
                while((y2 != columna) && (objeto == false) && (continuar1 == true)){
                        this.miTimer(250);
                        if ((y2-columna)<0) {
                            this.direccion(1);
                        }else if((y2 - columna) > 0){
                               this.direccion(3);
                        }           
                        continuar1 = this.seguirRecorrido();
                }
            }
                   
            // se encamina a la posicion final de A2
            if (continuar1 == false) {
                System.out.println("camino alternativo...");
                // Fila y columna de la posicion final de A2
                x2 = Laberinto.posicionFinalAgente2[0];
                y2 = Laberinto.posicionFinalAgente2[1];
                // Guarda la posicion actual de A1
                int [] posActual1 = posicionActual(Laberinto.mapa, Laberinto.a1);
                int filaAlternativa = posActual1[0];
                int columnaAlternativa = posActual1[1];
                
                while((x2 != fila) && (objeto == false)){
                      recalcular = true;
                      this.miTimer(250);
                      if ((x2-fila)<0) {
                          this.direccion(2);
                      }else if((x2 - fila) > 0){
                          this.direccion(0);
                      }
                }                
                        
                // A1 se mueve en columnas
                while((y2 != columna) && (objeto == false)){
                      this.miTimer(250);
                      if ((y2-columna)<0) {
                          this.direccion(1);
                      }else if((y2 - columna) > 0){
                                this.direccion(3);
                      }           
                }                        
            }  
        }
        
        // Temporizador
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
        public boolean esAgenteDos(char movimiento){
            boolean agenteDos = false;
            switch(movimiento){
                case 'w':
                    if (Laberinto.mapa[fila - 1][columna] == Laberinto.a2) {
                        agenteDos = true;
                    }
                    break;
                case 's':
                    if (Laberinto.mapa[fila + 1][columna] == Laberinto.a2) {
                        agenteDos = true;
                    }
                    break;
                case 'a':
                    if (Laberinto.mapa[fila][columna - 1] == Laberinto.a2) {
                        agenteDos = true;
                    }
                    break;
                case 'd':
                    if (Laberinto.mapa[fila][columna + 1] == Laberinto.a2) {
                        agenteDos = true;
                    }
                    break;
            }
            return agenteDos;        
        }
             
        /* Movimiento*/
        public void actualizarAbajo(){
            fila = fila + 1;
            Laberinto.mapa[fila][columna] = Laberinto.a1;
            Laberinto.mapa[fila - 1][columna] = 0;
            //Pinta movimiento en el frame de A1
            Laberinto.btnMtz1[columna][fila].setBackground(Color.green);
            Laberinto.lblAbajo1.setIcon(Laberinto.imgAbajo);
            // Pinta moviiento de Laberinto principal
            Laberinto.mapaG[columna][fila].setIcon(Laberinto.agenteUno);
            Laberinto.mapaG[columna][fila - 1].setIcon(Laberinto.pasillo1);
        }
        
        public void actualizarIzquierda(){
            columna = columna - 1;
            Laberinto.mapa[fila][columna] = Laberinto.a1;
            Laberinto.mapa[fila][columna + 1] = 0;
            //Pinta movimiento en el frame de A1
            Laberinto.btnMtz1[columna][fila].setBackground(Color.green);
            Laberinto.lblIzquierda1.setIcon(Laberinto.imgIzquierda);
            // Pinta moviiento de Laberinto principal
            Laberinto.mapaG[columna][fila].setIcon(Laberinto.agenteUno);
            Laberinto.mapaG[columna + 1][fila].setIcon(Laberinto.pasillo1);
        }
        
        public void actualizarArriba(){
            fila = fila - 1;
            Laberinto.mapa[fila][columna] = Laberinto.a1;
            Laberinto.mapa[fila + 1][columna] = 0;
            //Pinta movimiento en el frame de A1
            Laberinto.btnMtz1[columna][fila].setBackground(Color.green);
            Laberinto.lblArriba1.setIcon(Laberinto.imgArriba);
            // Pinta moviiento de Laberinto principal
            Laberinto.mapaG[columna][fila].setIcon(Laberinto.agenteUno);
            Laberinto.mapaG[columna][fila + 1].setIcon(Laberinto.pasillo1);
        }
        
        public void actualizarDerecha(){
            columna = columna + 1;
            Laberinto.mapa[fila ][columna] = Laberinto.a1;
            Laberinto.mapa[fila][columna - 1] = 0;
            //Pinta movimiento en el frame de A1
            Laberinto.btnMtz1[columna][fila].setBackground(Color.green);
            Laberinto.lblDerecha1.setIcon(Laberinto.imgDerecha);
            Laberinto.mapaG[columna][fila].setIcon(Laberinto.agenteUno);
            Laberinto.mapaG[columna - 1][fila].setIcon(Laberinto.pasillo1);
            
        }
        
        @Override
        public boolean done() {
         
            return corteUno; 
        }

    }
   
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


        