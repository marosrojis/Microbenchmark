package cz.rojik;

import cz.rojik.constant.ProjectContants;
import cz.rojik.model.Error;
import cz.rojik.model.ErrorInfo;
import cz.rojik.model.MicrobenchmarkResult;
import cz.rojik.model.Result;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GeneratorHTML {

    private static Logger logger = LoggerFactory.getLogger(GeneratorHTML.class);

    public GeneratorHTML() {}

    public void generateHTMLFile(Result result, Template template) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        sb.append("<html><head>")
                .append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">")
                .append("</head><body>")
                .append("<h2 class=\"text-center\">")
                .append(result.getTime().format(formatter))
                .append("</h2><br />");

        if (result.isSuccess()) {
            generateResultTable(sb, result, template);
        }
        else {
            generateErrorHTMLFile(sb, result.getErrors());
        }

        sb.append("</body></html>");

        try {
            File file = new File(ProjectContants.RESULT_HTML_FILE);
            FileUtils.writeStringToFile(file, sb.toString(), StandardCharsets.UTF_8, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateResultTable(StringBuilder sb, Result result, Template template) {
        logger.info("Generate HTML page with results");
        int i = 1;

        sb.append("<table class=\"table table-hover\">")
                .append("<thead><tr><th scope=\"col\">#</th><th scope=\"col\">Název</th><th scope=\"col\">Tělo metody</th><th scope=\"col\">Počet warmup iterací</th><th scope=\"col\">Počet měření</th><th scope=\"col\">Naměřený čas</th><th scope=\"col\">+- chyba</th><th scope=\"col\">Jednotky</th></tr></thead>")
                .append("<tbody>");
        for (MicrobenchmarkResult mbResult : result.getResults()) {
            if (mbResult.isFastest()) {
                sb.append("<tr class=\"table-success\">");
            }
            else {
                sb.append("<tr>");
            }
            sb.append("<th scope=\"row\">")
                    .append(i)
                    .append("</th><td>")
                    .append(mbResult.getName())
                    .append("</td><td>")
                    .append(template.getTestMethods().get(i - 1).replaceAll("\n", "<br />"))
                    .append("</td><td>")
                    .append(mbResult.getWarmupIterations())
                    .append("</td><td>")
                    .append(mbResult.getMeasurementIterations())
                    .append("</td><td>")
                    .append(String.format("%.3f", mbResult.getScore()))
                    .append("</td><td>")
                    .append(String.format("%.3f", mbResult.getError()))
                    .append("</td><td>")
                    .append(mbResult.getUnit())
                    .append("</td></tr>");
            i++;
        }
        sb.append("</tbody></table>");
    }

    private void generateErrorHTMLFile(StringBuilder sb, List<ErrorInfo> errors) {
        logger.info("Generate ERROR HTML page with results");
        int i = 1;

        sb.append("<table class=\"table table-hover\">")
                .append("<thead><tr><th scope=\"col\">#</th><th scope=\"col\">Chybný kód</th><th scope=\"col\">Popis chyby</th></tr></thead>")
                .append("<tbody>");
        for (ErrorInfo errorInfo : errors) {
            int row = -1;
            sb.append("<tr><th scope=\"row\">")
                    .append(i)
                    .append("</th><td>");
            for (Error error : errorInfo.getErrors()) {
                if (row != error.getRow()) {
                    sb.append(error.getCode())
                            .append("<br />");
                }
                row = error.getRow();
            }
            sb.append("</td><td>");
            for (Error error : errorInfo.getErrors()) {
                sb.append(error.getMessage())
                        .append("<br />");
            }
            sb.append("</td></tr>");
            i++;
        }
        sb.append("</tbody></table>");
    }
}
