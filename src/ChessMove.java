 

public class ChessMove extends Move implements java.io.Serializable{
    public int from;
    public int to;
	
	public String toString(){
		int f1 = ((from % 10));
		int f2 = (from - f1);
		int t1 = ((to % 10));
		int t2 = (to - t1);
		
		String fa = getLetter(f1-1);
		String fb = getNumber(f2);
		String ta = getLetter(t1-1);
		String tb = getNumber(t2);
		return ChessMainFrame.getString("from") + fa+fb + ChessMainFrame.getString("to") + ta+tb;
	}
	
	private String getLetter(int n){
		String k;
		switch(n){
                    case 1: k = "A"; break;
                    case 2: k = "B"; break;
                    case 3: k = "C"; break;
                    case 4: k = "D"; break;
                    case 5: k = "E"; break;
                    case 6: k = "F"; break;
                    case 7: k = "G"; break;
                    case 8: k = "H"; break;
		   default: k = "Err"; break;
		}		
		return k;
	}
	
	private String getNumber(int n){
		String k;
		switch((n/10)-1){
			case 1: k = "1"; break;
			case 2: k = "2"; break;
			case 3: k = "3"; break;
			case 4: k = "4"; break;
			case 5: k = "5"; break;
			case 6: k = "6"; break;
			case 7: k = "7"; break;
			case 8: k = "8"; break;
		   default: k = "Err"; break;
		}		
		return k;		
	}
}
