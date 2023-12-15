package org.babyfish.jimmer.client.java.model;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

public class BookInput {

    private final String name;

    private final int edition;

    private final BigDecimal price;

    @Nullable
    private final Long storeId;

    private final List<Long> authorIds;

    public BookInput(
            String name,
            int edition,
            BigDecimal price,
            @Nullable Long storeId,
            List<Long> authorIds
    ) {
        this.name = name;
        this.edition = edition;
        this.price = price;
        this.storeId = storeId;
        this.authorIds = authorIds;
    }

    @Override
    public String toString() {
        return "BookInput{" +
                "name='" + name + '\'' +
                ", edition=" + edition +
                ", price=" + price +
                ", storeId=" + storeId +
                ", authorIds=" + authorIds +
                '}';
    }
}
