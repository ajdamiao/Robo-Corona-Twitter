package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class Main {

    private static Object HttpUtility;

    public static <NameValuePair, HttpPost> void main(String[] args) throws IOException, InterruptedException {
        while (true) {
            final String url = "https://www.worldometers.info/coronavirus/country/brazil/";
            String numeroCasos = null, mortes = null, curados = null;
            try {
                final Document document = Jsoup.connect(url).get();
                String informacoes = null;
                int cont = 0;
                for (Element row : document.select("div.maincounter-number")) {
                    cont = cont + 1;
                    informacoes = row.select(".maincounter-number").text();
                    // GAMBIARRA (a cada volta no FOR o cont acrescenta em 1 para poder armazenar as informacoes em variaveis foras do FOR)
                    if (cont == 1) {
                        numeroCasos = informacoes;
                    }
                    if (cont == 2) {
                        mortes = informacoes;
                    }
                    if (cont == 3) {
                        curados = informacoes;
                    }
                }
                String mensagem = ("Total de casos no Brasil:\n" + "Casos confirmados: " + numeroCasos + "\n" + "Óbitos: " + mortes + "\n" + "Recuperações: " + curados + "\n"+ "#coronavirus #corona" + "\n\n");
                System.out.println(mensagem);

                // HTTP REQUEST
                URL urlZapier = new URL("https://hooks.zapier.com/hooks/catch/7355727/o5xs2za");
                URLConnection con = urlZapier.openConnection();
                HttpURLConnection http = (HttpURLConnection) con;
                http.setRequestMethod("POST"); // PUT is another valid option
                http.setDoOutput(true);

                Map<String, String> arguments = new HashMap<>();
                arguments.put("value1", mensagem);
                StringJoiner sj = new StringJoiner("&");
                for (Map.Entry<String, String> entry : arguments.entrySet())
                    sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                            + URLEncoder.encode(entry.getValue(), "UTF-8"));
                byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
                int length = out.length;

                http.setFixedLengthStreamingMode(length);
                http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                http.connect();
                try (OutputStream os = http.getOutputStream()) {
                    os.write(out);
                }
                // FIM HTTP REQUEST
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Tweet enviado");
            Thread.sleep(3600000);
        }
    }
}

