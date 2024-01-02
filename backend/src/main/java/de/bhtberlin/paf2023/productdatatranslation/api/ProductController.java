package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    final ProductSearchService productSearchService;

    final ModelMapper mapper;

    @GetMapping(value = "/search")
    public ResponseEntity<Page<ProductDto>> searchAllProducts(
            @RequestParam(value = "query", required = false) Optional<String> search,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(this.productSearchService.searchAllProducts(search.orElse(""), LocaleContextHolder.getLocale(), pageable)
                .map(product -> mapper.map(product, ProductDto.class)));
    }
}
