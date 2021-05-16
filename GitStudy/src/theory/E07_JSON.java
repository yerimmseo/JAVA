package theory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class E07_JSON {

	/*
		# JSON
		- JavaScript의 Object타입과 굉장히 유사한 형태의 데이터 표현 방식
		- 문자열만으로 다양한 데이터를 간결하게 표현할 수 있어서 인기가 많다
		- { "Key" : Value, "Key" : Value, ... }
		
		# JSON의 Value에 표현 가능한 타입들
		- '', "" : String
		- 숫자, 소수 : Number
		- [] : Array
		- {} : Object (JAVA의 Map)
	*/
	File fileMap = new File("./data/myMap.json");
	
	public String mapToJsonStr(HashMap map) {
		HashMap<String, Object> myMap = map;
		StringBuilder str = new StringBuilder();
		
		for (String key : myMap.keySet()) {
			Object value = myMap.get(key);
			
			if (value instanceof Object[]) {
				Object[] arr = (Object[])value;
				
				str.append("\"" + key + "\"" + ": [");
				for (int i = 0; i < arr.length; i++) {
					if (arr[i] instanceof String) {
						if (i == arr.length - 1) {
							str.append("\"" + arr[i] + "\"");
						} else {
							str.append("\"" + arr[i] + "\"" + ", ");
						}
					} else {
						if (i == arr.length - 1) {
							str.append(arr[i]);
						} else {
							str.append(arr[i] + ", ");
						}
					}
				}
				str.append("],\n");
			} else if (value instanceof String) {
				str.append("\"" + key + "\"" + ": " + "\"" + value + "\"" + ",\n");
			} else {
				str.append("\"" + key + "\"" + ": " + value + ",\n");
			}
		}
		return str.toString();
	}
	
	public void mapToJsonFile(HashMap myMap) {
		String str = mapToJsonStr(myMap);
		
		try {
			FileOutputStream fout = new FileOutputStream(fileMap);
			DataOutputStream dout = new DataOutputStream(fout);
			
			dout.writeUTF(str);
			
			dout.close();
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, Object> fileToMap() {
		HashMap<String, Object> myMap = new HashMap<>();
		ArrayList<String> keyList = new ArrayList<>();
		ArrayList<String> valueList = new ArrayList<>();

		try {
			FileInputStream fin = new FileInputStream(fileMap);
			DataInputStream din = new DataInputStream(fin);
			
			String str = din.readUTF();
//			System.out.println(str);
			
			Pattern regexOfKey = Pattern.compile("[\\w]*\":");
			Pattern regexOfValue = Pattern.compile(":\s\\[?[가-힣-\\w\"\s,]*\\]?,");
			Matcher matchKey = regexOfKey.matcher(str);
			Matcher matchValue = regexOfValue.matcher(str);
			
			while (matchKey.find()) {
				String[] keys = matchKey.group().split("\":");
				
				for (String key : keys) {
					keyList.add(key);
				}
			}
			
			while (matchValue.find()) {
				String[] values = matchValue.group().split(":\s"); 
				
				for (String value : values) {
					if (!value.isBlank()) {
						valueList.add(value.substring(0, value.length()-1));
					}
				}
			}
			
			for (int i = 0; i < keyList.size(); i++) {
				String value = valueList.get(i);
				String key = keyList.get(i);
				
				if (value.contains("[") && value.contains("]") && value.contains("\"")) {
					Pattern regexStr = Pattern.compile("[\\w]+");
					Matcher matchStr = regexStr.matcher(value);
					
					ArrayList<String> strList = new ArrayList<>();
					
					int count = 0;
					while (matchStr.find()) {
						strList.add(matchStr.group());
						count++;
					}
					
					String[] strArr = new String[count];
					
					for (int j = 0; j < count; j++) {
						strArr[j] = strList.get(j);
					}
					myMap.put(key, strArr);
				} else if (value.contains("[") && value.contains("]")) {
					Pattern regexOfInt = Pattern.compile("[0-9]+");
					Matcher matchInt = regexOfInt.matcher(value);
					
					ArrayList<Integer> intList = new ArrayList<>();
					
					int count = 0;
					while (matchInt.find()) {
						Integer num = Integer.parseInt(matchInt.group());
						intList.add(num);
						count++;
					}
					
					Integer[] intArr = new Integer[count];
					
					for (int j = 0; j < count; j++) {
						intArr[j] = intList.get(j);
					}
					myMap.put(key, intArr);
				} else if (value.contains("\"")){
					myMap.put(key, value.substring(1, value.length() - 1));
				} else {
					myMap.put(key, value);
				}
			}
			
			for (String key : myMap.keySet()) {
				Object value = myMap.get(key);
				
				if (value instanceof Object[]) {
					Object[] arr = (Object[])value;
					
					System.out.print("\"" + key + "\"" + ": [");
					for (int i = 0; i < arr.length; i++) {
						if (arr[i] instanceof String) {
							if (i == arr.length - 1) {
								System.out.print("\"" + arr[i] + "\"");
							} else {
								System.out.print("\"" + arr[i] + "\"" + ", ");
							}
						} else {
							if (i == arr.length - 1) {
								System.out.print(arr[i]);
							} else {
								System.out.print(arr[i] + ", ");
							}
						}
					}
					System.out.print("],\n");
				} else if (value instanceof String) {
					System.out.print("\"" + key + "\"" + ": " + "\"" + value + "\"" + ",\n");
				} else {
					System.out.print("\"" + key + "\"" + ": " + value + ",\n");
				}
			}
			
			
			din.close();
			fin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myMap;
	}
	
	public static void main(String[] args) {
		
		// myMap에 저장된 데이터들을 JASON형태의 문자열로 만들어 파일에 출력하는 메서드를 만들어보세요
		// (확장자는 .json)
		HashMap<String, Object> myMap = new HashMap<>();
		
		myMap.put("name", "홍길동");
		myMap.put("age", 123);
		myMap.put("tel", "010-1234-1234");
		myMap.put("tel2", null);
		myMap.put("language", new String[] {"C", "JAVA", "JavaScript", "React"});
		myMap.put("lotto", new Integer[] {1, 2, 3, 4, 5, 6, 7});
		System.out.println(myMap.entrySet());
		
		E07_JSON json = new E07_JSON();
		json.mapToJsonFile(myMap);

		// 작성된 파일의 내용을 다시 JAVA 맵으로 로드하는 메서드를 만들어보세요
		HashMap<String, Object> testMap = json.fileToMap();
		System.out.println(testMap.entrySet());
		
	}

}












