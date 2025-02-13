package pl.iadkouskaya.Crypto.service;

import pl.iadkouskaya.Crypto.dto.Crypto;

import java.io.IOException;
import java.util.List;

public interface CryptoService {
    List<Crypto> getAllCryptos(String symbol, Double minPrice, Double maxPrice) throws IOException, InterruptedException;
}
