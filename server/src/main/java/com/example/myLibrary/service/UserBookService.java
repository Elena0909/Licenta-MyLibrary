package com.example.myLibrary.service;

import com.example.myLibrary.mapper.AuthorMapper;
import com.example.myLibrary.mapper.NationalityMapper;
import com.example.myLibrary.mapper.UserBookMapper;
import com.example.myLibrary.model.*;
import com.example.myLibrary.model.dto.*;
import com.example.myLibrary.repository.*;
import com.example.myLibrary.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserBookService {
    private final UserBookRepository userBookRepository;
    private final UserBookMapper userBookMapper;
    private final UserRepository userRepository;
    private final FormatRepository formatRepository;
    private final StatusRepository statusRepository;
    private final BookRepository bookRepository;
    private final GenreService genreService;
    private final CategoryService categoryService;
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final NationalityMapper nationalityMapper;

    @Autowired
    public UserBookService(UserBookRepository userBookRepository, UserBookMapper userBookMapper, UserRepository userRepository, FormatRepository formatRepository, StatusRepository statusRepository, BookRepository bookRepository, GenreService genreService, CategoryService categoryService, AuthorRepository authorRepository, AuthorMapper authorMapper, NationalityMapper nationalityMapper) {
        this.userBookRepository = userBookRepository;
        this.userBookMapper = userBookMapper;
        this.userRepository = userRepository;
        this.formatRepository = formatRepository;
        this.statusRepository = statusRepository;
        this.bookRepository = bookRepository;
        this.genreService = genreService;
        this.categoryService = categoryService;
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.nationalityMapper = nationalityMapper;
    }

    public UserBookDTO create(Long userId, UserBookDTO userBookDTO) {
        UserBook userBook = exist(userId, userBookDTO);
        if (userBook == null) {

            Optional<User> userOptional = userRepository.findById(userId);
            Optional<Book> bookOptional = bookRepository.findById(userBookDTO.getBook().getId());
            Optional<Status> statusOptional = statusRepository.findById(userBookDTO.getStatusId());
            FormatBook formatBook;

            if (userOptional.isEmpty() || bookOptional.isEmpty() || statusOptional.isEmpty()) {
                throw new RuntimeException("Error server: Information missing");
            }

            if (userBookDTO.getFormatBookId() != null) {
                Optional<FormatBook> formatBookOptional = formatRepository.findById(userBookDTO.getFormatBookId());
                formatBook = formatBookOptional.orElseThrow(RuntimeException::new);
            } else
                formatBook = null;

            User user = userOptional.get();
            Book book = bookOptional.get();
            Status status = statusOptional.get();



            UserBook newUserBook = userBookMapper.convertToUserBook(userBookDTO, book, user, formatBook, status);

            if (newUserBook == null)
                throw new RuntimeException("Error server: Convert UserBook to failure");

            validateDate(newUserBook.getDateStart(), newUserBook.getDateFinished());

            userBookRepository.save(newUserBook);
            if(Objects.equals(status.getId(), Utils.READ)){
                updateStarsBook(book,userBookDTO.getStars());
            }
            return userBookMapper.convertToUserBookDTO(newUserBook);
        }
        return update(userBook, userBookDTO);
    }

    private void updateStarsBook(Book book, Float stars){
        int count = userBookRepository.countAllByBookId(book.getId());
        Float bookStars;
        if(count==1)
            bookStars = stars;
        else
         bookStars = ((count-1)*book.getStars() + stars)/count;
        book.setStars(bookStars);
        bookRepository.save(book);
    }

    private void validateDate(LocalDate start, LocalDate end) {
        LocalDate now = LocalDate.now();

        if (start != null && start.compareTo(now) > 0)
            throw new RuntimeException("Do you travel through time?");

        if (end != null && end.compareTo(now) > 0)
            throw new RuntimeException("Do you travel through time?");

        if (start != null && end != null)
            if (end.compareTo(start) < 0)
                throw new RuntimeException("Did you finish the book before you started?");
    }

    private UserBookDTO update(UserBook userBook, UserBookDTO userBookDTO) {
        FormatBook formatBook;
        Status status;

        if (userBookDTO.getFormatBookId() != null) {
            Optional<FormatBook> formatBookOptional = formatRepository.findById(userBookDTO.getFormatBookId());
            formatBook = formatBookOptional.orElseThrow(RuntimeException::new);
        } else
            formatBook = null;

        Optional<Status> statusOptional = statusRepository.findById(userBookDTO.getStatusId());
        status = statusOptional.orElseThrow(RuntimeException::new);

        userBook.setFormatBook(formatBook);
        userBook.setStatus(status);
        userBook.setStars(userBookDTO.getStars());
        userBook.setReview(userBookDTO.getReview());

        if (userBookDTO.getDateStart() != null)
            userBook.setDateStart(userBookDTO.getDateStart().convertToLocalDate());
        else
            userBook.setDateStart(null);
        if (userBookDTO.getDateFinished() != null)
            userBook.setDateFinished(userBookDTO.getDateFinished().convertToLocalDate());
        else
            userBook.setDateFinished(null);

        validateDate(userBook.getDateStart(), userBook.getDateFinished());

        userBookRepository.save(userBook);
        if(Objects.equals(status.getId(), Utils.READ)){
            updateStarsBook(userBook.getBook(),userBookDTO.getStars());
        }
        return userBookMapper.convertToUserBookDTO(userBook);
    }

    public UserBook exist(Long userID, UserBookDTO userBookDTO) {
        Optional<UserBook> userBookOptional = userBookRepository.findByUser_IdAndBook_Id(userID, userBookDTO.getBook().getId());
        return userBookOptional.orElse(null);
    }

    public String currentStatus(Long userId, Long bookId) {
        Optional<UserBook> userBookOptional = userBookRepository.findByUser_IdAndBook_Id(userId, bookId);

        return userBookOptional.map(userBook -> userBook.getStatus().getName()).orElse(null);
    }

    public UserBookDTO checkExist(Long userId, Long bookId) {
        Optional<UserBook> userBookOptional = userBookRepository.findByUser_IdAndBook_Id(userId, bookId);

        return userBookOptional.map(userBookMapper::convertToUserBookDTO).orElse(null);
    }

    
    public Map<String, List<UserBookDTO>> getAllBooksReadForMap(Long id, Integer year) {
        List<UserBook> booksReadPerYear = userBookRepository.findAllByUserIdAndStatusId(id,Utils.READ).stream().filter(userBook -> userBook.getDateFinished().getYear() == year).collect(Collectors.toList());

        List<Nationality> nationalities = booksReadPerYear.stream().map(userBook -> userBook.getBook().getAuthors().stream().findFirst().get().getNationality()).distinct().collect(Collectors.toList());

        Map<String, List<UserBookDTO>> map = new HashMap<>();

        if (booksReadPerYear.size() == 0)
            return map;

        for (Nationality nationality : nationalities
        ) {
            List<UserBook> userBooks = booksReadPerYear.stream().filter(userBook -> userBook.getBook().getAuthors().stream().findFirst().get().getNationality().equals(nationality)).collect(Collectors.toList());
            NationalityDTO nationalityDTO = nationalityMapper.convertToNationalityDTO(nationality);
            List<UserBookDTO> userBookDTOS = userBookMapper.convertListUserBook(userBooks);
            map.put(nationalityDTO.getName(), userBookDTOS);
        }

        return map;
    }

    public List<UserBookDTO> getFilteredBooks(Long userId, Long statusId,int page) {
        Pageable pageable = PageRequest.of(page, Utils.NO_BOOKS, Sort.by("dateFinished").descending());
        List<UserBook> userBooks = userBookRepository.findAllByUserIdAndStatusId(userId, statusId, pageable);

        return userBookMapper.convertListUserBook(userBooks);
    }

    public List<UserBookDTO> reviewsBook(Long bookId, int page) {
        Pageable pageable = PageRequest.of(page, Utils.NO_REVIEWS, Sort.by("dateFinished"));
        List<UserBook> userBooks = userBookRepository.findAllByBookId(bookId, pageable);

        List<UserBook> reviews = userBooks.stream().filter(userBook -> userBook.getStars() != null).collect(Collectors.toList());

        return userBookMapper.convertListUserBook(reviews);
    }

    public Map<Float, List<UserBookDTO>> booksAndStars(Long idUser, Integer year) {
        List<UserBookDTO> booksReadPerYear = booksDTOReadPerYear(idUser, year);
        Map<Float, List<UserBookDTO>> map = new HashMap<>();

        if (booksReadPerYear.size() == 0)
            return map;

        List<Float> stars = Utils.STARS;

        for (Float star : stars
        ) {
            map.put(star, booksReadPerYear.stream()
                    .filter(userBookDTO -> userBookDTO.getStars() != null && userBookDTO.getStars().equals(star))
                    .collect(Collectors.toList()));
        }

        return map;
    }

    public Map<String, List<UserBookDTO>> booksReadPerMonth(Long idUser, Integer year) {
        List<UserBookDTO> booksReadPerYear = booksDTOReadPerYear(idUser, year);
        Map<String, List<UserBookDTO>> map = new HashMap<>();

        if (booksReadPerYear.size() == 0)
            return map;

        List<String> months = Utils.MONTHS;

        for (String month : months
        ) {
            map.put(month, booksReadPerYear.stream()
                    .filter(userBookDTO -> userBookDTO.getDateFinished().getMonth() == months.indexOf(month) + 1)
                    .collect(Collectors.toList()));
        }

        return map;
    }

    
    public List<UserBookDTO> booksDTOReadPerYear(Long idUser, Integer year) {
        List<UserBook> userBooks = userBookRepository.findAllByUserIdAndStatusId(idUser,Utils.READ);
        List<UserBook> booksReadPerYear = userBooks.stream().filter(userBook -> userBook.getDateFinished().getYear() == year).collect(Collectors.toList());
        return userBookMapper.convertListUserBook(booksReadPerYear);
    }

    public Map<String, Integer> pagesReadPerMonth(Long idUser, Integer year) {
        Map<String, List<UserBookDTO>> booksReadPerYear = booksReadPerMonth(idUser, year);
        Map<String, Integer> pagesReadPerYear = new HashMap<>();

        if (booksReadPerYear.size() == 0)
            return pagesReadPerYear;

        for (String month : Utils.MONTHS
        ) {
            pagesReadPerYear.put(month, booksReadPerYear.get(month).stream().mapToInt(userBook -> userBook.getBook().getPages()).sum());
        }

        return pagesReadPerYear;
    }

    
    public UserBookDTO slowestBook(Long idUser, Integer year) {
        List<UserBook> userBooks = userBookRepository.findAllByUserIdAndStatusId(idUser,Utils.READ);
        List<UserBook> booksReadPerYear = userBooks.stream().filter(userBook -> userBook.getDateFinished().getYear() == year).collect(Collectors.toList());

        if (booksReadPerYear.size() == 0)
            return new UserBookDTO();

        Optional<UserBook> slowestBook;

        slowestBook = booksReadPerYear.stream().max(Comparator.comparingLong(userBook -> ChronoUnit.DAYS.between(userBook.getDateStart(), userBook.getDateFinished())));

        if (slowestBook.isPresent())
            return userBookMapper.convertToUserBookDTO(slowestBook.get());

        throw new RuntimeException("Book not found");
    }

    
    public UserBookDTO fastestBook(Long idUser, Integer year) {
        List<UserBook> userBooks = userBookRepository.findAllByUserIdAndStatusId(idUser,Utils.READ);
        List<UserBook> booksReadPerYear = userBooks.stream().filter(userBook -> userBook.getDateFinished().getYear() == year).collect(Collectors.toList());

        if (booksReadPerYear.size() == 0)
            return new UserBookDTO();

        Optional<UserBook> fastestBook;

        fastestBook = booksReadPerYear.stream().min(Comparator.comparingLong(userBook -> ChronoUnit.DAYS.between(userBook.getDateStart(), userBook.getDateFinished())));

        if (fastestBook.isPresent())
            return userBookMapper.convertToUserBookDTO(fastestBook.get());

        throw new RuntimeException("Book not found");
    }

    public UserBookDTO shortestBook(Long idUser, Integer year) {
        List<UserBookDTO> booksReadPerYear = booksDTOReadPerYear(idUser, year);

        if (booksReadPerYear.size() == 0)
            return new UserBookDTO();

        Optional<UserBookDTO> shortestBook = booksReadPerYear.stream().min(Comparator.comparingLong(userBook -> userBook.getBook().getPages()));

        if (shortestBook.isPresent())
            return shortestBook.get();

        throw new RuntimeException("Book not found");
    }

    public UserBookDTO longestBook(Long idUser, Integer year) {
        List<UserBookDTO> booksReadPerYear = booksDTOReadPerYear(idUser, year);

        if (booksReadPerYear.size() == 0)
            return new UserBookDTO();

        Optional<UserBookDTO> longestBook = booksReadPerYear.stream().max(Comparator.comparingLong(userBook -> userBook.getBook().getPages()));

        if (longestBook.isPresent())
            return longestBook.get();

        throw new RuntimeException("Book not found");
    }

    public Map<String, Double> yourYear(Long idUser, Integer year) {
        List<UserBookDTO> booksReadPerYear = booksDTOReadPerYear(idUser, year);
        Map<String, Double> stats = new HashMap<>();

        Double books = (double) booksReadPerYear.size();

        if (books == 0)
            return stats;


        Double pages = booksReadPerYear.stream()
                .mapToDouble(userBook -> userBook.getBook().getPages())
                .sum();

        Double avgStars = booksReadPerYear.stream()
                .mapToDouble(UserBookDTO::getStars)
                .sum() / books;

        Double avgPages = pages / books;
        int month;
        LocalDate now = LocalDate.now();
        if(now.getYear()==year)
        month = now.getMonthValue();
        else
            month = 12;

        Double avgBooks = books / month;

        stats.put("books", books);
        stats.put("pages", pages);
        stats.put("avgStars", avgStars);
        stats.put("avgPages", avgPages);
        stats.put("avgBooks", avgBooks);

        return stats;
    }

    public Map<String, Integer> booksGenre(Long idUser, Integer year) {
        Map<String, Integer> booksGenre = new HashMap<>();

        List<UserBookDTO> booksReadPerYear = booksDTOReadPerYear(idUser, year);

        if (booksReadPerYear.size() == 0)
            return booksGenre;

        List<GenreDTO> allGenre = genreService.getAll();

        for (GenreDTO genre : allGenre
        ) {
            List<UserBookDTO> books = booksReadPerYear.stream().filter(userBookDTO -> userBookDTO.getBook().getGenres().stream().anyMatch(genreDTO -> genre.getName().equals(genreDTO.getName()))).collect(Collectors.toList());
            if (books.size() != 0) {
                booksGenre.put(genre.getName(), books.size());
            }
        }

        return booksGenre;
    }

    public Map<String, Integer> bookFormats(Long idUser, Integer year) {
        Map<String, Integer> bookFormats = new HashMap<>();

        List<UserBookDTO> booksReadPerYear = booksDTOReadPerYear(idUser, year);

        if (booksReadPerYear.size() == 0)
            return bookFormats;

        List<FormatBook> allFormat = formatRepository.findAll();

        for (FormatBook format : allFormat
        ) {
            List<UserBookDTO> books = booksReadPerYear.stream().filter(userBookDTO -> Objects.equals(userBookDTO.getFormatBookId(), format.getId())).collect(Collectors.toList());

            bookFormats.put(format.getName(), books.size());

        }

        return bookFormats;
    }

    public Map<String, Integer> bookCategories(Long idUser, Integer year) {
        Map<String, Integer> bookCategories = new HashMap<>();

        List<UserBookDTO> booksReadPerYear = booksDTOReadPerYear(idUser, year);

        if (booksReadPerYear.size() == 0)
            return bookCategories;

        List<CategoryDTO> categories = categoryService.getAll();

        for (CategoryDTO category : categories
        ) {
            List<UserBookDTO> books = booksReadPerYear.stream().filter(userBookDTO -> userBookDTO.getBook().getCategory().getId() == category.getId()).collect(Collectors.toList());

            bookCategories.put(category.getName(), books.size());

        }

        return bookCategories;
    }

    public Map<String, List<UserBookDTO>> genAuthors(Long idUser, Integer year) {
        Map<String, List<UserBookDTO>> genAuthors = new HashMap<>();

        List<UserBookDTO> booksReadPerYear = booksDTOReadPerYear(idUser, year);

        if (booksReadPerYear.size() == 0)
            return genAuthors;

        List<UserBookDTO> women = booksReadPerYear.stream().filter(userBookDTO -> userBookDTO.getBook().getAuthors().size() == 1 && userBookDTO.getBook().getAuthors().get(0).getSex() == 'f').collect(Collectors.toList());
        List<UserBookDTO> men = booksReadPerYear.stream().filter(userBookDTO -> userBookDTO.getBook().getAuthors().size() == 1 && userBookDTO.getBook().getAuthors().get(0).getSex() == 'm').collect(Collectors.toList());
        List<UserBookDTO> others = booksReadPerYear.stream().filter(userBookDTO -> userBookDTO.getBook().getAuthors().size() > 1 || (userBookDTO.getBook().getAuthors().size() == 1 && userBookDTO.getBook().getAuthors().get(0).getSex() == '/')).collect(Collectors.toList());

        genAuthors.put("women", women);
        genAuthors.put("men", men);
        genAuthors.put("others", others);

        return genAuthors;
    }

    public AuthorDTO favoriteAuthor(Long idUser, Integer year) {
        AuthorDTO authorDTO = new AuthorDTO();

        List<UserBookDTO> booksReadPerYear = booksDTOReadPerYear(idUser, year);

        if (booksReadPerYear.size() == 0)
            return authorDTO;

        List<AuthorDTO> authors = booksReadPerYear.stream().flatMap(userBookDTO -> userBookDTO.getBook().getAuthors().stream()).collect(Collectors.toList());

        Map<Long, Long> f = authors
                .stream()
                .collect(Collectors.groupingBy(AuthorDTO::getId, Collectors.counting()));
        Long id = Collections.max(f.entrySet(), Comparator.comparing(Map.Entry::getValue)).getKey();

        authorDTO = authorMapper.convertToAuthorDTO(authorRepository.getById(id));

        return authorDTO;
    }
}
