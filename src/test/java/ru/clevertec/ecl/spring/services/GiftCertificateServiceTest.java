package ru.clevertec.ecl.spring.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.spring.builders.impls.GiftCertificateTestBuilder;
import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.exception.NotFoundException;
import ru.clevertec.ecl.spring.exception.ServerErrorException;
import ru.clevertec.ecl.spring.repository.GiftCertificateRepository;
import ru.clevertec.ecl.spring.services.impl.GiftCertificateServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class GiftCertificateServiceTest {

    @Mock
    private GiftCertificateRepository certificateRepository;
    GiftCertificateService service;

    private static final GiftCertificateTestBuilder CERTIFICATE_TEST_BUILDER = GiftCertificateTestBuilder.aCertificate();


    private static Stream<Arguments> certificateArgumentsProvider(){
        return Stream.of(
                Arguments.of(new GiftCertificate(3, "gift1", 120, 45, null, null, null))
        );
    }

    private static Stream<Arguments> certificateArgumentsProviderUpdate(){
        return Stream.of(
                Arguments.of(new GiftCertificate(0, "newGift", 120, 45, null, null, null))
        );
    }

    @BeforeEach
    void init() {
        service = new GiftCertificateServiceImpl(certificateRepository);
    }

    @Nested
    class CreateCertificateTest{

        @ParameterizedTest
        @MethodSource("ru.clevertec.ecl.spring.services.GiftCertificateServiceTest#certificateArgumentsProvider")
        public void removeTest(GiftCertificate certificate) {
            service.deleteById(certificate.getId());

            assertThat(service.getByName(certificate.getName())).isNull();
        }

        @Test
        void createTagShouldReturnException() {
            GiftCertificate actual = CERTIFICATE_TEST_BUILDER.build();
            String expectedError = "Error with Created";

            lenient()
                    .doThrow(new ServerErrorException(expectedError))
                    .when(certificateRepository)
                    .save(actual);

            Exception exception = assertThrows(ServerErrorException.class,
                    () -> service.create(actual, null));
            String actualError = exception.getMessage();

            assertThat(actualError).contains(expectedError);
        }

    }

    @Nested
    class FindByIdTest {

        @Test
        void FindByIdShouldReturnCertificate() {
            GiftCertificate expected = CERTIFICATE_TEST_BUILDER.build();
            long id = expected.getId();

            doReturn(Optional.of(expected))
                    .when(certificateRepository)
                    .findById(id);


            GiftCertificate actual = service.getById(id);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void findByIdShouldReturnThrowException() {
            long id = 2L;

            doThrow(new NotFoundException(""))
                    .when(certificateRepository)
                    .findById(id);

            assertThrows(NotFoundException.class, () -> certificateRepository.findById(id));
        }

        @Test
        void findByIdShouldReturnThrowExceptionWithMessage() {
            long id = 5L;
            String expectedError = "Certificate with id - " + id + " not found";

            doThrow(new NotFoundException(expectedError))
                    .when(certificateRepository)
                    .findById(id);

            Exception exception = assertThrows(NotFoundException.class,
                    () -> certificateRepository.findById(id));
            String actualError = exception.getMessage();

            assertThat(actualError).isEqualTo(expectedError);
        }

    }

    @Nested
    class FindByNameTest {

        @Test
        void FindByNameShouldReturnCertificate() {
            GiftCertificate expected = CERTIFICATE_TEST_BUILDER.build();
            String name = expected.getName();

            doReturn(expected)
                    .when(certificateRepository)
                    .findByName(name);


            GiftCertificate actual = service.getByName(name);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void findByNameShouldReturnThrowException() {
            String name = "default";

            doThrow(new NotFoundException(""))
                    .when(certificateRepository)
                    .findByName(name);

            assertThrows(NotFoundException.class, () -> certificateRepository.findByName(name));
        }

        @Test
        void findByNameShouldReturnThrowExceptionWithMessage() {
            String name = "default";
            String expectedError = "Certificate with name - " + name + " not found";

            doThrow(new NotFoundException(expectedError))
                    .when(certificateRepository)
                    .findByName(name);

            Exception exception = assertThrows(NotFoundException.class,
                    () -> certificateRepository.findByName(name));
            String actualError = exception.getMessage();

            assertThat(actualError).isEqualTo(expectedError);
        }

    }
    @Nested
    class GetAllTest {

        @Test
        void getAllShouldReturnListOfSizeOne() {

            int expectedSize = 1;
            Pageable pageable = PageRequest.of(1,1);
            Page<GiftCertificate> page = new PageImpl<>(List.of(CERTIFICATE_TEST_BUILDER.build()));

            doReturn(page)
                    .when(certificateRepository)
                    .findAll(any(PageRequest.class));

            Page<GiftCertificate> actualValues = service.getAll(pageable);

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        void testShouldReturnListWithContainsCertificate() {
            GiftCertificate expected = CERTIFICATE_TEST_BUILDER.build();
            Pageable pageable = PageRequest.of(1,1);
            Page<GiftCertificate> page = new PageImpl<>(List.of(expected));

            doReturn(page)
                    .when(certificateRepository)
                    .findAll(any(PageRequest.class));

            Page<GiftCertificate> actualValues = service.getAll(pageable);

            assertThat(actualValues).contains(expected);
        }

        @Test
        void testShouldReturnException() {
            Pageable pageable = PageRequest.of(1,1);

            doThrow(new NotFoundException(""))
                    .when(certificateRepository)
                    .findAll(any(PageRequest.class));

            assertThrows(NotFoundException.class, () -> service.getAll(pageable));
        }
    }

    @Nested
    class DeleteCertificateTest{

        @ParameterizedTest
        @MethodSource("ru.clevertec.ecl.spring.services.GiftCertificateServiceTest#certificateArgumentsProvider")
        public void removeTest(GiftCertificate certificate) {
            service.deleteById(certificate.getId());

            assertThat(service.getByName(certificate.getName())).isNull();
        }

        @Test
        void deleteCertificateShouldReturnException() {
            long id = CERTIFICATE_TEST_BUILDER.build().getId();
            String expectedError = "Certificate with id - " + id + " not found";

            lenient()
                    .doThrow(new NotFoundException(expectedError))
                    .when(certificateRepository)
                    .deleteById(id);

            Exception exception = assertThrows(NotFoundException.class,
                    () -> service.deleteById(id));
            String actualError = exception.getMessage();

            assertThat(actualError).isEqualTo(expectedError);
        }

    }

    @Nested
    class UpdateCertificateTest{

        @ParameterizedTest
        @MethodSource("ru.clevertec.ecl.spring.services.GiftCertificateServiceTest#certificateArgumentsProviderUpdate")
        public void updateTest(GiftCertificate certificate) {
            service.update(certificate);

            assertThat(service.getByName("newGift")).isNotNull();
        }

        @Test
        void updateCertificateShouldReturnException() {
            GiftCertificate actual = CERTIFICATE_TEST_BUILDER.build();
            String expectedError = "Error with Update";

            lenient()
                    .doThrow(new ServerErrorException(expectedError))
                    .when(certificateRepository)
                    .save(actual);

            Exception exception = assertThrows(ServerErrorException.class,
                    () -> service.update(actual));
            String actualError = exception.getMessage();

            assertThat(actualError).contains(expectedError);
        }

    }

}
