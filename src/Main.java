
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import com.kosta.opn.Calc;
class StringTokenizerPlusHT{
private HashMap <String,Var> mapForVarClasses=null;
private StringTokenizer st=null;
private StringBuilder sb=null;
private RegExp re=null;
    public StringTokenizerPlusHT(HashMap <String,Var> mapForVarClasses) {
        this.mapForVarClasses=mapForVarClasses;
        sb=new StringBuilder();
        re=new RegExp();
        
    }
    public String getValueString(String sIn,String delimeters){
     st=new StringTokenizer(sIn,delimeters,true);
    
    while(st.hasMoreTokens()){
    String stringTmp=st.nextToken();
    if(!stringTmp.equals("[")&!stringTmp.equals("]")){
    if(re.test(stringTmp,"^%\\w+%$")){//in Used Vars//
           String str=stringTmp.substring(1,stringTmp.length()-1);
                       
            Var myVar=(Var)mapForVarClasses.get(str);
            sb.append(myVar.getValue());
            
        }else{
                    sb.append(stringTmp);
                }
    }
    }

    
return sb.toString();

}
}
class ExpParser{
    HashMap  mapForVarClasses=null;

    public ExpParser() {
        mapForVarClasses=new HashMap<String,Var>();
    }
    
    public void oper(String sIn){
RegExp re=new RegExp(); 
 StringTokenizer st=new StringTokenizer(sIn,"= ",true);//string tokenizer dlya stroki expression
        System.out.println(sIn);
        if(sIn.substring(0,3).equals("set")){//esli sIn eto "set"
            Var var=new Var();
            while(st.hasMoreTokens()){//cicl string tokenizer dlya Vars to set
String StringStmp=st.nextToken();

if(re.test(StringStmp,"^\\d+$") || re.test(StringStmp,"^\\d+\\.\\d+$"))
{
    
    var.setValue(StringStmp);//<<< set int/float
}
else if(re.test(StringStmp,"^i_\\w+$")){
            
            var.setIndificator(StringStmp);//<<< set Indificator
        }else if(re.test(StringStmp,"^s_\\w+$")){
            
            var.setValue(StringStmp);//<<< set String
        }

else if(StringStmp.equals("int") || StringStmp.equals("float")|| StringStmp.equals("double")|| StringStmp.equals("String")|| StringStmp.equals("char")|| StringStmp.equals("boolean")){
            
            var.setType(StringStmp); //<<< set Type<<<
        }


    }
            mapForVarClasses.put(var.getIndificator(), var);
            

    }else if(sIn.substring(0,6).equals("mat_op")){//<<<Mat expression
            //System.out.println("mat op: "+sIn);
            Calc myCalc=new Calc();
            String stringS=sIn.substring(6,sIn.length());
            StringTokenizerPlusHT stpht=new StringTokenizerPlusHT(mapForVarClasses);
            String sCalc=stpht.getValueString(stringS,"[]()+-*/^");
            //System.out.println(sCalc);
             try{
                 System.out.println(sIn+"="+sCalc+"="+myCalc.calculate(myCalc.opn(sCalc)));
    }
    catch(Exception e){
        javax.swing.JOptionPane.showMessageDialog(null, e);
    }

            //System.out.println(stpht.getValueString(stringS,"[]()+-*^"));
            
            
            
    }
        else if(re.test(sIn,"^%\\w+%$")){//in Used Vars//
            
            String str=sIn.substring(1,sIn.length()-1);
            System.out.println("***********");
            System.out.println(str);//get Vars
            System.out.println("***********");
            
            Var myVar=(Var)mapForVarClasses.get(str);
            System.out.println("))))))))))))))");
            System.out.println(sIn+"="+myVar.getValue());
            System.out.println(")))))))))))))))");
        }
        else{                              //<<<in Function:this is arguments in Function//
            System.out.println("function:"+sIn);
            StringTokenizer st_w=new StringTokenizer(sIn,"()",true);
            while(st_w.hasMoreTokens()){
String StringStmp=st_w.nextToken();
if(re.test(StringStmp,"^\\%\\w+\\%$")){
            ///***System.out.println("Use Var:"+StringStmp);
            String str=StringStmp.substring(1,StringStmp.length()-1);
            System.out.println("***********");
            System.out.println(str); //get Vars
            System.out.println("***********");
            
            Var myVar=(Var)mapForVarClasses.get(str);
            System.out.println("))))))))))))))");
            System.out.println(StringStmp+"="+myVar.getValue());
            System.out.println("))))))))))))))");
            
}

              


        }
}
        
}

    @Override
    public String toString() {
        return "ExpParser{" + "mapForVarClasses=" + mapForVarClasses + '}';
    }
    
}




class Main  {
public static void main(String args[]){
    String arg="";
    if(args.length!=0){
     arg=args[0];   
      
    }else{
        System.out.println("ussage Main <file>");
    }
    File f=new File(arg);
    ReadBytes rb=new ReadBytes(f,"cp1251");
    ArrayList l=rb.read(';');
    ExpParser ep=new ExpParser();
    Iterator<String> it=l.iterator();
    while(it.hasNext()){
        String str=it.next();
        ep.oper(str);////// esli eto \r\n t e 2 to 1arg=2+3 2arg=2+1/////////
    }
    //System.out.println(ep);

}
}