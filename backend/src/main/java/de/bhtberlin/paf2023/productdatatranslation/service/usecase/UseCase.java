package de.bhtberlin.paf2023.productdatatranslation.service.usecase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A UseCase implements a specific complex functionality containing
 * several steps and interacts with different Services and Repositories.
 *
 * @param <D> The Class of the Data Object provided to the UseCase.
 * @param <R> The Class of the Response Object from the UseCase.
 */
public interface UseCase<D, R> {

    /**
     * Execute the UseCase.
     *
     * @param dto A data transfer object containing all required data to execute the UseCase.
     * @return The result of the UseCase. May be null, if {@link R} is {@link Void}.
     */
    public @Nullable R execute(@NotNull D dto);
}
