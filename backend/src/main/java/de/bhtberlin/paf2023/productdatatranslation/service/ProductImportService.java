package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.entity.Translation;
import de.bhtberlin.paf2023.productdatatranslation.repo.ProductRepository;
import de.bhtberlin.paf2023.productdatatranslation.repo.TranslationRepository;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

/**
 * Service for product importing.
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ProductImportService {

    private static final String CSV_CONTENT_TYPE = "text/csv";
    private static final String CSV_HEADER = "serial;name;height;width;depth;weight;shortDescription;longDescription";

    /**
     * An implementation of a {@link ProductRepository} for data access.
     * For example a specific JpaRepository for access to database layer.
     */
    final ProductRepository productRepository;

    /**
     * An implementation of a {@link TranslationRepository} for data access.
     * For example a specific JpaRepository for access to database layer.
     */
    final TranslationRepository translationRepository;

    final LanguageService languageService;

    /**
     * Import products from a CSV file asynchronously.
     *
     * @param file The CSV file to import.
     * @return A {@link CompletableFuture} that will be completed when the import is finished.
     * @throws IOException If there was a problem reading the file.
     */
    @Async
    public CompletableFuture<Boolean> importProductsFromCsv(@NotNull MultipartFile file) throws IOException {
        if (file.getContentType() == null || !file.getContentType().startsWith(CSV_CONTENT_TYPE)) {
            return CompletableFuture.completedFuture(false);
        }

        CsvReader.builder()
                .skipEmptyRows(true)
                .fieldSeparator(';')
                .build(new InputStreamReader(file.getInputStream()))
                .forEach(row -> {
                    if (!CSV_HEADER.startsWith(row.getField(0).toLowerCase())) {
                        this.importProductFromCsvRow(row);
                    }
                });
        log.info("Imported products from file: " + file.getOriginalFilename());
        return CompletableFuture.completedFuture(true);
    }

    /**
     * Import a single product from a row in a CSV file.
     * todo: maybe have unique serial numbers?
     *
     * @param row The CSV row to import.
     */
    @Transactional
    public void importProductFromCsvRow(CsvRow row) {
        Product product = new Product();
        product.setSerial(row.getField(0));
        product.setName(row.getField(1));
        product.setHeight(Double.parseDouble(row.getField(2)));
        product.setWidth(Double.parseDouble(row.getField(3)));
        product.setDepth(Double.parseDouble(row.getField(4)));
        product.setWeight(Double.parseDouble(row.getField(5)));
        this.productRepository.save(product);
        Translation translation = new Translation();
        translation.setShortDescription(row.getField(6));
        translation.setLongDescription(row.getField(7));
        translation.setProduct(product);
        translation.setLanguage(this.languageService.getDefaultLanguage());
        this.translationRepository.save(translation);
    }

}
