package de.bhtberlin.paf2023.productdatatranslation.component;

import de.bhtberlin.paf2023.productdatatranslation.entity.Color;
import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.service.ColorCrudService;
import de.bhtberlin.paf2023.productdatatranslation.service.LanguageCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateTestDataRunner implements ApplicationRunner {

    final LanguageCrudService languageService;
    final ColorCrudService colorService;

    public void run(ApplicationArguments args) {
        // todo: should be removed after tests are done
        this.languageService.createLanguage(new Language("deutsch", "euro", "metric", "de_DE"));
        this.colorService.createColor(new Color("red"));
        this.colorService.createColor(new Color("blue"));
    }
}
