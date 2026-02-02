package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ServletHipotenusa extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        Double a = parseDouble(request.getParameter("a"));
        Double b = parseDouble(request.getParameter("b"));

        String entrada = "a=" + safe(request.getParameter("a")) + ", b=" + safe(request.getParameter("b"));
        String salida = "";
        String err = null;

        if (a == null || b == null) {
            err = "Ingresa los dos catetos (números).";
        } else if (a < 0 || b < 0) {
            err = "Los catetos deben ser mayores o iguales a 0.";
        } else {
            double h = Math.hypot(a, b);
            salida = String.format(java.util.Locale.US, "%.6f", h);
        }

        StringBuilder inner = new StringBuilder();
        inner.append("<h2>Servlet Hipotenusa</h2>");
        inner.append(nav("hip"));

        if (err != null) {
            inner.append(errorBox(err));
        } else {
            inner.append(table2(entrada, salida));
        }

        inner.append("<div class='actions'>")
             .append("<a class='btn primary' href='hipotenusa.html'>Nuevo cálculo</a>")
             .append("<a class='btn' href='index.html'>Volver</a>")
             .append("</div>");

        try (PrintWriter out = response.getWriter()) {
            out.print(page("Hipotenusa", inner.toString()));
        }
    }

    private Double parseDouble(String s) {
        if (s == null) return null;
        try { return Double.valueOf(s.trim()); } catch (NumberFormatException ex) { return null; }
    }

    private String safe(String s) {
        return (s == null) ? "" : s.trim();
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
