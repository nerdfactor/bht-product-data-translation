package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

	final ProductSearchService productSearchService;

	final ModelMapper mapper;

	@GetMapping(value = "/search")
	public ResponseEntity<List<ProductDto>> searchAllProducts(@RequestParam(value = "search", required = false) Optional<String> search) {
		return ResponseEntity.ok(this.productSearchService.searchAllProducts(search.orElse(""))
				.stream().map(product -> this.mapper.map(product, ProductDto.class))
				.toList());
	}
}
