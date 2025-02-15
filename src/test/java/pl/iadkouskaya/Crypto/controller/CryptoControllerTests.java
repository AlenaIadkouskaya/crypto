package pl.iadkouskaya.Crypto.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.iadkouskaya.Crypto.dto.Crypto;
import pl.iadkouskaya.Crypto.service.CryptoService;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CryptoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoService cryptoService;

    @Test
    void should_return_filtered_cryptos_when_params_are_provided() throws Exception {
        // given
        List<Crypto> filteredCryptos = List.of(
                new Crypto("BTC", "Bitcoin", 50000.0),
                new Crypto("ETH", "Ethereum", 3000.0)
        );

        when(cryptoService.getAllCryptos("BTC", 1000.0, 60000.0)).thenReturn(filteredCryptos);

        // when & then
        mockMvc.perform(get("/api/cryptos")
                        .param("symbol", "BTC")
                        .param("minPrice", "1000")
                        .param("maxPrice", "60000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].symbol").value("BTC"))
                .andExpect(jsonPath("$[1].symbol").value("ETH"));

        verify(cryptoService, times(1)).getAllCryptos("BTC", 1000.0, 60000.0);
    }

    @Test
    void should_return_empty_list_when_no_cryptos_match_filter() throws Exception {
        // given
        List<Crypto> emptyList = List.of();

        when(cryptoService.getAllCryptos("BTC", 100000.0, 600000.0)).thenReturn(emptyList);

        // when & then
        mockMvc.perform(get("/api/cryptos")
                        .param("symbol", "BTC")
                        .param("minPrice", "100000")
                        .param("maxPrice", "600000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(cryptoService, times(1)).getAllCryptos("BTC", 100000.0, 600000.0);
    }

    @Test
    void should_return_all_cryptos_when_no_filters_are_provided() throws Exception {
        // given
        List<Crypto> allCryptos = List.of(
                new Crypto("BTC", "Bitcoin", 50000.0),
                new Crypto("ETH", "Ethereum", 3000.0),
                new Crypto("DOGE", "Dogecoin", 0.08)
        );

        when(cryptoService.getAllCryptos(null, null, null)).thenReturn(allCryptos);

        // when & then
        mockMvc.perform(get("/api/cryptos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].symbol").value("BTC"))
                .andExpect(jsonPath("$[1].symbol").value("ETH"))
                .andExpect(jsonPath("$[2].symbol").value("DOGE"));

        verify(cryptoService, times(1)).getAllCryptos(null, null, null);
    }

    @Test
    void should_return_internal_server_error_when_get_runtime_exception_is_thrown() throws Exception {
        when(cryptoService.getAllCryptos(anyString(), anyDouble(), anyDouble()))
                .thenThrow(new RuntimeException("The cryptocurrency list is empty or could not be loaded"));

        mockMvc.perform(get("/api/cryptos")
                        .param("symbol", "BTC")
                        .param("minPrice", "1000")
                        .param("maxPrice", "60000"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error: The cryptocurrency list is empty or could not be loaded"));
    }

    @Test
    void should_return_bad_request_when_get_json_processing_exception_is_thrown() throws Exception {
        when(cryptoService.getAllCryptos(anyString(), anyDouble(), anyDouble()))
                .thenThrow(new JsonProcessingException("Invalid JSON format") {
                });

        mockMvc.perform(get("/api/cryptos")
                        .param("symbol", "BTC")
                        .param("minPrice", "1000")
                        .param("maxPrice", "60000"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error processing JSON: Invalid JSON format"));
    }

    @Test
    void should_return_service_unavailable_when_get_IO_exception_is_thrown() throws Exception {
        when(cryptoService.getAllCryptos(anyString(), anyDouble(), anyDouble()))
                .thenThrow(new IOException("I/O error"));

        mockMvc.perform(get("/api/cryptos")
                        .param("symbol", "BTC")
                        .param("minPrice", "1000")
                        .param("maxPrice", "60000"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().string("Error while executing the HTTP request: I/O error"));
    }

    @Test
    void should_return_internal_server_error() throws Exception {
        when(cryptoService.getAllCryptos(anyString(), anyDouble(), anyDouble()))
                .thenThrow(new InterruptedException("Thread interrupted"));

        mockMvc.perform(get("/api/cryptos")
                        .param("symbol", "BTC")
                        .param("minPrice", "1000")
                        .param("maxPrice", "60000"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Request was interrupted: Thread interrupted"));
    }

}
