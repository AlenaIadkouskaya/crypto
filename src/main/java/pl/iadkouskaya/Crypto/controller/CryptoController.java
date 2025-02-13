package pl.iadkouskaya.Crypto.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.iadkouskaya.Crypto.dto.Crypto;
import pl.iadkouskaya.Crypto.service.CryptoService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/cryptos")
@CrossOrigin(origins = "http://localhost:4200")
public class CryptoController {
    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCrypties(
            @RequestParam(required = false) String symbol,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) throws IOException, InterruptedException {

        List<Crypto> cryptos = cryptoService.getAllCryptos(symbol, minPrice, maxPrice);

        return ResponseEntity.ok(cryptos);

    }

}
