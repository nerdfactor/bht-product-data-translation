package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.service.ProductImportService;
import de.bhtberlin.paf2023.productdatatranslation.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    final ProductSearchService productSearchService;

    final ProductImportService productImportService;

    final ModelMapper mapper;

    @GetMapping(value = "/search")
    public ResponseEntity<Page<ProductDto>> searchAllProducts(
            @RequestParam(value = "query", required = false) Optional<String> search,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(this.productSearchService.searchAllProducts(search.orElse(""), LocaleContextHolder.getLocale(), pageable)
                .map(product -> mapper.map(product, ProductDto.class)));
    }

    /**
     * Import products from a CSV file.
     * This method will return a 202 Accepted response if the request contains the header Prefer: respond-async to enable
     * asynchronous processing.
     *
     * @param file   The CSV file to import.
     * @param prefer The Prefer header value.
     * @return A 202 Accepted response for asynchronous processing or a 200 OK response if the request was processed synchronously.
     */
    @GetMapping(value = "/import")
    public ResponseEntity<Void> importProducts(@RequestPart("file") MultipartFile file,
                                               @RequestHeader(value = "prefer", required = false, defaultValue = "") String prefer) {
        try {
            CompletableFuture<Boolean> future = this.productImportService.importProductsFromCsv(file);
            if (prefer.toLowerCase().contains("respond-async")) {
                return ResponseEntity.accepted().build();
            } else {
                future.join();
                return ResponseEntity.ok().build();
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
