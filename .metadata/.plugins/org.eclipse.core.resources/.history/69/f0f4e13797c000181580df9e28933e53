
public class HandleTokenizer {
	//function to generate the MySQL search sentence segment in boolean mode full-text search
	public static String tokenizer(String original){
		String[] tmp = new String[5];
		tmp = original.replaceAll("(\\W)", " ").trim().split("\\s+");
		for (String i : tmp) {
			System.out.print(i + " ");
		}
		System.out.println(" ");
		
		StringBuffer ft = new StringBuffer();
		for(int i=0; i<tmp.length; i++) {
			if(tmp[i].length() <= 2) {
				ft.append(tmp[i]).append("* ");
			} else {
				ft.append("+").append(tmp[i]).append("* ");
			}		
		}
		
		String ftSearch = ft.toString();
		return ftSearch;
	}
}
