/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.http.HttpServletResponse;
import net.ecom.common.GeneralConf;
import net.ecom.common.LogUtil;

/**
 *
 * @author sridharan.b
 */
public class XssScriptsBlocker {

    public boolean sanitize(String strParameter, ServletRequest request, ServletResponse response) throws ServletException, IOException {
        LogUtil.log("XssScriptsBlocker sanitize");
        boolean result = true;
        //System.out.println("[ECOM-WAR] XssScriptsBlocker within sanitizeHtmlCharacters =======>" + strParameter);
        //HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        //ActionErrors errors = new ActionErrors();
        if (strParameter != null) {
            String strRegexValue = "((((\\%3C)|<)+[A-Za-z0-9\\%]+((\\%3E)|>))+[A-Za-z0-9(\\%20|\\ )\\%((\\%2F)|\\/)((\\%22)|\")((\\%27)|\')((\\%2E)|.)((\\%3D)|=)((\\%24)|$)((\\%26)|&)(\\%27)(\\%28)((\\%3B)|;)((\\%3F)|?)((\\%7B)|{)((\\%7D)|})((\\%23)|#)]+(((\\%3C)|<)((\\%2F)|\\/)+[A-Za-z0-9\\%]+((\\%3E)|>)))"
                    + "|(((\\%3C)|<)+[A-Za-z0-9\\%]+[A-Za-z0-9(\\%20|\\ )\\%((\\%2F)|\\/)((\\%22)|\")((\\%27)|\')((\\%2E)|.)((\\%3D)|=)((\\%24)|$)((\\%26)|&)(\\%27)(\\%28)((\\%3B)|;)((\\%3F)|?)((\\%7B)|{)((\\%7D)|})((\\%23)|#)]+(((\\%2F)|\\/)+((\\%3E)|>)))|(((\\%3C)|<)+[A-Za-z0-9\\%]+[A-Za-z0-9(\\%20|\\ )\\%((\\%2F)|\\/)((\\%22)|\")((\\%27)|\')((\\%2E)|.)((\\%3D)|=)((\\%24)|$)((\\%26)|&)(\\%27)(\\%28)((\\%3B)|;)((\\%3F)|?)((\\%7B)|{)((\\%7D)|})((\\%23)|#)]+((\\%3E)|>))|"
                    + "<script>|</script>|<javascript>|vbscript|\\?php|&#40|&#41";
            Pattern p = Pattern.compile(strRegexValue);
            Matcher m = p.matcher(strParameter);
            if (m.find()) {
                //errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("error.security"));
                // req.setAttribute(Globals.ERROR_KEY, errors);
                LogUtil.log("XssScriptsBlocker within sanitizeHtmlCharacters: " + strParameter);
                //req.getRequestDispatcher("SessionExpired.jsp").forward(req, res);
                //request.getRequestDispatcher("SessionExpired.jsp").forward(request, response);
                // throw new SecurityViolationException();
                result = false;
                res.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            if ("Y".equalsIgnoreCase((String) request.getServletContext().getAttribute(GeneralConf.Enable_New_XSS_Pattern))) {
                // Case-insensitive regex to detect common XSS attack patterns
                String strRegexValueNew
                        = "(?i)"
                        + // case-insensitive
                        "(<script.*?>.*?</script>)|"
                        + // <script> blocks
                        "((javascript|vbscript|data):)|"
                        + // URI handlers
                        "(on\\w+\\s*=)|"
                        + // onerror=, onclick=, etc.
                        "(<.*?\\s+src\\s*=\\s*['\"].*?>)|"
                        + // tags with src=
                        "(<iframe.*?>)|"
                        + "(<img.*?>)|"
                        + "(<svg.*?>)|"
                        + "(<object.*?>)|"
                        + "(<embed.*?>)|"
                        + "(<base.*?>)";

                Pattern pattern = Pattern.compile(strRegexValueNew);
                Matcher matcher = pattern.matcher(strParameter);

                if (matcher.find()) {
                    LogUtil.log("XssScriptsBlocker XSS pattern detected New: " + strParameter);

                    // Optional alert to user (DEBUG only, not for production)
                    res.setContentType("text/html");
                    PrintWriter out = res.getWriter();
                    out.println("<script>alert('Potential XSS attack detected. Request blocked.');</script>");
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);

                    result = false;
                }
            }

        }
        return result;
    }
}
