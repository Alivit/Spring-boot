package ru.clevertec.ecl.spring.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.entity.Order;
import ru.clevertec.ecl.spring.entity.User;
import ru.clevertec.ecl.spring.exception.NotFoundException;
import ru.clevertec.ecl.spring.exception.ServerErrorException;
import ru.clevertec.ecl.spring.repository.GiftCertificateRepository;
import ru.clevertec.ecl.spring.repository.OrderRepository;
import ru.clevertec.ecl.spring.repository.UserRepository;
import ru.clevertec.ecl.spring.services.OrderService;

/**
 * Класс сервис crud операций для работы с заказами
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    /**
     * Это поле интерфейса описывающее поведение
     * сервис обработчика запросов sql
     * @see GiftCertificateRepository
     */
    private final GiftCertificateRepository certificateRepository;

    /**
     * Это поле интерфейса описывающее поведение
     * сервис обработчика запросов sql
     * @see UserRepository
     */
    private final UserRepository userRepository;

    /**
     * Метод созданный для добавления заказа в таблицу orders
     *
     * @param order содержит данные о заказе
     * @param idUser содержит id пользователя
     * @param idCertificate содержит id сертификата
     */
    @Override
    @Transactional
    public Order create(Order order, Long idUser, Long idCertificate) {
        try {
            GiftCertificate certificate = certificateRepository.findById(idCertificate).orElseThrow(() ->
                    new NotFoundException("Certificate with id - " + idCertificate + " not found"));
            log.info("Certificate with id " + idCertificate + ": {}", certificate);

            User user = userRepository.findById(idUser).orElseThrow(() ->
                    new NotFoundException("User with id - " + idUser + " not found"));
            log.info("User with id " + idUser + ": {}", user);

            orderRepository.save(createOrderWithCertificate(order, certificate, user));
            log.info("Insert order");

            return getById(order.getId());
        }catch (Exception e){
            throw new ServerErrorException("Error with Insert certificate: " + e);
        }
    }

    /**
     * Метод созданный для создания заказа с сертификатом
     *
     * @param order содержит данные о заказе
     * @param certificate содержит данные о подарочном сертификате
     * @param user содержит данные о пользователе
     *
     * @return объект заказа со сертификатом
     */
    private Order createOrderWithCertificate(Order order,
                                             GiftCertificate certificate,
                                             User user)
    {
        order.setPrice(certificate.getPrice());
        order.setUser(user);
        order.getCertificates().add(certificate);

        return order;
    }

    /**
     * Метод который получает данные из таблицы orders в виде страницы
     * offset - начало страницы, limit - конец страницы
     * и сортирующий страницу по имени поля и по методу ASC/DESC
     *
     * @param pageable содержит информацию о страницы
     *
     * @return страницу заказов со всей информацией
     */
    @Override
    public Page<Order> getAll(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        if(orders.isEmpty()) throw new NotFoundException("Orders not found");
        log.info("Orders : {}", orders);

        return orders;
    }

    @Override
    @Transactional
    public Order update(Order order, User user, GiftCertificate certificate) {
        try {
            orderRepository.save(updateOrderWithCertificate(order, certificate, user));
            log.info("Update order");

            return getById(order.getId());
        }catch (Exception e){
            throw new ServerErrorException("Error with Update certificate: " + e);
        }
    }

    /**
     * Метод созданный для удаления данных из таблицы orders по id
     *
     * @param id содержит id тега
     *
     * @return заказа со всей информацией
     */
    @Override
    @Transactional
    public Order deleteById(Long id) {
        try {
            Order order = getById(id);
            orderRepository.delete(order);
            log.info("Order with id " + id + " was deleted");
            return order;
        }catch (NotFoundException e){
            throw e;
        }catch (Exception e){
            throw new ServerErrorException("Error with Delete order: " + e);
        }
    }

    /**
     * Метод который находит данные из таблицы orders по id
     *
     * @param id содержит id заказа
     *
     * @return заказа со всей информацией
     */
    @Override
    public Order getById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Order with id - " + id + " not found"));
        log.info("Order with id " + id + ": {}", order);

        return order;
    }

    /**
     * Метод созданный для обновления заказа с сертификатом
     *
     * @param order содержит данные о заказе
     * @param certificate содержит данные о подарочном сертификате
     * @param user содержит данные о пользователе
     *
     * @return объект заказа со сертификатом
     */
    private Order updateOrderWithCertificate(Order order,
                                             GiftCertificate certificate,
                                             User user)
    {
        GiftCertificate oldCertificate = certificateRepository.getReferenceById(certificate.getId());
        log.info("Certificate with id " + certificate.getId() + ": {}", certificate);
        oldCertificate.setName(certificate.getName());
        oldCertificate.setPrice(certificate.getPrice());
        oldCertificate.setDuration(certificate.getDuration());
        oldCertificate.setTags(certificate.getTags());

        User oldUser = userRepository.getReferenceById(user.getId());
        log.info("User with id " + user.getId() + ": {}", user);
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());

        Order oldOrder = orderRepository.getReferenceById(order.getId());
        oldOrder.setUser(oldUser);
        oldOrder.getCertificates().add(oldCertificate);

        return oldOrder;
    }
}
