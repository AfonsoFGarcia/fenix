package ServidorAplicacao.utils.smsResponse;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DataBeans.ISmsDTO;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.utils.exceptions.SmsCommandConfigurationException;
import Util.sms.SmsCommandAuthenticationType;
import framework.factory.ServiceManagerServiceFactory;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 *  
 */
public class SmsCommand {
    private class VariableInformation {
        private int variablePosition;

        private String variableType;

        public VariableInformation(int variablePosition, String variableType) {
            this.variablePosition = variablePosition;
            this.variableType = variableType;
        }

        public int getVariablePosition() {
            return variablePosition;
        }

        public String getVariableType() {
            return variableType;
        }

    }

    private Pattern expression;

    private HashMap variablesInformation;

    private boolean replyToSender;

    private String serviceName;

    private String serviceArgs[];

    private SmsCommandAuthenticationType smsCommandAuthenticationType;

    public SmsCommand(String expressionString, boolean replyToSender, String serviceName,
            String serviceArgs[], String authenticationType) throws SmsCommandConfigurationException {

        this.serviceName = serviceName;
        this.serviceArgs = serviceArgs;
        this.replyToSender = replyToSender;
        this.variablesInformation = new HashMap();

        // add reserved keyword FULLSMSTEXT
        this.variablesInformation.put(SmsCommandExpressionConstants.FULL_SMS_TEXT_KEYWORD,
                new VariableInformation(0, SmsCommandExpressionConstants.WORD_TYPE));

        checkIfKeywordsExistInExpression(expressionString);

        for (int i = 0; i < serviceArgs.length; i++) {
            String variableName = serviceArgs[i];

            if ((variableName.equals(SmsCommandExpressionConstants.FULL_SMS_TEXT_KEYWORD))
                    || variableName.equals(SmsCommandExpressionConstants.SENDER_MSISDN_KEYWORD)) {
                continue;
            }

            String variableInExpression = "${" + variableName + ":";

            //check the existence of the variable in expression
            int variableStartIndex = expressionString.indexOf(variableInExpression);
            if (variableStartIndex == -1) {
                throw new SmsCommandConfigurationException("Variable " + variableName
                        + " does not exist in expression: " + expressionString);
            }

            int variableEndIndex = expressionString.indexOf("}", variableStartIndex);

            //check duplicate variables in expression
            int nextIndexOfVariable = expressionString.indexOf(variableInExpression, variableEndIndex);
            if (nextIndexOfVariable != -1) {
                throw new SmsCommandConfigurationException("Variable " + variableName
                        + " is duplicated in expression" + expressionString);
            }

        }

        this.smsCommandAuthenticationType = SmsCommandAuthenticationType.getEnum(authenticationType);

        if (this.smsCommandAuthenticationType == null) {
            throw new SmsCommandConfigurationException("Unknown authentication type in command: "
                    + expressionString);
        }

        buildRegexAndVariableInformation(expressionString);
        checkRequirementsForAuthenticationType(expressionString);
    }

    /**
     * @param expressionString
     * @throws SmsCommandConfigurationException
     */
    private void checkIfKeywordsExistInExpression(String expressionString)
            throws SmsCommandConfigurationException {

        String[] keywords = { SmsCommandExpressionConstants.FULL_SMS_TEXT_KEYWORD,
                SmsCommandExpressionConstants.SENDER_MSISDN_KEYWORD };

        for (int i = 0; i < keywords.length; i++) {
            String keyword = keywords[i];

            int indexOfReservedWord = expressionString.indexOf("${" + keyword + ":");
            
            if (indexOfReservedWord != -1) {
                throw new SmsCommandConfigurationException("In expression " + expressionString + " => "
                        + keyword + " cannot be used as a variable name because its a reserved keyword");
            }

        }

    }

    /**
     * @throws SmsCommandConfigurationException
     *  
     */
    private void checkRequirementsForAuthenticationType(String expressionString)
            throws SmsCommandConfigurationException {

        if (this.smsCommandAuthenticationType.equals(SmsCommandAuthenticationType.NONE)) {
            //no requirements to check

        } else if (this.smsCommandAuthenticationType.equals(SmsCommandAuthenticationType.USER_PASS)) {

            //check USERNAME and PASSWORD variables in expression
            VariableInformation usernameVariableInformation = (VariableInformation) this.variablesInformation
                    .get(SmsCommandExpressionConstants.USERNAME_VARIABLE);
            VariableInformation passwordVariableInformation = (VariableInformation) this.variablesInformation
                    .get(SmsCommandExpressionConstants.PASSWORD_VARIABLE);

            if ((usernameVariableInformation == null)
                    || (!usernameVariableInformation.getVariableType().equals(
                            SmsCommandExpressionConstants.WORD_TYPE))
                    || (passwordVariableInformation == null)
                    || (!passwordVariableInformation.getVariableType().equals(
                            SmsCommandExpressionConstants.WORD_TYPE)))

                throw new SmsCommandConfigurationException("Authentication type chosen for expression: "
                        + expressionString
                        + " requires definition of <USERNAME:word> and <PASSWORD:word> variables.");

        }
        /*
         * else if
         * (this.smsCommandAuthenticationType.equals(SmsCommandAuthenticationType.MSISDN_PASS)) {
         * 
         * //check PASSWORD variable in expression VariableInformation
         * passwordVariableInformation = (VariableInformation)
         * this.variablesInformation
         * .get(SmsCommandExpressionConstants.PASSWORD);
         * 
         * if ((passwordVariableInformation == null) ||
         * (!passwordVariableInformation.getVariableType().equals(
         * SmsCommandExpressionConstants.WORD_TYPE))) {
         * 
         * throw new SmsCommandConfigurationException("Authentication type
         * chosen for expression: " + expressionString + " requires definition
         * of <PASSWORD:word> variable."); } }
         */

    }

    private void buildRegexAndVariableInformation(String expressionString)
            throws SmsCommandConfigurationException {

        StringBuffer regex = new StringBuffer();
        int nextVariableIndex = 1;

        if (expressionString.length() == 0) {
            this.expression = Pattern.compile("");
            return;
        }

        regex.append(SmsCommandExpressionConstants.BEGIN_LINE_REGEX);
        regex.append("(");

        for (int i = 0; i < expressionString.length(); i++) {
            if (expressionString.charAt(i) == '$') {
                if ((i < expressionString.length() - 1) && (expressionString.charAt(i + 1) == '{')) {
                    if ((i > 0) && (expressionString.charAt(i - 1) != ' ')) {
                        regex.append(")");
                        regex.append("(");
                        nextVariableIndex++;
                    }
                    int variableNameStartIndex = i + 2;
                    int variableNameEndIndex = expressionString.indexOf(":", variableNameStartIndex);

                    if (variableNameEndIndex == -1) {
                        //throw new
                        // SmsCommandConfigurationException("Expression contains
                        // invalid
                        // characters");
                        throw new SmsCommandConfigurationException("Expression " + expressionString
                                + " contains unclosed variable declarations (missing ':' separator)");
                    }

                    //we found a variable
                    String variableName = expressionString.substring(variableNameStartIndex,
                            variableNameEndIndex);

                    if (variableName.length() == 0) {
                        throw new SmsCommandConfigurationException(
                                "Variable names must have at least one character");
                    }

                    // determine variable type and build regex
                    int variableTypeStartIndex = variableNameEndIndex + 1;
                    int variableTypeEndIndex = expressionString.indexOf("}", variableTypeStartIndex);

                    if (variableTypeEndIndex == -1) {
                        throw new SmsCommandConfigurationException("Expression " + expressionString
                                + " contains unclosed variable declarations (missing '}' terminator)");
                    }

                    String variableType = expressionString.substring(variableTypeStartIndex,
                            variableTypeEndIndex);

                    VariableInformation variableInformation = new VariableInformation(nextVariableIndex,
                            variableType);

                    if (this.variablesInformation.containsKey(variableName)) {
                        //duplicate variable name
                        throw new SmsCommandConfigurationException("Variable " + variableName
                                + " already exists in expression");
                    }

                    this.variablesInformation.put(variableName, variableInformation);

                    String variableTypeRegex = computeTypeRegex(variableType);
                    if (variableTypeRegex == null) {
                        String errorMessage = variableType.length() != 0 ? "Unknown type "
                                + variableType + " for variable " + variableName + " in expression "
                                + expressionString : "Variable " + variableName + " in expression "
                                + expressionString + " must have a type";

                        throw new SmsCommandConfigurationException(errorMessage);
                    }

                    regex.append(variableTypeRegex);
                    i = variableTypeEndIndex;

                }

            } else if (expressionString.charAt(i) == ' ') {
                nextVariableIndex++;
                regex.append(")");
                regex.append(SmsCommandExpressionConstants.SPACE_REGEX);
                regex.append("(");

            } else {
                String charToAdd = expressionString.charAt(i) == '.' ? "\\." : ""
                        + expressionString.charAt(i);
                regex.append(charToAdd);
            }

        }

        regex.append(")");
        regex.append(SmsCommandExpressionConstants.END_LINE_REGEX);
        this.expression = Pattern.compile(regex.toString());
    }

    /**
     * @param variableType
     * @return
     */
    private static String computeTypeRegex(String variableType) {
        if (variableType.equals(SmsCommandExpressionConstants.INTEGER_TYPE)) {
            return SmsCommandExpressionConstants.INTEGER_NUMBER_REGEX;
        } else if (variableType.equals(SmsCommandExpressionConstants.DECIMAL_TYPE)) {
            return SmsCommandExpressionConstants.DECIMAL_NUMBER_REGEX;
        } else if (variableType.equals(SmsCommandExpressionConstants.WORD_TYPE)) {
            return SmsCommandExpressionConstants.WORD_REGEX;
        } else if (variableType.equals(SmsCommandExpressionConstants.STRING_TYPE)) {
            return SmsCommandExpressionConstants.STRING_REGEX;
        } else {
            return null;
        }

    }

    /**
     * @param senderMsisdn
     *            MSISDN of the message sender
     * @param smsText
     *            The command to handle
     * 
     * @return boolean indicating if could handle the command
     */
    public boolean handleCommand(String senderMsisdn, String smsText) {
        Matcher matcher = expression.matcher(smsText);

        if (matcher.matches() == false) {
            // the smsText command provided does not match this command
            return false;
        }

        Object args[] = computeServiceArgs(senderMsisdn, matcher);

        try {

            UserView userView = null;
            if (this.smsCommandAuthenticationType.equals(SmsCommandAuthenticationType.NONE) == false) {

                VariableInformation password = (VariableInformation) this.variablesInformation
                        .get(SmsCommandExpressionConstants.PASSWORD_VARIABLE);
                Object[] authenticationArgs = new Object[2];

                authenticationArgs[0] = matcher.group(password.variablePosition);

                if (this.smsCommandAuthenticationType.equals(SmsCommandAuthenticationType.USER_PASS)) {

                    VariableInformation username = (VariableInformation) this.variablesInformation
                            .get(SmsCommandExpressionConstants.USERNAME_VARIABLE);

                    authenticationArgs[1] = matcher.group(username.variablePosition);

                }
                /*
                 * else {
                 * 
                 * //we have to authenticate using msisdn and password Object[]
                 * argsReadUsername = { senderMsisdn }; String username =
                 * (String) ServiceManagerServiceFactory.executeService(null,
                 * "ReadUsernameByMobilePhone", argsReadUsername);
                 * authenticationArgs[1] = username; }
                 */

                userView = (UserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao",
                        authenticationArgs);

            }

            Object result = ServiceManagerServiceFactory
                    .executeService(userView, this.serviceName, args);

            if ((result != null) && (this.replyToSender == true)) {

                // billing model should be inserted here
                //(depends of who is going to charge the response sms money?
                // Fenix or Mobile Operator?)
                ISmsDTO smsDTO = (ISmsDTO) result;
                System.out.println("RESPONSE: " + smsDTO.toSmsText());
            }

        } catch (FenixServiceException e) {

            // ignore exception because we don't want the sender to pay this
            // service
            // depending on the billing model, we might want or not to send the
            // sms back
        }

        return true;

    }

    private Object[] computeServiceArgs(String senderMsisdn, Matcher matcher) {
        Object[] args = new Object[this.serviceArgs.length];

        for (int i = 0; i < this.serviceArgs.length; i++) {
            String variableName = this.serviceArgs[i];

            if (variableName.equals(SmsCommandExpressionConstants.SENDER_MSISDN_KEYWORD)) {

                args[i] = senderMsisdn;

            } else {

                VariableInformation variableInformation = (VariableInformation) this.variablesInformation
                        .get(variableName);
                args[i] = createArgument(variableInformation, matcher);
            }
        }

        return args;
    }

    private Object createArgument(VariableInformation variableInformation, Matcher matcher) {
        Object arg = null;

        if ((variableInformation.getVariableType().equals(SmsCommandExpressionConstants.WORD_TYPE))
                || (variableInformation.getVariableType()
                        .equals(SmsCommandExpressionConstants.STRING_TYPE))) {
            arg = new String(matcher.group(variableInformation.getVariablePosition()));

        } else if (variableInformation.getVariableType().equals(
                SmsCommandExpressionConstants.INTEGER_TYPE)) {
            arg = new Integer(matcher.group(variableInformation.getVariablePosition()));

        } else if (variableInformation.getVariableType().equals(
                SmsCommandExpressionConstants.DECIMAL_TYPE)) {
            arg = new Float(matcher.group(variableInformation.getVariablePosition()));

        }

        return arg;
    }

}