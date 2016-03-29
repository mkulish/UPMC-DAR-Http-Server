package edu.upmc.dar.server.util;

import edu.upmc.dar.server.Main;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class TemplateParserUtil {
    public static File compileTemplate(File templateFile, String path, String appName, String packageName, String templateClassName) throws Exception {
        File srcFile = new File(Main.APPS_FOLDER_PATH + File.separator + appName + File.separator + path + File.separator + templateClassName + ".java");
        PrintWriter srcCode = new PrintWriter(srcFile);

        StringBuilder template = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(templateFile)));
        String line;
        while((line = in.readLine()) != null){
            template.append(line).append("\n");
        }
        in.close();

        List<String> imports = new LinkedList<>();
        String parsedContent = processTemplate(template.toString(), imports);

        srcCode.println("package " + packageName + ";");
        srcCode.println();

        srcCode.println("import java.util.*;");

        srcCode.println("import edu.upmc.dar.server.view.Template;");
        srcCode.println("import edu.upmc.dar.server.model.Model;");
        srcCode.println("import edu.upmc.dar.server.http.request.HttpRequest;");
        if(! imports.isEmpty()) {
            srcCode.println();
            for (String importClass : imports) {
                srcCode.println("import " + importClass + ";");
            }
        }
        srcCode.println();

        srcCode.println("public class " + templateClassName + " extends Template {");
        srcCode.println("\t@SuppressWarnings(\"unchecked\")");
        srcCode.println("\t@Override");
        srcCode.println("\tpublic String doView(Model model, HttpRequest request) {");
        srcCode.println("\t\tStringBuilder out = new StringBuilder();");

        srcCode.println(parsedContent);

        srcCode.println("\t\treturn out.toString();");
        srcCode.println("\t}");
        srcCode.println("}");
        srcCode.close();

        return srcFile;
    }

    private static String processTemplate(String code, List<String> imports){
        StringBuilder result = new StringBuilder();
        Stack<Token> stack = new Stack<>();

        String text = code;
        int linesCount = 1, tabCount = 2, start = 0, end;
        while(! "".equals(text)){
            end = text.indexOf('#', start);
            if(end == -1 || end == text.length() - 1){
                outputRaw(result, text, linesCount, tabCount, false, false);
                break;
            /*
            } else if(text.charAt(end+1) == '#'){
                start = end + 2;
            */
            } else if(text.charAt(end+1) == '{'){
                //a scriptlet
                linesCount = outputRaw(result, text.substring(0, end), linesCount, tabCount, false, false);

                String scriptlet = getSubstringBetween(text.substring(end + 1), '{', '}');
                linesCount = outputRaw(result, scriptlet, linesCount, tabCount, true, false);
                text = text.substring(end + scriptlet.length() + 3);
                start = 0;
            } else if(text.charAt(end+1) == '['){
                //a model attribute
                linesCount = outputRaw(result, text.substring(0, end), linesCount, tabCount, false, false);

                String scriptlet = getSubstringBetween(text.substring(end + 1), '[', ']');
                linesCount = outputRaw(result, scriptlet, linesCount, tabCount, true, true);
                text = text.substring(end + scriptlet.length() + 3);
                start = 0;
            } else {
                //a potential token
                String toParse = text.substring(end + 1);
                Token token = null;
                for(Token t : Token.values()){
                    if(toParse.startsWith(t.name().toLowerCase())){
                        token = t;
                    }
                }

                if(token != null){
                    if(stack.isEmpty() || ! stack.peek().onlyTokensInside) {
                        linesCount = outputRaw(result, text.substring(0, end), linesCount, tabCount, false, false);
                    } else {
                        linesCount += countChar(text.substring(0, end), '\n');
                    }

                    if(token != Token.END){
                        end += token.name().length() + 1;

                        String arg = null;
                        if(token.takesArguments()){
                            arg = getSubstringBetween(toParse.substring(token.name().length()), '(', ')');
                            end += arg.length() + 2;
                        }
                        switch(token){
                            case IF :{
                                result.append(nTabsEscaped(tabCount++)).append("if( ").append(arg).append(" ){\n");
                                stack.push(token);
                                break;
                            }
                            case ELSE :{
                                result.append(nTabsEscaped(tabCount - 1)).append("} else {\n");
                                break;
                            }
                            case FOR :{
                                result.append(nTabsEscaped(tabCount++)).append("for( ").append(arg).append(" ){\n");
                                stack.push(token);
                                break;
                            }
                            case INCLUDE :{
                                result.append(nTabsEscaped(tabCount++)).append("out.append(includeTemplate(\"").append(arg).append("\")\n");
                                stack.push(token);
                                break;
                            }
                            case OVERRIDE :{
                                result.append(nTabsEscaped(tabCount++)).append(".putComponent(\"").append(arg).append("\", new Component(){\n")
                                        .append(nTabsEscaped(tabCount)).append("@Override\n")
                                        .append(nTabsEscaped(tabCount++)).append("public void doView(StringBuilder out, Model model, HttpRequest request){\n");
                                stack.push(token);
                                break;
                            }
                            case DEFINE :{
                                result.append(nTabsEscaped(tabCount)).append("includeComponent(\"").append(arg).append("\", out, model, request);\n");
                                break;
                            }
                            case IMPORT :{
                                imports.add(arg);
                                break;
                            }
                        }
                    } else {
                        end += Token.END.name().length() + 1;

                        if(!stack.isEmpty()) {
                            Token openingToken = stack.pop();
                            switch (openingToken) {
                                case IF: case FOR: {
                                    result.append(nTabsEscaped(--tabCount)).append("}\n");
                                    break;
                                }
                                case INCLUDE: {
                                    result.append(nTabsEscaped(tabCount--)).append(".doView(model, request)\n")
                                            .append(nTabsEscaped(tabCount)).append(");\n");
                                    break;
                                }
                                case OVERRIDE: {
                                    result.append(nTabsEscaped(--tabCount)).append("}\n");
                                    result.append(nTabsEscaped(--tabCount)).append("})\n");
                                    break;
                                }
                            }
                        }
                    }
                    text = text.substring(end);
                    start = 0;
                } else {
                    //no token found, it was just a single #
                    start = end + 1;
                }
            }
        }

        return result.toString();
    }

    private static int outputRaw(StringBuilder sb, String text, int linesCounter, int tabCounter, boolean isCode, boolean outputCode){
        if("".equals(text)) return linesCounter;

        /*
        if(!isCode){
            text = text.replace("##", "#");
        }
    */
        String[] lines = text.split("\n");
        for(int i = 0; i < lines.length; ++i){
            if(isCode) {
                if(outputCode && !"".equals(lines[i].trim())){
                    sb.append(nTabsEscaped(tabCounter)).append("out.append(").append(lines[i].trim());
                } else {
                    sb.append(nTabsEscaped(tabCounter)).append(lines[i].trim());
                }
            } else {
                sb.append(nTabsEscaped(tabCounter)).append("// #").append(linesCounter).append("\n");
                sb.append(nTabsEscaped(tabCounter)).append("out.append(\"").append(escapeString(lines[i]));
            }

            if( i < lines.length - 1) {
                if(! isCode) {
                    sb.append("\\n");
                }
                linesCounter++;
            }

            sb.append((isCode) ? ((outputCode)? ");\n" : "\n") : "\");\n");
        }
        return linesCounter;
    }

    private static String escapeString(String string){
        return string.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static int countChar(String string, char ch){
        int result = 0;
        for(char c : string.toCharArray()){
            if(c == ch){
                result++;
            }
        }
        return result;
    }

    private static String nTabsEscaped(int n){
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < n; ++i){
            result.append("\t");
        }
        return result.toString();
    }

    private static String getSubstringBetween(String string, char open, char close){
        if(! string.startsWith("" + open)){
            return "";
        }
        int count = 1;
        int i = 1;
        while(count > 0 && i < string.length()){
            char ch = string.charAt(i++);
            if(ch == open){
                count ++;
            } else if(ch == close){
                count --;
            }
        }

        return (count == 0) ? string.substring(1, i-1) : "";
    }

    protected enum Token {
        IMPORT(true, false),
        IF(true, false),
        ELSE(false, false),
        FOR(true, false),

        DEFINE(true, false),
        INCLUDE(true, true),
        OVERRIDE(true, false),

        END(false, false);

        private boolean takesArguments;
        private boolean onlyTokensInside;

        Token(boolean takesArguments, boolean onlyTokensInside){
            this.takesArguments = takesArguments;
            this.onlyTokensInside = onlyTokensInside;
        }

        public boolean takesArguments(){
            return takesArguments;
        }
    }
}
