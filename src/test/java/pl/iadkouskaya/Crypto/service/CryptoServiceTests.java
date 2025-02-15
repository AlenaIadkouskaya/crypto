package pl.iadkouskaya.Crypto.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import pl.iadkouskaya.Crypto.dto.Crypto;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptoServiceTests {
    @Mock
    private HttpClient httpClient;
    @Mock
    private final ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
    private CryptoServiceImpl cryptoService;
    @Value("${crypto.urls.urlAnyCrypto:http://mocked-url.com}")
    private String apiUrl;

    @BeforeEach
    void setUp() {
        cryptoService = new CryptoServiceImpl(httpClient, objectMapper, "http://mocked-url.com");
    }

    @Test
    void should_filter_cryptos_correctly() throws IOException, InterruptedException {
        // given
        String jsonResponse = """
                [
                    {"symbol":"BTC","name":"Bitcoin","current_price":50000.0},
                    {"symbol":"ETH","name":"Ethereum","current_price":3000.0},
                    {"symbol":"DOGE","name":"Dogecoin","current_price":0.08}
                ]
                """;

        List<Crypto> mockCryptos = List.of(
                new Crypto("BTC", "Bitcoin", 50000.0),
                new Crypto("ETH", "Ethereum", 3000.0),
                new Crypto("DOGE", "Dogecoin", 0.08)
        );

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(jsonResponse);

        when(httpClient.send(argThat(req ->
                        req.uri().toString().equals("http://mocked-url.com")),
                any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        when(objectMapper.readValue(eq(jsonResponse), any(TypeReference.class)))
                .thenReturn(mockCryptos);
        // when
        List<Crypto> result = cryptoService.getAllCryptos("BTC", 10000.0, 60000.0);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSymbol()).isEqualTo("BTC");
    }

    @Test
    void should_throw_exception_when_no_cryptos_found() throws IOException, InterruptedException {
        // given
        String jsonResponse = "[]";

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(jsonResponse);

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        when(objectMapper.readValue(eq(jsonResponse), any(TypeReference.class)))
                .thenReturn(List.of());
        // when
        Executable e = () -> cryptoService.getAllCryptos(null, null, null);

        // then
        assertThrows(RuntimeException.class, e);
    }

    @Test
    void should_return_all_cryptos_when_no_filters_applied() throws IOException, InterruptedException {
        // given
        String jsonResponse = """
            [
                {"symbol":"BTC","name":"Bitcoin","current_price":50000.0},
                {"symbol":"ETH","name":"Ethereum","current_price":3000.0},
                {"symbol":"DOGE","name":"Dogecoin","current_price":0.08}
            ]
            """;

        List<Crypto> mockCryptos = List.of(
                new Crypto("BTC", "Bitcoin", 50000.0),
                new Crypto("ETH", "Ethereum", 3000.0),
                new Crypto("DOGE", "Dogecoin", 0.08)
        );

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(jsonResponse);

        when(httpClient.send(argThat(req ->
                        req.uri().toString().equals("http://mocked-url.com")),
                any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        when(objectMapper.readValue(eq(jsonResponse), any(TypeReference.class)))
                .thenReturn(mockCryptos);

        // when
        List<Crypto> result = cryptoService.getAllCryptos(null, null, null);

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getSymbol()).isEqualTo("BTC");
        assertThat(result.get(1).getSymbol()).isEqualTo("ETH");
        assertThat(result.get(2).getSymbol()).isEqualTo("DOGE");
    }

}