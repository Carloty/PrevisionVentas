import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import trees.RegressionTree;

public class GeneticProgramming {

	public static List<Integer> selection(List<RegressionTree> population, double selectivePressure, double[][] data){
		return ruleta(asignacionFitnessPorRango(population,selectivePressure, data));		
	}
	
	
	public static List<Integer> selecionarMejores(List<RegressionTree> individuos, double presionSelectiva, double[][] data) {
		//return ruleta(asignacionFitnessProporcional(individuos, data));
		return ruleta(asignacionFitnessPorRango(individuos,presionSelectiva, data));		
	}
	
	public static List<Integer> ruleta(double[] fitness) {
		int tamano = fitness.length;
		List<Integer> indicesMejores = new ArrayList<Integer>(0);
		
		// Calcular el total del fitness
		double fitnessTotal = 0;
		for (int i = 0; i < tamano; i++) {
			fitnessTotal += fitness[i];
		}
		
		// Calcular las probabilidades acumuladas
		double[] probas = new double[tamano];
		probas[0] = fitness[0] / fitnessTotal;
		for (int i = 1; i < tamano - 1; i++) {
			probas[i] = probas[i-1] + fitness[i] / fitnessTotal;
			//System.out.println("Proba cumulée pour indiv " + i + " : " + probas[i]);
		}
		probas[tamano-1] = 1;
		
		// Selecionar los individuos
		Random r = new Random();
		double valor;
		for (int i = 0; i < tamano; i++) {
			valor = r.nextDouble();
			for (int j = 0; j < tamano; j++) {
				if (valor < probas[j]) {
					indicesMejores.add(j);
					break;
				}
			}			
		}
		
		return indicesMejores;
	}
	
	public static int[] getRangoConEvaluacion(double[] evaluacion) {
		int tamano = evaluacion.length;
		int[] ranks = new int[tamano];
		
		// Calcular el rango de los individuos
				for (int i = 0; i < tamano; i++) {
					ranks[i] = 0;
					for (int j = 0; j < tamano; j++) {
						if (i != j && evaluacion[i] >= evaluacion[j]) {
							ranks[i]++;
						}
					}
					ranks[i]++;
				}
		return ranks;
	}
 	
	public static double[] getFitnessConRango(double presionSelectiva, int[] ranks) {
		int tamano = ranks.length;
		double[] fitness = new double[tamano];
		
		// Calcular el fitness
		for (int i = 0; i < tamano; i++) {
			fitness[i] = 2-presionSelectiva + 2*(presionSelectiva-1)*(ranks[i]-1)/(tamano-1);
		}
		return fitness;
	}
	
	private static double[] asignacionFitnessPorRango(List<RegressionTree> individuos, double presionSelectiva, double[][] data) {
		int tamano = individuos.size();
		double[] evaluacion = new double[tamano];
		double[] fitness = new double[tamano];

		// Asignación del fitness por ranking
		for (int i = 0; i < tamano; i++) {
			evaluacion[i] = (-1)*individuos.get(i).getFitness(data);
		}
		fitness = GeneticProgramming.getFitnessConRango(presionSelectiva, GeneticProgramming.getRangoConEvaluacion(evaluacion));
		return fitness;
	}
	
	private static double[] asignacionFitnessProporcional(List<RegressionTree> individuos, double[][] data) {
		int tamano = individuos.size();
		double[] fitness = new double[tamano];
		for (int i = 0; i < tamano; i++) {
			fitness[i] = (-1)*individuos.get(i).getFitness(data);
		}
		return fitness;		
	}

}
