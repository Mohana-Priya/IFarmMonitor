package util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecutor {
	public static void executeUnixCommand(String cmd,StringBuffer sb) throws IOException{
		System.out.println("Executing cmd:--> "+cmd);
		Process p;
		if(cmd.contains("|")){
			FileWriter fw = new FileWriter("/net/adc6140270/scratch/mattulur/IFarmMonitor/testscript.sh", false);
			fw.write(cmd);
			fw.close();
			p = Runtime.getRuntime().exec("/net/adc6140270/scratch/mattulur/IFarmMonitor/testscript.sh");
		} else {
			p = Runtime.getRuntime().exec(cmd);
		}
		
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String s;
        while ((s=stdInput.readLine()) != null) {                                   
        	sb.append(s);
        	sb.append("\n");
        }
        
        if(sb.lastIndexOf("\n")!= -1){
        	sb.deleteCharAt(sb.lastIndexOf("\n"));
        }
        while ((s = stdError.readLine()) != null) {
            sb.append(s+"\n");
        }		
	}
}
