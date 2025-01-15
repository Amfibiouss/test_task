package aaa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	private static boolean isLong(String str) {
		try {
			Long.parseLong(str);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	private static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	private static Writer initializeWriter(String filename, boolean append) {
		
		try {
			Writer writer = new FileWriter(filename, append);
			writer = new BufferedWriter(writer);
			return writer;
		} catch (IOException e) {
			System.out.println("Не удалось открыть файл " + filename);
			return null;
		}
	}
	
	private static void tryWrite(Writer writer, String str) {
		
		try {
			writer.write(str + "\n");
		} catch (IOException e) {
			System.out.println("Не удалось записать в файл строку: " + str);
		}
	}
	
	private static void closeWriter(Writer writer) {
		
		try {
			writer.close();
		} catch (IOException e) {
			System.out.println("Произошла ошибка во время закрытия файла");
		}
	}
	
	
    public static void main(String[] args) {
    	
    	String path = null;
    	String prefix = null;
    	boolean append = false;
    	boolean show_statistics = false;
    	boolean show_verbose_statistics = false;
    	List <String> files = new ArrayList<String>();
    	
    	
    	for (int i = 0; i < args.length; i++) {
    		switch(args[i]) {
    		case "-o":
    			i++;
    			
    			if (i == args.length) {
    				System.out.println("Ошибка. После опции -o нет аргумента");
    				break;
    			}
    			
    			if (path != null) {
    				System.out.println("Ошибка. Опция -o встречается несколько раз");
    				break;   				
    			}
    			
    			path = args[i];
    			break;
    			
    		case "-p":
    			i++;
    			
    			if (i == args.length) {
    				System.out.println("Ошибка. После опции -p нет аргумента");
    				break;
    			}
    			
    			if (prefix != null) {
    				System.out.println("Ошибка. Опция -p встречается несколько раз");
    				break;   				
    			}
    			
    			prefix = args[i];
    			break;
    			
    		case "-a":
    			append = true;
    			break;
    			
    		case "-s":
    			show_statistics = true;
    			break;
    			
    		case "-f":
    			show_verbose_statistics = true;
    			break;
    			
    		default:
    			files.add(args[i]);
    			break;
    		}
    	}
    	
    	if (show_statistics && show_verbose_statistics) {
    		show_statistics = false;
    	}
    	
    	if (prefix == null)
    		prefix = "";
    	
    	if (path == null)
    		path = ".";	
    	
    	long long_cnt = 0, double_cnt = 0, str_cnt = 0;
    	
    	long long_max = 0, long_min = 0, long_sum  = 0;
    	double double_max = 0, double_min = 0, double_sum = 0;
    	long str_max = 0, str_min = 0;
    	
    	Writer long_writer = null, double_writer = null, str_writer = null;
    	
    	for (String file : files) {
    		
        	try (FileReader freader = new FileReader(file);
        		BufferedReader reader = new BufferedReader(freader))
        	{
        		for (String str = reader.readLine(); str != null; str = reader.readLine()) {

        			if (isLong(str)) {
        				if (long_writer == null) 
        					long_writer = initializeWriter(path + "/" + prefix + "integers.txt", append);
        				
        				if(long_writer != null)
        					tryWrite(long_writer, str);
        				
        				if (show_statistics) {
        					long_cnt++;
        				}
        				
        				if (show_verbose_statistics) {
        					long_cnt++;
        					
        					long number = Long.parseLong(str);
        					
        					if (long_cnt == 1 || number < long_min) 
        						long_min = number;
        					
        					if (long_cnt == 1 || number > long_max) 
        						long_max = number;
        					
        					long_sum += number;
        				} 
        				
        			} else if (isDouble(str)) {
        				
        				if (double_writer == null) 
        					double_writer = initializeWriter(path + "/" + prefix + "floats.txt", append);
        				
        				if (double_writer != null)
        					tryWrite(double_writer, str);
        				
        				if (show_statistics) {
        					double_cnt++;
        				}
        				
        				if (show_verbose_statistics) {
        					double_cnt++;
        					
        					double number = Double.parseDouble(str);
        					
        					if (double_cnt == 1 || number < double_min) 
        						double_min = number;
        					
        					if (double_cnt == 1 || number > double_max) 
        						double_max = number;
        					
        					double_sum += number;
        				} 
        				
        			} else {
        				if (str_writer == null) 
        					str_writer = initializeWriter(path + "/" + prefix + "strings.txt", append);
        				
        				if(str_writer != null)
        					tryWrite(str_writer, str);
        				
        				if (show_statistics) {
        					str_cnt++;
        				}
        				
        				if (show_verbose_statistics) {
        					str_cnt++;
        					
        					if (str_cnt == 1 || str.length() < str_min) 
        						str_min = str.length();
        					
        					if (str_cnt == 1 || str.length() > str_max) 
        						str_max = str.length();
        				}
        			}
        			
        		}
        	} catch (IOException e) {
        		System.out.println("Ошибка во время чтения файла " + file);
        	}
    	}
 
    	if (long_writer != null) 
    		closeWriter(long_writer);
    	
    	if (double_writer != null) 
    		closeWriter(double_writer);
    	
    	if (str_writer != null) 
    		closeWriter(str_writer);
    	
    	
    	if (show_statistics) {
    		System.out.println("Количество целых чисел: " + long_cnt);
    		System.out.println("Количество вещественных чисел: " + double_cnt);
    		System.out.println("Количество строк: " + str_cnt);
    	}
    	
    	if (show_verbose_statistics) {
    		System.out.println("Статистика по целых числам: ");
    		System.out.println("    Количество: " + long_cnt);
    		if (long_cnt > 0) {
	    		System.out.println("    Максимум: " + long_max);
	    		System.out.println("    Минимум: " + long_min);
	    		System.out.println("    Сумма: " + long_sum);
	    		System.out.println("    Среднее: " + long_sum * 1.0 / long_cnt);
    		}

    		System.out.println("Статистика по вещественным числам: ");
    		System.out.println("    Количество: " + double_cnt);
    		if (double_cnt > 0) {
	    		System.out.println("    Максимум: " + double_max);
	    		System.out.println("    Минимум: " + double_min);
	    		System.out.println("    Сумма: " + double_sum);
	    		System.out.println("    Среднее: " + double_sum / double_cnt);
    		}
    		
    		System.out.println("Статистика по строкам: ");
    		System.out.println("    Количество: " + str_cnt);
    		if (str_cnt > 0) {
	    		System.out.println("    Максимальная длина: " + str_max);
	    		System.out.println("    Минимальная длина: " + str_min);
    		}
    	}
    	
    }

}
