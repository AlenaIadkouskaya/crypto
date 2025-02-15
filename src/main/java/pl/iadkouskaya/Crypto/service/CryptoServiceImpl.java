package pl.iadkouskaya.Crypto.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.iadkouskaya.Crypto.dto.Crypto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CryptoServiceImpl implements CryptoService {
    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    public CryptoServiceImpl() {
    }
    @Autowired
    public CryptoServiceImpl(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public CryptoServiceImpl(HttpClient httpClient, ObjectMapper objectMapper,
                             @Value("${crypto.urls.urlAnyCrypto}") String urlAnyCrypto) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.urlAnyCrypto = urlAnyCrypto;
    }


    @Value("${crypto.urls.urlAnyCrypto}")
    private String urlAnyCrypto;

    @Override
    public List<Crypto> getAllCryptos(String symbol, Double minPrice, Double maxPrice) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlAnyCrypto))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        List<Crypto> cryptos = objectMapper.readValue(response.body(), new TypeReference<List<Crypto>>() {
        });

        if (cryptos == null || cryptos.isEmpty()) {
            throw new RuntimeException("The cryptocurrency list is empty or could not be loaded");
        }

        return filterCryptos(cryptos, symbol, minPrice, maxPrice);

    }

    private List<Crypto> filterCryptos(List<Crypto> cryptos, String symbol, Double minPrice, Double maxPrice) {
        if (symbol != null) {
            cryptos = cryptos.stream()
                    .filter(crypto -> crypto.getSymbol().toLowerCase().contains(symbol.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (minPrice != null) {
            cryptos = cryptos.stream()
                    .filter(crypto -> crypto.getCurrent_price() >= minPrice)
                    .collect(Collectors.toList());
        }

        if (maxPrice != null) {
            cryptos = cryptos.stream()
                    .filter(crypto -> crypto.getCurrent_price() <= maxPrice)
                    .collect(Collectors.toList());
        }

        return cryptos;
    }
}
