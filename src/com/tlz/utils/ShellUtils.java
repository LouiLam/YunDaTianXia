package com.tlz.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.util.Log;

public class ShellUtils {

	private static final String TAG = ShellUtils.class.getSimpleName();
	
    public static final String COMMAND_SU       = "su";
    public static final String COMMAND_SH       = "sh";
    public static final String COMMAND_EXIT     = "exit\n";
    public static final String COMMAND_LINE_END = "\n";
    
    public static boolean checkRootPermission() {
        return execCommand("echo root", true, false).result == 0;
    }

    public static CommandResult execCommand(String command, boolean isRoot) {
        return execCommand(new String[] { command }, isRoot, true);
    }

    public static CommandResult execCommand(List<String> commands, boolean isRoot) {
        return execCommand(commands == null ? null : commands.toArray(new String[] {}), isRoot, true);
    }
    
    public static CommandResult execCommand(String[] commands, boolean isRoot) {
        return execCommand(commands, isRoot, true);
    }

    public static CommandResult execCommand(String command, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(new String[] { command }, isRoot, isNeedResultMsg);
    }

    public static CommandResult execCommand(List<String> commands, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(commands == null ? null : commands.toArray(new String[] {}), isRoot, isNeedResultMsg);
    }

    public static CommandResult execCommand(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }
                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s = null;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } finally {
        	IOUtils.close(os, successResult, errorResult, process);
        }
        return new CommandResult(result, String.valueOf(successMsg), String.valueOf(errorMsg));
    }

    public static class CommandResult {

        public int    result;
        public String successMsg;
        public String errorMsg;

        public CommandResult(int result){
            this.result = result;
        }

        public CommandResult(int result, String successMsg, String errorMsg){
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }
    
    public static boolean ping(String address){
        try { 
            Process p = Runtime.getRuntime().exec("ping -c 1 -w 100 " + address); 
            int status = p.waitFor();
            if (status == 0) {
            	return true;
            }
        } catch (Exception e) {
        	Flog.e("ping error!");
        }
        return false;
    }
    
//    public static boolean isRoot() {
//        String binPath = "/system/bin/su";
//        String xBinPath = "/system/xbin/su";
//        if (new File(binPath).exists() && isExecutable(binPath)) {
//            return true;
//        }
//        
//        if (new File(xBinPath).exists() && isExecutable(xBinPath)) {
//            return true;
//        }
//        return false;
//    }
//    
//    private static boolean isExecutable(String filePath) {
//        Process p = null;
//        try {
//            p = Runtime.getRuntime().exec("ls -l " + filePath);
//            // 获取返回内容
//            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            String str = in.readLine();
//            if (str != null && str.length() >= 4) {
//                char flag = str.charAt(3);
//                if (flag == 's' || flag == 'x')
//                    return true;
//            }
//        } catch (IOException e) {
//            Flog.e(e);
//        }finally{
//            if(p!=null){
//                p.destroy();
//            }
//        }
//        return false;
//    }
    
    private ShellUtils() {/*Do not new me*/}
}
