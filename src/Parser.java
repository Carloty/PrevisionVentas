import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Random;

import trees.attributes.Attribute;
import trees.attributes.DateAttribute;
import trees.attributes.NominalAttribute;
import trees.attributes.NumericalAttribute;

public class Parser {
	
	public static HashMap<Integer, Attribute> getAllAttributes(){
		HashMap<Integer,Attribute> attributes = new HashMap<Integer,Attribute>();
		
		try {
			attributes.put(0, new NumericalAttribute("tienda", Attribute.AttributeType.NUMERICAL, 0, 1, 1115, false));
			
			String[] tipoTienda = {"a", "b", "c", "d"};
			attributes.put(1, new NominalAttribute("tipoTienda", Attribute.AttributeType.NOMINAL, tipoTienda, 1, false));
			
			String[] surdito = {"a", "b", "c"};
			attributes.put(2, new NominalAttribute("surdito", Attribute.AttributeType.NOMINAL, surdito, 2, false));
			
			attributes.put(3, new NumericalAttribute("distanciaCompetition", Attribute.AttributeType.NUMERICAL, 3, 0, 100000, true));
			
			attributes.put(4, new DateAttribute("fechaInicio", Attribute.AttributeType.DATE, "yyyy-MM-dd", 4, true)); // dd/MM/yyyy
			
			String[] bool = {"0", "1"};
			attributes.put(5, new NominalAttribute("promocion2", Attribute.AttributeType.NOMINAL, bool, 5, false));
			
			attributes.put(6, new NumericalAttribute("semanaInicioPromocion2", Attribute.AttributeType.NUMERICAL, 6, 1, 52, true));
			
			attributes.put(7, new NumericalAttribute("anoInicioPromocion2", Attribute.AttributeType.NUMERICAL, 7, 2009, 2015, true));
			
			String[] intervaloPromo = {"Jan-Apr-Jul-Oct", "Feb-May-Aug-Nov", "Mar-Jun-Sept-Dec"};
			attributes.put(8, new NominalAttribute("intervaloPromocion", Attribute.AttributeType.NOMINAL, intervaloPromo, 8, true));
			
			String[] diaSemana = {"1", "2", "3", "4", "5", "6", "7"};
			attributes.put(9, new NominalAttribute("diaSemana", Attribute.AttributeType.NOMINAL, diaSemana, 9, false));
			
			attributes.put(10, new DateAttribute("fecha", Attribute.AttributeType.DATE, "yyyy-MM-dd", 10, false));

			attributes.put(11, new NominalAttribute("abierto", Attribute.AttributeType.NOMINAL, bool, 11, false));

			attributes.put(12, new NominalAttribute("promocion", Attribute.AttributeType.NOMINAL, bool, 12, false));
			
			String[] festivo = {"0", "a", "b", "c"};
			attributes.put(13, new NominalAttribute("festivo", Attribute.AttributeType.NOMINAL, festivo, 13, false));
			
			attributes.put(14, new NominalAttribute("noLectivo", Attribute.AttributeType.NOMINAL, bool, 14, false));
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("WRONG LIST ATTRIBUTE");
		}
		
		return attributes;
	}
	
	public static double[][] getDataFromFile(String fileName){
		HashMap<Integer,Attribute> attList = getAllAttributes();
		String line;
		double[][] data = new double[610328][16];
		int index = 0;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			
			while ((line = br.readLine()) != null) {
				data[index++] = Parser.transformCSVdataToDouble(line, attList);
			}
			
			br.close();

		} catch (Exception e) {
			System.err.println("ERROR WHILE GETTING LINES FROM FILE");
			e.printStackTrace();
		}
		
		return data;
	}
	
	public static void getDataFromFile(String fileName, double percentTrain, double[][] dataTrain, double[][] dataTest){
		HashMap<Integer,Attribute> attList = getAllAttributes();
		String line;
		Random r = new Random();
		
		int indexTrain = 0;
		int indexTest = 0;
		
		double[] dataSample;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			
			while ((line = br.readLine()) != null) {
				dataSample = Parser.transformCSVdataToDouble(line, attList);
				double proba = r.nextDouble();
				if(proba < percentTrain && indexTrain < dataTrain.length || proba >= percentTrain && indexTest >= dataTest.length){
					dataTrain[indexTrain++] = dataSample;
				} else {
					dataTest[indexTest++] = dataSample;
				}
			}
			
			br.close();

		} catch (Exception e) {
			System.err.println("ERROR WHILE GETTING LINES FROM FILE WITH TEST/TRAIN");
			e.printStackTrace();
		}
	}
	
	public static void getDataFromInitialFile(String fileName, double percentTrain, double[][] dataTrain, double[][] dataTest){
		HashMap<Integer,Attribute> attList = getAllAttributes();
		String line;
		Random r = new Random();
		
		int indexTrain = 0;
		int indexTest = 0;
		
		double[] dataSample;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			
			line = br.readLine(); // first line of the CSV file (we don't want it)
			
			while ((line = br.readLine()) != null) {
				line = line.replace("\"","");
				dataSample = Parser.transformCSVdataToDouble(line, attList);
				double proba = r.nextDouble();
				if(proba < percentTrain && indexTrain < dataTrain.length || proba >= percentTrain && indexTest >= dataTest.length){
					dataTrain[indexTrain++] = dataSample;
				} else {
					dataTest[indexTest++] = dataSample;
				}
			}
			
			br.close();

		} catch (Exception e) {
			System.err.println("ERROR WHILE GETTING LINES FROM FILE WITH TEST/TRAIN");
			e.printStackTrace();
		}
	}
	
	public static double[] transformCSVdataToDouble(String s, HashMap<Integer,Attribute> attList){
		double[] data = new double[16];
		String[] attributes = s.split(",");
		try{
			
			data[0] = attList.get(0).valueOf(attributes[0]);
			data[1] = attList.get(1).valueOf(attributes[1]);
			data[2] = attList.get(2).valueOf(attributes[2]);
			data[3] = attList.get(3).valueOf(attributes[3]);
			
			// Ano y mes inicio
			if(!attributes[4].equals("")){
				String date = attributes[5] + "-" + attributes[4] + "-01";
				data[4] = attList.get(4).valueOf(date);
			} else {
				data[4] = attList.get(4).valueOf("");
			}				
			
			data[5] = attList.get(5).valueOf(attributes[6]);
			data[6] = attList.get(6).valueOf(attributes[7]);
			data[7] = attList.get(7).valueOf(attributes[8]);
			
			// Intervalo Promocion
			if (attributes[9].equals("")){
				data[8] = attList.get(8).valueOf("");
				
				data[9] = attList.get(9).valueOf(attributes[10]);
				data[10] = attList.get(10).valueOf(attributes[11]);
				
				// No se guarda el atributo Clientes : attributes[12]
				
				data[11] = attList.get(11).valueOf(attributes[13]);
				data[12] = attList.get(12).valueOf(attributes[14]);
				data[13] = attList.get(13).valueOf(attributes[15]);
				data[14] = attList.get(14).valueOf(attributes[16]);
				
				// Ventas
				data[15] = Double.valueOf(attributes[17]);
			} else {
				String interval = attributes[9] + "-" + attributes[10] + "-" + attributes[11] + "-" + attributes[12];
				data[8] = attList.get(8).valueOf(interval);
				
				data[9] = attList.get(9).valueOf(attributes[13]);
				data[10] = attList.get(10).valueOf(attributes[14]);
				
				// No se guarda el atributo Clientes : attributes[15]
				
				data[11] = attList.get(11).valueOf(attributes[16]);
				data[12] = attList.get(12).valueOf(attributes[17]);
				data[13] = attList.get(13).valueOf(attributes[18]);
				data[14] = attList.get(14).valueOf(attributes[19]);
				
				// Ventas
				data[15] = Double.valueOf(attributes[20]);
			}
		} catch (Exception e) {
			System.out.println("TROUBLES HERE !");
			System.out.println(e.getMessage());
		}
		return data;
	}
	
	public static double[][] modifyListAttributes(HashMap<Integer,Attribute> attributesList, int[] attributesToKeep, double[][] data){
		HashMap<Integer,Attribute> newAttributes = new HashMap<Integer,Attribute>();
		
		if (attributesList.size() == attributesToKeep.length-1){
			// Creating a new list with attributes to keep
			int newID = 0;
			for(int id : attributesList.keySet()){
				if (attributesToKeep[id] == 1){
					Attribute att = attributesList.get(id);
					att.setId(newID);
					newAttributes.put(newID, att);
					newID++;
				}
			}
			
			// Keeping cols of data array associated to the attributes to keep
			double[][] temp = new double[data.length][newAttributes.size()+1];
			for(int row = 0; row < data.length; row++){
				// column index of the new array
				int index = 0;
				for(int col = 0; col < data[0].length; col++){
					// if the attribute is to be kept
					if (attributesToKeep[col] == 1){
						temp[row][index] = data[row][col];
						index++;
					}
				}
			}
			
			attributesList.clear();
			attributesList.putAll(newAttributes);
			
			return temp;
		} else {
			return data;
		}
	}

	public static double[][] getNSamples(int n, double[][] data){
		Random r = new Random();
		double[][] samples = new double[n][data[0].length];
		
		for(int i=0; i<n; i++){
			int index = r.nextInt(data.length);
			samples[i] = data[index];
		}
		
		return samples;
	}
}
