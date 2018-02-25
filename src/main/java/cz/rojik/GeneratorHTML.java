package cz.rojik;

import cz.rojik.constant.ProjectContants;
import cz.rojik.model.MicrobenchmarkResult;
import cz.rojik.model.Result;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

public class GeneratorHTML {

    private static Logger logger = LoggerFactory.getLogger(GeneratorHTML.class);

    public GeneratorHTML() {}

    public void generateHTMLFile(Result results, Template template) {
        logger.info("Generate HTML page with results");

        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        sb.append("<html><head>")
                .append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">")
                .append("</head><body>")
                .append("<h2 class=\"text-center\">")
                .append(results.getTime().format(formatter))
                .append("</h2><br />");

        generateResultTable(sb, results, template);

        sb.append("</body></html>");

        try {
            File file = new File(ProjectContants.RESULT_HTML_FILE);
            FileUtils.writeStringToFile(file, sb.toString(), StandardCharsets.UTF_8, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateResultTable(StringBuilder sb, Result results, Template template) {
        int i = 1;

        sb.append("<table class=\"table table-hover\">")
                .append("<thead><tr><th scope=\"col\">#</th><th scope=\"col\">Název</th><th scope=\"col\">Tělo metody</th><th scope=\"col\">Počet warmup iterací</th><th scope=\"col\">Počet měření</th><th scope=\"col\">Naměřený čas</th><th scope=\"col\">+- chyba</th><th scope=\"col\">Jednotky</th></tr></thead>")
                .append("<tbody>");
        for (MicrobenchmarkResult mbResult : results.getResults()) {
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
}
