package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.ErrorResponseDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.service.ProductCrudService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for {@link Product} related REST operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    final ProductCrudService productCrudService;

    final ModelMapper mapper;

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<ProductDto>> listAllProducts() {
        return ResponseEntity.ok(this.productCrudService.listAllProducts()
                .stream().map(product -> this.mapper.map(product, ProductDto.class))
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readProduct(@PathVariable final int id) {
        Optional<Product> product = this.productCrudService.readProduct(id);
        if (product.isEmpty()) {
            return ErrorResponseDto.createResponseEntity(
                    HttpStatus.NOT_FOUND,
                    "Product with Id " + id + " was not found."
            );
        }
        return ResponseEntity.ok(this.mapper.map(product.get(), ProductDto.class));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> setProduct(@PathVariable final int id, @RequestBody final ProductDto dto) {
        if (id != dto.getId()) {
            return ErrorResponseDto.createResponseEntity(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Mismatch between provided Ids."
            );
        }
        Product updated = this.productCrudService.updateProduct(this.mapper.map(dto, Product.class));
        return ResponseEntity.ok(this.mapper.map(updated, ProductDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable final int id) {
        this.productCrudService.deleteProductById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
