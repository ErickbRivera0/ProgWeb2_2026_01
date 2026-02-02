package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ServletBinarios extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String operacion = request.getParameter("operacion");
        String numero = request.getParameter("numero");

        String entrada = (numero == null) ? "" : numero.trim();
        String salida = "";
        String opBonita = "Operación";
        String err = null;

        if (operacion == null || operacion.isBlank()) {
            err = "Selecciona una operación.";
        } else if ("dec2bin".equals(operacion)) {
            opBonita = "Decimal → Binario";
            try {
                long n = Long.parseLong(entrada);
                if (n < 0) {
                    err = "El número decimal debe ser mayor o igual a 0.";
                } else {
                    salida = Long.toBinaryString(n);
                }
            } catch (NumberFormatException ex) {
                err = "Ingresa un número decimal válido (solo dígitos).";
            }
        } else if ("bin2dec".equals(operacion)) {
            opBonita = "Binario → Decimal";
            if (!entrada.matches("[01]+")) {
                err = "Ingresa un número binario válido (solo 0 y 1).";
            } else {
                try {
                    long n = Long.parseLong(entrada, 2);
                    salida = String.valueOf(n);
                } catch (NumberFormatException ex) {
                    err = "El binario es demasiado grande.";
                }
            }
        } else {
            err = "Operación inválida.";
        }

        StringBuilder inner = new StringBuilder();
        inner.append("<h2>Servlet Binarios</h2>");
        inner.append(nav("bin"));
        inner.append("<div class='note'>Operación: <b>").append(escape(opBonita)).append("</b></div>");

        if (err != null) {
            inner.append(errorBox(err));
        } else {
            inner.append(table2(entrada, salida));
        }

        inner.append("<div class='actions'>")
             .append("<a class='btn primary' href='binarios.html'>Nuevo cálculo</a>")
             .append("<a class='btn' href='index.html'>Volver</a>")
             .append("</div>");

        try (PrintWriter out = response.getWriter()) {
            out.print(page("Binarios", inner.toString()));
        }
    }


    private String page(String title, String innerHtml) {
        return "<!doctype html>"
                + "<html lang='es'><head>"
                + "<meta charset='utf-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1'>"
                + "<title>" + escape(title) + "</title>"
                + "<link rel='stylesheet' href='style.css'>"
                + "</head><body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<div class='brand'>PW2 • Servlets</div>"
                + "<div class='pill'>Tomcat 11 • Servlet 6.0</div>"
                + "</div>"
                + "<div class='card'><div class='card-body'>"
                + innerHtml
                + "<div class='footer'><div>Eric Bonilla y Margie Dubon</div><div>202110080015 - 202220070063</div></div>"
                + "</div></div></div>"
                + "</body></html>";
    }

    private String nav(String current) {
        String active = "btn primary";
        String normal = "btn";
        return "<div class='nav'>"
                + link("index.html", "Menú", normal)
                + link("binarios.html", "Binarios", current.equals("bin") ? active : normal)
                + link("numeros.html", "Números", current.equals("num") ? active : normal)
                + link("hipotenusa.html", "Hipotenusa", current.equals("hip") ? active : normal)
                + "</div>";
    }

    private String link(String href, String text, String cls) {
        return "<a class='" + cls + "' href='" + href + "'>" + escape(text) + "</a>";
    }

    private String table2(String in, String out) {
        return "<div class='table-wrap'><table>"
                + "<thead><tr><th>Entrada</th><th>Respuesta</th></tr></thead>"
                + "<tbody><tr><td>" + escape(in) + "</td><td>" + escape(out) + "</td></tr></tbody>"
                + "</table></div>";
    }

    private String errorBox(String msg) {
        return "<div class='error'>" + escape(msg) + "</div>";
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

}
