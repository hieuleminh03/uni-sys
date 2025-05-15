package util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@Builder
@Getter
public class Paging {

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public static Paging of(int page, int size, long totalElements) {
        return Paging.builder()
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages((int) Math.ceil((double) totalElements / size))
                .build();
    }

    public static Paging of(int page, int size) {
        return Paging.builder()
                .page(page)
                .size(size)
                .totalElements(0)
                .totalPages(0)
                .build();
    }

    public static Paging empty() {
        return Paging.builder()
                .page(0)
                .size(0)
                .totalElements(0)
                .totalPages(0)
                .build();
    }

    public static Paging from(Page<?> page) {
        return Paging.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
