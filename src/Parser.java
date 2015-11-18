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
			attributes.add(new NumericalAttribute("tienda", Attribute.AttributeType.NUMERICAL, 0, 1, 1115));
			
			String[] tipoTienda = {"a", "b", "c", "d"};
			attributes.add(new NominalAttribute("tipoTienda", Attribute.AttributeType.NOMINAL, tipoTienda, 1));
			
			String[] surdito = {"a", "b", "c"};
			attributes.add(new NominalAttribute("surdito", Attribute.AttributeType.NOMINAL, surdito, 2));
			
			attributes.add(new NumericalAttribute("distanciaCompetition", Attribute.AttributeType.NUMERICAL, 3, 0, 100000));
			
			attributes.add(new DateAttribute("fechaInicio", Attribute.AttributeType.DATE, "dd/MM/yyyy", 4));
			
			attributes.add(new NumericalAttribute("promocion2", Attribute.AttributeType.NUMERICAL, 5, 0, 1));
			
			attributes.add(new NumericalAttribute("semanaInicioPromocion2", Attribute.AttributeType.NUMERICAL, 6, 1, 50));
			
			attributes.add(new NumericalAttribute("anoInicioPromocion2", Attribute.AttributeType.NUMERICAL, 7, 2000, 2020));
			
			// TODO IntervaloPromocion OK ?
			String[] mesPromocion = {"0", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
			attributes.add(new NominalAttribute("primeroMesPromocion", Attribute.AttributeType.NOMINAL, mesPromocion, 8));
			attributes.add(new NominalAttribute("segundoMesPromocion", Attribute.AttributeType.NOMINAL, mesPromocion, 9));
			attributes.add(new NominalAttribute("terceroMesPromocion", Attribute.AttributeType.NOMINAL, mesPromocion, 10));
			attributes.add(new NominalAttribute("cuartoMesPromocion", Attribute.AttributeType.NOMINAL, mesPromocion, 11));
			
			String[] diaSemana = {"1", "2", "3", "4", "5", "6", "7"};
			attributes.add(new NominalAttribute("diaSemana", Attribute.AttributeType.NOMINAL, diaSemana, 12));
			
			attributes.add(new DateAttribute("fecha", Attribute.AttributeType.DATE, "dd/MM/yyyy", 13));

			attributes.add(new NumericalAttribute("abierto", Attribute.AttributeType.NUMERICAL, 14, 0, 1));

			attributes.add(new NumericalAttribute("promocion", Attribute.AttributeType.NUMERICAL, 15, 0, 1));
			
			String[] festivo = {"0", "a", "b", "c"};
			attributes.add(new NominalAttribute("festivo", Attribute.AttributeType.NOMINAL, festivo, 16));
			
			attributes.add(new NumericalAttribute("noLectivo", Attribute.AttributeType.NUMERICAL, 17, 0, 1));
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return attributes;
	}

	public static double[][] getDataFromSCVFile(String fileName){
		List<String> fileLines = getStringsFromFile(new File(fileName));
		List<Attribute> attList = getAllAttributes();
		
		double[][] data = new double[fileLines.size()][19];
		try {
			String[] attributes;
			
			// For each line of the CSV file
			for (int i=0; i < fileLines.size(); i++){
				attributes = fileLines.get(i).split(",");
				
				data[i][0] = attList.get(0).valueOf(attributes[0]);
				data[i][1] = attList.get(1).valueOf(attributes[1]);
				data[i][2] = attList.get(2).valueOf(attributes[2]);
				data[i][3] = attList.get(3).valueOf(attributes[3]);
				
				// Ano y mes inicio
				String date = "01/"+attributes[5]+"/"+attributes[6];
				data[i][4] = attList.get(4).valueOf(date);
				
				data[i][5] = attList.get(5).valueOf(attributes[7]);
				data[i][6] = attList.get(6).valueOf(attributes[8]);
				data[i][7] = attList.get(7).valueOf(attributes[9]);
				
				// Intervalo Promocion
				data[i][8] = attList.get(8).valueOf(attributes[10]);
				data[i][9] = attList.get(9).valueOf(attributes[11]);
				data[i][10] = attList.get(10).valueOf(attributes[12]);
				data[i][11] = attList.get(11).valueOf(attributes[13]);
				
				data[i][12] = attList.get(12).valueOf(attributes[14]);
				data[i][13] = attList.get(13).valueOf(attributes[15]);
				
				// No se guarda el atributo Clientes : attributes[16]
				
				data[i][14] = attList.get(14).valueOf(attributes[17]);
				data[i][15] = attList.get(15).valueOf(attributes[18]);
				data[i][16] = attList.get(16).valueOf(attributes[19]);
				data[i][17] = attList.get(17).valueOf(attributes[20]);
				
				// Ventas
				data[i][18] = Double.valueOf(attributes[21]);
				
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	
		return data;
	}
	
	public static List<String> getStringsFromFile(File file){
		List<String> fileLines = new ArrayList<String>();
		String line;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			while ((line = br.readLine()) != null) {
				fileLines.add(line);
			}
			
			br.close();

		} catch (Exception e) {
			System.err.println("ERROR WHILE GETTING LINES FROM FILE");
			e.printStackTrace();
		}
		
		return fileLines;
	}
}
