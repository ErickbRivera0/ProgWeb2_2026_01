package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ServletNumeros extends HttpServlet {
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
        Integer a = parseInt(request.getParameter("a"));
        Integer b = parseInt(request.getParameter("b"));
        Integer c = parseInt(request.getParameter("c"));

      
        String entrada = "a=" + safe(request.getParameter("a")) + ", b=" + safe(request.getParameter("b")) + ", c=" + safe(request.getParameter("c"));
        String salida = "";
        String opBonita = "Operación";
        String err = null;

        if (operacion == null || operacion.isBlank()) {
            err = "Selecciona una operación.";
        }

       
        else if ("mayor".equals(operacion)) {

            if (a == null || b == null || c == null) {
                err = "Ingresa A, B y C (enteros).";
            } else {
                opBonita = "Mayor de 3 números";
                salida = String.valueOf(Math.max(a, Math.max(b, c)));
            }
        }

      
        else if ("menor".equals(operacion)) {

            if (a == null || b == null || c == null) {
                err = "Ingresa A, B y C (enteros).";
            } else {
                opBonita = "Menor de 3 números";
                salida = String.valueOf(Math.min(a, Math.min(b, c)));
            }
        }

   
        else if ("moda".equals(operacion)) {

            opBonita = "Número más repetido (Moda)";
            String lista = request.getParameter("lista");

            if (lista == null || lista.trim().isEmpty()) {
                err = "Ingresa una lista de números. Ej: 5,7,7,2,7";
            } else {

                String[] parts = lista.trim().split("[,\\s]+");
                java.util.Map<Integer, Integer> freq = new java.util.HashMap<>();

                try {
                    for (String p : parts) {
                        if (p.trim().isEmpty()) continue; // por si viene "1,2,2,"
                        int v = Integer.parseInt(p.trim());
                        freq.put(v, freq.getOrDefault(v, 0) + 1);
                    }

                    if (freq.isEmpty()) {
                        err = "No se detectaron números válidos en la lista.";
                    } else {
                        // 1) frecuencia máxima
                        int bestCount = 0;
                        for (int count : freq.values()) {
                            if (count > bestCount) bestCount = count;
                        }

                        entrada = lista;

                        // 2) lista de modas (por si hay empate)
                        java.util.List<Integer> modas = new java.util.ArrayList<>();
                        for (var e : freq.entrySet()) {
                            if (e.getValue() == bestCount) modas.add(e.getKey());
                        }
                        java.util.Collections.sort(modas);

                        // 3) armar resumen de frecuencias ordenado
                        java.util.List<Integer> keys = new java.util.ArrayList<>(freq.keySet());
                        java.util.Collections.sort(keys);
                        StringBuilder frecTxt = new StringBuilder();
                        for (int k : keys) {
                            frecTxt.append(k).append("→").append(freq.get(k)).append("  ");
                        }

                        if (bestCount <= 1) {
                            salida = "No hay moda (todos se repiten 1 vez). Frecuencias: " + frecTxt.toString().trim();
                        } else if (modas.size() == 1) {
                            salida = "Moda: " + modas.get(0) + " (repite " + bestCount + " veces). Frecuencias: " + frecTxt.toString().trim();
                        } else {
                            salida = "Modas (empate): " + modas + " (repiten " + bestCount + " veces). Frecuencias: " + frecTxt.toString().trim();
                        }
                    }

                } catch (NumberFormatException ex) {
                    err = "La lista debe contener solo enteros. Ej: 1,2,3";
                }
            }
        }
        else {
            err = "Operación inválida.";
        }

        StringBuilder inner = new StringBuilder();

       
        inner.append("<h2 class='title'>Servlet Números</h2>");
        inner.append(nav("num"));
        inner.append("<div class='note'>Operación: <b>").append(escape(opBonita)).append("</b></div>");

        if (err != null) {
            inner.append(errorBox(err));
        } else {
            inner.append(table2(entrada, salida));
        }

        inner.append("<div class='actions'>")
             .append("<a class='btn primary' href='numeros.html'>Nuevo cálculo</a>")
             .append("<a class='btn' href='index.html'>Volver</a>")
             .append("</div>");

        try (PrintWriter out = response.getWriter()) {
            out.print(page("Números", inner.toString()));
        }
    }

    private Integer parseInt(String s) {
        if (s == null) return null;
        try { return Integer.valueOf(s.trim()); } catch (NumberFormatException ex) { return null; }
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

               
                + "<div class='footer'>"
                + "<div><b>Eric Bonilla</b> y <b>Margie Dubon</b></div>"
                + "<div class='muted'>202110080015 - 202220070063</div>"
                + "</div>"

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
