package ru.clevertec.ecl.spring.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.spring.entity.User;
import ru.clevertec.ecl.spring.exception.NotFoundException;
import ru.clevertec.ecl.spring.repository.UserRepository;
import ru.clevertec.ecl.spring.services.UserService;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class  UserServiceImpl implements UserService {

    /**
     * Это поле интерфейса описывающее поведение
     * сервис обработчика запросов sql
     * @see UserRepository
     */
    private final UserRepository repository;

    /**
     * Метод который находит данные из таблицы users по id
     *
     * @param id содержит id пользователя
     *
     * @return пользователя со всей информацией
     */
    @Override
    public User getById(Long id) {
        User user = repository.findById(id).orElseThrow(() ->
                new NotFoundException("User with id - " + id + " not found"));
        log.info("User with id " + id + ": {}", user);

        return user;
    }

    /**
     * Метод который получает данные из таблицы users в виде страницы
     * offset - начало страницы, limit - конец страницы
     * и сортирующий страницу по имени поля и по методу ASC/DESC
     *
     * @param offset содержит начало страницы
     * @param limit содержит конец страницы
     * @param sort лист с обьектами по которым надо сортировать
     *
     * @return страницу пользователей со всей информацией
     */
    @Override
    public Page<User> getAll(Integer offset, Integer limit, String sort) {
        String[] sorting = sort.split(",");

        Page<User> users = repository.findAll(
                PageRequest.of(offset,limit, sortSetting(sorting[0], sorting[1])));
        if(users.isEmpty()) throw new NotFoundException("Users not found");
        log.info("Users : {}", users);

        return users;
    }

    /**
     * Метод который настраивает сортировку по имени поля и по методу ASC/DESC
     *
     * @param sortField содержит имя поле
     * @param sortMethod содержит метод сортировки ASC/DESC
     *
     * @return объект сортировки
     */
    private Sort sortSetting(String sortField, String sortMethod){
        if("ASC".equals(sortMethod)) {
            return Sort.by(Sort.Direction.ASC, sortField);
        }
        else {
            return Sort.by(Sort.Direction.DESC, sortField);
        }
    }
}
