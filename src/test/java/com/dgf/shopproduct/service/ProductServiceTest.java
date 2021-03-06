package com.dgf.shopproduct.service;

import com.dgf.shopproduct.Constants;
import com.dgf.shopproduct.model.Product;
import com.dgf.shopproduct.repo.ProductRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductServiceTest {

    @Mock
    private ProductRepo repo;
    private ProductService service;

    @BeforeAll
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        service = new ProductService(repo);
    }

    @Test
    public void findByNameIgnoreCaseContaining() {
        Product entity = Constants.PRODUCTS.get().get(0);
        when(repo.findByNameIgnoreCaseContaining(any())).thenReturn(Flux.fromIterable(Collections.singletonList(entity)));
        StepVerifier
            .create(service.findByNameIgnoreCaseContaining(entity.getName()))
            .assertNext(loaded -> assertEquals(entity.getName(),loaded.getName()))
            .expectComplete()
            .verify();
    }

    @Test
    public void create() {
        when(repo.deleteAll()).thenReturn(Mono.empty());
        when(repo.saveAll(anyList())).thenReturn(Flux.fromIterable(Constants.PRODUCTS.get()));
        StepVerifier
            .create(service.create(Constants.PRODUCTS.get()))
            .expectNextSequence(Constants.PRODUCTS.get())
            .expectComplete()
            .verify();
    }
}
