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
			
			attributes.add(new DateAttribute("fechaInicio", Attribute.AttributeType.DATE, "yyyy-MM-dd", 4));
			
			attributes.add(new NumericalAttribute("promocion2", Attribute.AttributeType.NUMERICAL, 5, 0, 1));
			
			attributes.add(new NumericalAttribute("semanaInicioPromocion2", Attribute.AttributeType.NUMERICAL, 6, 1, 50));
			
			attributes.add(new NumericalAttribute("anoInicioPromocion2", Attribute.AttributeType.NUMERICAL, 7, 2000, 2020));
			
			// TODO IntervaloPromocion
			attributes.add(new NumericalAttribute("", Attribute.AttributeType.NUMERICAL, 8, 2000, 2020));
			
			String[] diaSemana = {"1", "2", "3", "4", "5", "6", "7"};
			attributes.add(new NominalAttribute("diaSemana", Attribute.AttributeType.NOMINAL, diaSemana, 9));
			
			attributes.add(new DateAttribute("fecha", Attribute.AttributeType.DATE, "yyyy-MM-dd", 10));

			attributes.add(new NumericalAttribute("abierto", Attribute.AttributeType.NUMERICAL, 11, 0, 1));

			attributes.add(new NumericalAttribute("promocion", Attribute.AttributeType.NUMERICAL, 12, 0, 1));
			
			String[] festivo = {"0", "a", "b", "c"};
			attributes.add(new NominalAttribute("festivo", Attribute.AttributeType.NOMINAL, festivo, 13));
			
			attributes.add(new NumericalAttribute("noLectivo", Attribute.AttributeType.NUMERICAL, 14, 0, 1));
			
			} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return attributes;
	}

	public static double[][] getDataFromSCVFile(String fileName){
		List<String> fileLines = getStringsFromFile(new File(fileName));
		
		double[][] data = new double[fileLines.size()][16];
		
		String[] attributes;
		for (int i=0; i < fileLines.size(); i++){
			attributes = fileLines.get(i).split(",");
			
			// TODO
			for (int j=0;j<4;j++){
				//data[i][j] = attributes[j];
			}
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
