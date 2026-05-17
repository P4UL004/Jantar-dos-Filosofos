package principal;
import java.util.List;

import jantar.Jantar;
import java.util.ArrayList;

public class Principal {

    //Define a duração total da simulação em milissegundos (Ex: 10 segundos)
    private static final long DURACAO_SIMULACAO = 10 * 1000; 

    public static void main(String[] args) {
        
        Jantar jantarDosFilosofos = new Jantar();
        //Lista para armazenar as threads
        List<Thread> ThreadsFilosofos = new ArrayList<>(); 
        
        System.out.println("Simulação iniciada. Rodando por " + (DURACAO_SIMULACAO / 1000) + " segundos.");

        for (int i = 0; i < jantarDosFilosofos.NUMERO_DE_FILOSOFOS; i++) {
            final int IdFilosofos = i;
            //Armazenamos a thread antes de iniciá-la
            Thread threadFilosofo = new Thread(() -> { 
                try {
                	jantarDosFilosofos.Janta(IdFilosofos);
                } catch (InterruptedException e) {
                    //A thread pode ser interrompida durante o sleep/acquire
                    System.out.println("Filósofo " + IdFilosofos + " interrompido.");
                }
            });
            
            ThreadsFilosofos.add(threadFilosofo); // Adiciona na lista
            threadFilosofo.start(); // Inicia
        }

        try {
            // 1. Espera o tempo definido para a simulação
            Thread.sleep(DURACAO_SIMULACAO);
            
            // 2. Sinaliza para todas as threads pararem
            System.out.println("\nTempo esgotado. Sinalizando filósofos para parar...");
            jantarDosFilosofos.pararSimulacao(); 
            
            // 3. Espera todas as threads terminarem com join()
            // Isso garante que o programa principal só continue depois que todos os filósofos pararem.
            for (Thread thread : ThreadsFilosofos) {
                thread.join(); 
            }

        } catch (InterruptedException e) {
            // Lógica para interromper a thread principal
            Thread.currentThread().interrupt();
        }

        jantarDosFilosofos.saidaDeEstatisticas();
        System.out.println("Simulação finalizada.");
    }

	private boolean Simulando;

    // Método para parar a simulação
    public void pararSimulacao() {
        this.Simulando = false;
    }
    
    // Método para exibir estatísticas
    public void saidaDeEstatisticas() {
        System.out.println("Estatísticas:...");
    }
}