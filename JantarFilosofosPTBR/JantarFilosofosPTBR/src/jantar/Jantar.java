package jantar;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Jantar {
	 private static final Random aleatorio = new Random();
	    public static final int NUMERO_DE_FILOSOFOS = 5;
	    private final Lock[] talher = new ReentrantLock[NUMERO_DE_FILOSOFOS];
	    private final Semaphore SemaforoJantar = new Semaphore(NUMERO_DE_FILOSOFOS - 1); 
	    private volatile boolean Simulando = true; 
	    private final int[] Contagem = new int[NUMERO_DE_FILOSOFOS]; 

	    private enum Acao {
	        PENSANDO("Pensando"),
	        PEGA_TALHER_ESQUERDO("Pegou talher esquerdo"),
	        PEGA_TALHER_DIREITO("Pegou talher direito"),
	        FILOSOFOS_COMEM("Comendo"),
	        TALHER_ESQUERDO_COLOCADO_NA_MESA("Colocou talher esquerdo na mesa"),
	        TALHER_DIREITO_COLOCADO_NA_MESA("Colocou talher direito na mesa");

	        private final String acaoString;

	        Acao(String acaoString) {
	            this.acaoString = acaoString;
	        }

	        @Override
	        public String toString() {
	            return acaoString;
	        }
	    }

	    public Jantar() {
	        for (int i = 0; i < NUMERO_DE_FILOSOFOS; i++) {
	            talher[i] = new ReentrantLock();
	        }
	    }

	     public void Janta(int IdFilosofo) throws InterruptedException {
	        Lock talherEsquerdo = talher[IdFilosofo];
	        Lock talherDireito = talher[(IdFilosofo + 1) % NUMERO_DE_FILOSOFOS];

			while (Simulando) { 
	            //Tenta adquirir o semáforo do 
				SemaforoJantar.acquire();

	                //Tenta pegar garfo
	                if (talherEsquerdo.tryLock()) {
	                    try {
	                        log(IdFilosofo, Acao.PEGA_TALHER_ESQUERDO);
	                        if (talherDireito.tryLock()) {
	                            try {
	                                log(IdFilosofo, Acao.PEGA_TALHER_DIREITO);
	                                //Pegou os dois garfos e então come
	                                comer(IdFilosofo);
	                            } finally {
	                            	//Libera o garfo direito
	                            	talherDireito.unlock();
	                                log(IdFilosofo, Acao.TALHER_DIREITO_COLOCADO_NA_MESA);
	                            }
	                        }
	                    } finally {
	                    //Libera o garfo esquerdo
	                    talherEsquerdo.unlock();
	                    log(IdFilosofo, Acao.TALHER_ESQUERDO_COLOCADO_NA_MESA);
	                    }
	                    SemaforoJantar.release();
	                }

	            pensar(IdFilosofo);
	        }
	}


	    private void log(int IdFilosofo, Acao action) {
	        System.out.println("Filosofo " + IdFilosofo + ": " + action);
	    }

	    private void comer(int IdFilosofo) throws InterruptedException {
	        log(IdFilosofo, Acao.FILOSOFOS_COMEM);
	        //Incrementa a contagem de refeições para este filósofo
	        Contagem[IdFilosofo]++; 
	        //Simula o tempo de comer
	        Thread.sleep(aleatorio.nextInt(1000));
	    }

	    private void pensar(int IdFilosofo) throws InterruptedException {
	        log(IdFilosofo, Acao.PENSANDO);
	        //Simular tempo pensando
	        Thread.sleep(aleatorio.nextInt(1000));
	 }
	    
	 // Método que a thread principal usará para sinalizar a parada.
	    public void pararSimulacao() {
	        this.Simulando = false;
	    }

	    // Método que a thread principal usará para exibir o resultado.
	    public void saidaDeEstatisticas() {
	        System.out.println("\n--- Estatísticas Finais ---");
	        // Aqui você pode adicionar lógica para encontrar o máximo e mínimo, se quiser.
	        for (int i = 0; i < NUMERO_DE_FILOSOFOS; i++) {
	            System.out.println("Filósofo " + i + " comeu " + Contagem[i] + " vezes.");
	        }
	        System.out.println("---------------------------\n");
	    }
}