import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import trees.attributes.Attribute;
import trees.attributes.DateAttribute;
import trees.attributes.NominalAttribute;
import trees.attributes.NumericalAttribute;

public class Parser {
	
	public static List<Attribute> getAllAttributes(){
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		try {
			attributes.add(new NumericalAttribute("tienda", Attribute.AttributeType.NUMERICAL, 0, 1, 1115, false));
			
			String[] tipoTienda = {"a", "b", "c", "d"};
			attributes.add(new NominalAttribute("tipoTienda", Attribute.AttributeType.NOMINAL, tipoTienda, 1, false));
			
			String[] surdito = {"a", "b", "c"};
			attributes.add(new NominalAttribute("surdito", Attribute.AttributeType.NOMINAL, surdito, 2, false));
			
			attributes.add(new NumericalAttribute("distanciaCompetition", Attribute.AttributeType.NUMERICAL, 3, 0, 100000, false));
			
			attributes.add(new DateAttribute("fechaInicio", Attribute.AttributeType.DATE, "yyyy-MM-dd", 4, true)); // dd/MM/yyyy
			
			attributes.add(new NumericalAttribute("promocion2", Attribute.AttributeType.NUMERICAL, 5, 0, 1, false));
			
			attributes.add(new NumericalAttribute("semanaInicioPromocion2", Attribute.AttributeType.NUMERICAL, 6, 1, 50, true));
			
			attributes.add(new NumericalAttribute("anoInicioPromocion2", Attribute.AttributeType.NUMERICAL, 7, 2000, 2020, true));
			
			String[] mesPromocion = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
			attributes.add(new NominalAttribute("primeroMesPromocion", Attribute.AttributeType.NOMINAL, mesPromocion, 8, true));
			attributes.add(new NominalAttribute("segundoMesPromocion", Attribute.AttributeType.NOMINAL, mesPromocion, 9, true));
			attributes.add(new NominalAttribute("terceroMesPromocion", Attribute.AttributeType.NOMINAL, mesPromocion, 10, true));
			attributes.add(new NominalAttribute("cuartoMesPromocion", Attribute.AttributeType.NOMINAL, mesPromocion, 11, true));
			
			String[] diaSemana = {"1", "2", "3", "4", "5", "6", "7"};
			attributes.add(new NominalAttribute("diaSemana", Attribute.AttributeType.NOMINAL, diaSemana, 12, false));
			
			attributes.add(new DateAttribute("fecha", Attribute.AttributeType.DATE, "yyyy-MM-dd", 13, false));

			attributes.add(new NumericalAttribute("abierto", Attribute.AttributeType.NUMERICAL, 14, 0, 1, false));

			attributes.add(new NumericalAttribute("promocion", Attribute.AttributeType.NUMERICAL, 15, 0, 1, false));
			
			String[] festivo = {"0", "a", "b", "c"};
			attributes.add(new NominalAttribute("festivo", Attribute.AttributeType.NOMINAL, festivo, 16, false));
			
			attributes.add(new NumericalAttribute("noLectivo", Attribute.AttributeType.NUMERICAL, 17, 0, 1, false));
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("WRONG LIST ATTRIBUTE");
		}
		
		return attributes;
	}
	
	public static double[][] getDataFromFile(String fileName){
		List<Attribute> attList = getAllAttributes();
		String line;
		double[][] data = new double[610328][19];
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
	
	public static double[] transformCSVdataToDouble(String s, List<Attribute> attList){
		double[] data = new double[19];
		String[] attributes = s.split(",");
		try{
			
			data[0] = attList.get(0).valueOf(attributes[0]);
			data[1] = attList.get(1).valueOf(attributes[1]);
			data[2] = attList.get(2).valueOf(attributes[2]);
			data[3] = attList.get(3).valueOf(attributes[3]);
			
			// Ano y mes inicio
			if(!attributes[4].equals("")){
				String date = attributes[5] + "-" + attributes[4] + "-01";
				//String date = "01/"+attributes[5]+"/"+attributes[6];
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
				data[9] = attList.get(9).valueOf("");
				data[10] = attList.get(10).valueOf("");
				data[11] = attList.get(11).valueOf("");
				
				data[12] = attList.get(12).valueOf(attributes[10]);
				data[13] = attList.get(13).valueOf(attributes[11]);
				
				// No se guarda el atributo Clientes : attributes[12]
				
				data[14] = attList.get(14).valueOf(attributes[13]);
				data[15] = attList.get(15).valueOf(attributes[14]);
				data[16] = attList.get(16).valueOf(attributes[15]);
				data[17] = attList.get(17).valueOf(attributes[16]);
				
				// Ventas
				data[18] = Double.valueOf(attributes[17]);
			} else {
				data[8] = attList.get(8).valueOf(attributes[9]);
				data[9] = attList.get(9).valueOf(attributes[10]);
				data[10] = attList.get(10).valueOf(attributes[11]);
				data[11] = attList.get(11).valueOf(attributes[12]);
				
				data[12] = attList.get(12).valueOf(attributes[13]);
				data[13] = attList.get(13).valueOf(attributes[14]);
				
				// No se guarda el atributo Clientes : attributes[15]
				
				data[14] = attList.get(14).valueOf(attributes[16]);
				data[15] = attList.get(15).valueOf(attributes[17]);
				data[16] = attList.get(16).valueOf(attributes[18]);
				data[17] = attList.get(17).valueOf(attributes[19]);
				
				// Ventas
				data[18] = Double.valueOf(attributes[20]);
			}
		} catch (Exception e) {
			System.out.println("TROUBLES HERE !");
			System.out.println(e.getMessage());
		}
		return data;
	}
	
	public static double[][] modifyListAttributes(List<Attribute> attributesList, int[] attributesToKeep, double[][] data){
		List<Attribute> newList = new ArrayList<Attribute>();
		if (attributesList.size() == attributesToKeep.length-1){
			// Creating a new list with attributes to keep
			for (int i=0; i< attributesToKeep.length-1; i++){
				if (attributesToKeep[i] == 1){
					newList.add(attributesList.get(i));
				}
			}
			
			// Keeping cols of data array associated to the attributes to keep
			double[][] temp = new double[data.length][newList.size()+1];
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

			// Updating attributes ID
			for (int i=0; i< newList.size(); i++){
				Attribute att = newList.get(i);
				att.setId(i);
				newList.remove(i); newList.add(i, att); // TODO or not ?
			}	
			
			attributesList.clear();
			attributesList.addAll(newList);
			
			return temp;
		} else {
			return data;
		}
	}
}
