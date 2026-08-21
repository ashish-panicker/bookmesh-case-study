package com.example.bookservice.service;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.dto.StockCheckRequest;
import com.example.bookservice.exceptions.ResourceNotFoundException;
import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book create(Book book) {
        log.info("Creating new book: {}", book.getTitle());
        return repository.save(book);
    }

    @Override
    public Optional<Book> findById(Long id) {
        log.debug("Fetching book with id: {}", id);
        return repository.findById(id);
    }

    /**
     * @deprecated Use {@link #findAll(Pageable)} instead for better performance and pagination support.
     */
    @Deprecated
    @Override
    public List<Book> findAll() {
        log.debug("Fetching all books");
        return repository.findAll();
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        log.debug("Fetching all books with pagination");
        return repository.findAll(pageable);
    }

    @Override
    public Book mapToEntity(BookDto dto) {
        return Book.builder()
                .author(dto.author())
                .title(dto.title())
                .stock(dto.stock())
                .unitPrice(dto.unitPrice())
                .build();

    }

    @Override
    public BookDto mapToDto(Book book) {
        return new BookDto(
                book.getId(), book.getTitle(), book.getAuthor(),
                book.getStock(), book.getUnitPrice()
        );
    }

    @Override
    public Optional<Book> findByAuthorAndTitle(String author, String title) {
        log.debug("Finding book by author: {} and title: {}", author, title);
        return repository.findByAuthorAndTitle(author, title);
    }

    @Override
    public boolean checkIfStockExists(Long bookId, int stock) {
        return repository.existsByIdAndStockGreaterThanEqual(bookId, stock);
    }

    @Override
    @Transactional
    public boolean deductStock(Long id, int quantity) {
        var book = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cannot find resource with id: " + id));
        book.setStock(book.getStock() - quantity);
        repository.save(book);
        return true;
    }

    @Override
    public boolean checkBulkStockExists(List<StockCheckRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return true;
        }
        for (var request : requests) {
            if (!repository.existsByIdAndStockGreaterThanEqual(request.bookId(), request.quantity())) {
                return false;
            }
        }
        return true;
    }
}
