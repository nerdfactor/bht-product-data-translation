package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
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

/**
 * Controller for {@link Product} related operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    /**
     * Service for searching {@link Product Products}.
     */
    final ProductSearchService productSearchService;

    /**
     * The {@link ModelMapper} used for mapping between Entity and DTOs.
     */
    final ModelMapper mapper;

    /**
     * Search for {@link Product Products} by name.
     *
     * @param search   The search query.
     * @param pageable The {@link Pageable} for pagination.
     * @return A {@link ResponseEntity} containing a {@link Page} of {@link ProductDto} objects.
     */
    @GetMapping(value = "/search")
    public ResponseEntity<Page<ProductDto>> searchAllProducts(
            @RequestParam(value = "query", required = false) Optional<String> search,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(this.productSearchService.searchAllProducts(search.orElse(""), LocaleContextHolder.getLocale(), pageable)
                .map(product -> mapper.map(product, ProductDto.class)));
    }
}
