package net.sourceforge.fenixedu.util.kerberos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import net.sourceforge.fenixedu._development.Custodian;
import net.sourceforge.fenixedu._development.LogLevel;
import pt.ist.bennu.core.util.ConfigurationManager;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.StringAppender;

public class Script {

    private static String createCommand(final String scriptName, final String user) {
        final String script = ConfigurationManager.getProperty(scriptName);
        return StringAppender.append(script, " ", user);
    }

    public static void runCommand(final String commandString, final String user, final String pass) throws KerberosException,
            ExcepcaoPersistencia {
        long time1 = System.currentTimeMillis();
        final String command = createCommand(commandString, user);
        long time2 = System.currentTimeMillis();
        ScriptResult scriptResult = runCmd(command, pass);
        long time3 = System.currentTimeMillis();
        outputRunCommandTime(commandString, time1, time2, time3);

        if (scriptResult.getExitCode() == -1) {
            throw new ExcepcaoPersistencia(scriptResult.getReturnCode());
        } else {
            if (scriptResult.getExitCode() == 1) {
                throw new KerberosException(scriptResult.getExitCode(), scriptResult.getReturnCode());
            }
        }
    }

    private static void outputRunCommandTime(final String commandString, final long time1, final long time2, final long time3) {
        if (LogLevel.INFO) {
            System.out.print("Command ");
            System.out.print(commandString);
            System.out.print(" took ");
            System.out.print(time2 - time1);
            System.out.print(" + ");
            System.out.print(time3 - time2);
            System.out.print(" = ");
            System.out.print(time3 - time1);
            System.out.print(" (ms)");
        }
    }

    public static DateTime returnExpirationDate(String user) throws KerberosException, ExcepcaoPersistencia {
        final String command = createCommand("passExpirationScript", user);
        final ScriptResult scriptResult = runCmd(command, "");

        if (scriptResult.getExitCode() == -1) {
            throw new ExcepcaoPersistencia(scriptResult.getReturnCode());
        } else {
            if (scriptResult.getExitCode() == 1) {
                throw new KerberosException(scriptResult.getExitCode(), scriptResult.getReturnCode());
            }
        }

        String dateString = scriptResult.getReturnCode();
        return new DateTime(Long.valueOf(dateString) * 1000);
    }

    public static void changeKerberosPass(String user, String pass) throws KerberosException, ExcepcaoPersistencia {
        System.out.println("Calling change password script.");
        runCommand("changePassScript", user, pass);
    }

    public static void createUser(String user, String pass) throws KerberosException, ExcepcaoPersistencia {
        System.out.println("Calling create user script.");
        runCommand("createUserScript", user, pass);
    }

    public static void verifyPass(String user, String pass) throws KerberosException, ExcepcaoPersistencia {
        System.out.println("Calling verify password script.");
        runCommand("verifyPassScript", user, pass);
    }

    private static ScriptResult runCmd(String cmd, String pass) {
        Process process = null;
        BufferedWriter outCommand = null;
        BufferedReader bufferedReader = null;
        ScriptWatchDog watchDog = new ScriptWatchDog(Float.valueOf(ConfigurationManager.getProperty("scriptTimeout")).longValue());

        try {
            process = Runtime.getRuntime().exec(cmd);
            outCommand = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            outCommand.write(pass);
            outCommand.newLine();
            outCommand.flush();

            watchDog.start(process);
            int exitCode = process.waitFor();
            watchDog.stop();
            String returnCode;
            if (watchDog.killedProcess()) {
                exitCode = -1;
                returnCode = "Timeout";
            } else {
                returnCode = bufferedReader.readLine();
                if (exitCode != 1 && exitCode != 0) {
                    exitCode = -1;
                }
            }
            return new ScriptResult(exitCode, returnCode);

        } catch (IOException ex) {
            System.out.println(Runtime.getRuntime().freeMemory());
            System.out.println(Runtime.getRuntime().totalMemory());
            ex.printStackTrace();
            try {
                Custodian.dumpThreadTrace();
            } catch (Exception e) {
                // do nothing. no log will be produced.
            }
            return new ScriptResult(-1, ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ScriptResult(-1, ex.getMessage());
        } finally {
            try {
                if (outCommand != null) {
                    outCommand.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (process != null) {
                    process.destroy();
                }
                watchDog.stop();
            } catch (Exception e) {

            }
        }
    }

    private static class ScriptResult {
        private int exitCode;

        private String returnCode;

        public ScriptResult(int exitCode, String returnCode) {
            this.exitCode = exitCode;
            this.returnCode = returnCode;
        }

        public int getExitCode() {
            return exitCode;
        }

        public void setExitCode(int exitCode) {
            this.exitCode = exitCode;
        }

        public String getReturnCode() {
            return returnCode;
        }

        public void setReturnCode(String returnCode) {
            this.returnCode = returnCode;
        }
    }
}
