package com.example.myLibrary.mapper;

import com.example.myLibrary.model.*;
import com.example.myLibrary.model.dto.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserBookMapper {

    private final FormatMapper formatMapper;
    private final StatusMapper statusMapper;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;


    public UserBookMapper(FormatMapper formatMapper, StatusMapper statusMapper, UserMapper userMapper, BookMapper bookMapper) {
        this.formatMapper = formatMapper;
        this.statusMapper = statusMapper;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

//    public UserBookDTO convertToUserBookDTO(UserBook userBook){
//        FormatBookDTO formatBookDTO;
//        if(userBook.getFormatBook()!=null)
//         formatBookDTO = formatMapper.convertToFormatDTO(userBook.getFormatBook());
//        else
//            formatBookDTO = null;
//        StatusDTO statusDTO = statusMapper.convertToStatusDTO(userBook.getStatus());
//        UserDTO userDTO = userMapper.convertToUserDTO(userBook.getUser());
//        BookDTO bookDTO = bookMapper.convertToBookDTO(userBook.getBook());
//       return new UserBookDTO(userBook.getId(), userBook.getDateStart(), userBook.getDateFinished(),
//               userBook.getStars(),userBook.getReview(),formatBookDTO,statusDTO,userDTO,bookDTO);
//    }

    public UserBookDTO convertToUserBookDTO(UserBook userBook) {
        FormatBookDTO formatBookDTO;
        if (userBook.getFormatBook() != null)
            formatBookDTO = formatMapper.convertToFormatDTO(userBook.getFormatBook());
        else
            formatBookDTO = null;
        StatusDTO statusDTO = statusMapper.convertToStatusDTO(userBook.getStatus());

        UserDTO userDTO = userMapper.convertToUserDTO(userBook.getUser());
        BookDTO bookDTO = bookMapper.convertToBookDTO(userBook.getBook());
        MyDate dateStart, dateFinished;

        if (userBook.getDateStart() != null)
            dateStart = new MyDate(userBook.getDateStart());
        else dateStart = null;
        if (userBook.getDateFinished() != null)
            dateFinished = new MyDate(userBook.getDateFinished());
        else dateFinished = null;


        return new UserBookDTO(userBook.getId(), dateStart, dateFinished,
                userBook.getStars(), userBook.getReview(), formatBookDTO != null ? formatBookDTO.getId() : null, statusDTO.getId(), userDTO,bookDTO);
    }

    public UserBook convertToUserBook(UserBookDTO userBookDTO, Book book, User user, FormatBook format, Status status) {
        UserBook userBook = new UserBook();

        if (userBookDTO.getDateStart() != null)
            userBook.setDateStart(userBookDTO.getDateStart().convertToLocalDate());
        else
            userBook.setDateStart(null);

        if (userBookDTO.getDateFinished() != null)
            userBook.setDateFinished(userBookDTO.getDateFinished().convertToLocalDate());
        else
            userBook.setDateFinished(null);

        userBook.setBook(book);
        userBook.setUser(user);
        userBook.setFormatBook(format);
        userBook.setStars(userBookDTO.getStars());
        userBook.setStatus(status);
        userBook.setReview(userBookDTO.getReview());
        return userBook;
    }

    public List<UserBookDTO> convertListUserBook(List<UserBook> userBooks){
        List<UserBookDTO> userBookDTOS = new ArrayList<>();

        for (UserBook userBook: userBooks
        ) {
            userBookDTOS.add(convertToUserBookDTO(userBook));
        }
        return  userBookDTOS;
    }


}
