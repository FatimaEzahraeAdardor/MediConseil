package org.youcode.mediconseil.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.youcode.mediconseil.domain.Availability;
import org.youcode.mediconseil.domain.Consultation;
import org.youcode.mediconseil.domain.Doctor;
import org.youcode.mediconseil.domain.User;
import org.youcode.mediconseil.domain.enums.ConsultationStatus;
import org.youcode.mediconseil.repository.ConsultationRepository;
import org.youcode.mediconseil.service.AvailabilityService;
import org.youcode.mediconseil.service.UserService;
import org.youcode.mediconseil.web.exception.ResourceNotFoundException;
import org.youcode.mediconseil.web.vm.request.ConsultationRequestVm;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceImplTest {

    @Mock
    private ConsultationRepository consultationRepository;

    @Mock
    private AvailabilityService availabilityService;

    @Mock
    private UserService userService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ConsultationServiceImpl consultationService;

    private UUID consultationId;
    private UUID doctorId;
    private UUID patientId;
    private UUID availabilityId;
    private Doctor doctor;
    private User patient;
    private Availability availability;
    private Consultation consultation;
    private ConsultationRequestVm requestVm;
    private LocalDateTime consultationDateTime;

    @BeforeEach
    void setUp() {
        // Initialize test data
        consultationId = UUID.randomUUID();
        doctorId = UUID.randomUUID();
        patientId = UUID.randomUUID();
        availabilityId = UUID.randomUUID();
        consultationDateTime = LocalDateTime.now().plusDays(1);

        // Create doctor
        doctor = new Doctor();
        doctor.setId(doctorId);
        doctor.setFirstName("Doctor");
        doctor.setLastName("Smith");
        doctor.setEmail("doctor@example.com");
        doctor.setPassword("password");

        // Create patient
        patient = new User();
        patient.setId(patientId);
        patient.setFirstName("Patient");
        patient.setLastName("Johnson");
        patient.setEmail("patient@example.com");

        // Create availability
        availability = new Availability();
        availability.setId(availabilityId);
        availability.setDoctor(doctor);
        availability.setStartTime(consultationDateTime);
        availability.setEndTime(consultationDateTime.plusMinutes(30));
        availability.setIsBooked(false);

        // Create consultation
        consultation = new Consultation();
        consultation.setId(consultationId);
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);
        consultation.setMotif("Check-up");
        consultation.setDateConsultation(consultationDateTime);
        consultation.setStatus(ConsultationStatus.PENDING);

        // Create request view model
        requestVm = new ConsultationRequestVm();
        requestVm.setDoctorId(doctorId);
        requestVm.setPatientId(patientId);
        requestVm.setAvailabilityId(availabilityId);
        requestVm.setMotif("Check-up");
        requestVm.setDateConsultation(consultationDateTime);
    }

    @Test
    void save_ShouldSaveAndReturnConsultation() {
        // Arrange
        when(consultationRepository.save(consultation)).thenReturn(consultation);

        // Act
        Consultation result = consultationService.save(consultation);

        // Assert
        assertEquals(consultation, result);
        verify(consultationRepository).save(consultation);
    }

    @Test
    void bookConsultation_WhenAvailabilityNotBooked_ShouldBookConsultation() {
        // Arrange
        when(availabilityService.findOrCreateAvailabilityById(
                eq(doctorId), eq(availabilityId), eq(consultationDateTime), any(LocalDateTime.class)))
                .thenReturn(availability);

        when(userService.findByID(patientId)).thenReturn(Optional.of(patient));
        when(consultationRepository.save(any(Consultation.class))).thenAnswer(invocation -> {
            Consultation savedConsultation = invocation.getArgument(0);
            savedConsultation.setId(consultationId);
            return savedConsultation;
        });

        // Act
        Consultation result = consultationService.bookConsultation(requestVm);

        // Assert
        assertNotNull(result);
        assertEquals(doctor, result.getDoctor());
        assertEquals(patient, result.getPatient());
        assertEquals(requestVm.getMotif(), result.getMotif());
        assertEquals(consultationDateTime, result.getDateConsultation());
        assertEquals(ConsultationStatus.PENDING, result.getStatus());

        verify(availabilityService).findOrCreateAvailabilityById(
                eq(doctorId), eq(availabilityId), eq(consultationDateTime), any(LocalDateTime.class));
        verify(availabilityService).save(availability);
        verify(userService).findByID(patientId);
        verify(consultationRepository).save(any(Consultation.class));
        assertTrue(availability.getIsBooked());
    }

    @Test
    void bookConsultation_WhenAvailabilityAlreadyBooked_ShouldThrowException() {
        // Arrange
        availability.setIsBooked(true);
        when(availabilityService.findOrCreateAvailabilityById(
                eq(doctorId), eq(availabilityId), eq(consultationDateTime), any(LocalDateTime.class)))
                .thenReturn(availability);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            consultationService.bookConsultation(requestVm);
        });

        assertEquals("Ce créneau horaire est déjà réservé.", exception.getMessage());
        verify(availabilityService).findOrCreateAvailabilityById(
                eq(doctorId), eq(availabilityId), eq(consultationDateTime), any(LocalDateTime.class));
        verify(availabilityService, never()).save(any(Availability.class));
        verify(userService, never()).findByID(any(UUID.class));
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    void bookConsultation_WhenPatientNotFound_ShouldThrowException() {
        // Arrange
        when(availabilityService.findOrCreateAvailabilityById(
                eq(doctorId), eq(availabilityId), eq(consultationDateTime), any(LocalDateTime.class)))
                .thenReturn(availability);
        when(userService.findByID(patientId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            consultationService.bookConsultation(requestVm);
        });

        assertEquals("Patient not found with ID: " + patientId, exception.getMessage());
        verify(availabilityService).findOrCreateAvailabilityById(
                eq(doctorId), eq(availabilityId), eq(consultationDateTime), any(LocalDateTime.class));
        verify(availabilityService).save(availability);
        verify(userService).findByID(patientId);
        verify(consultationRepository, never()).save(any(Consultation.class));
        assertTrue(availability.getIsBooked());
    }

    @Test
    void update_WhenConsultationExists_ShouldUpdateConsultation() {
        // Arrange
        Consultation updatedConsultation = new Consultation();
        updatedConsultation.setMotif("Updated motif");
        updatedConsultation.setStatus(ConsultationStatus.CONFIRMED);

        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(consultation));
        when(consultationRepository.save(any(Consultation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Consultation result = consultationService.update(consultationId, updatedConsultation);

        // Assert
        assertEquals("Updated motif", result.getMotif());
        assertEquals(ConsultationStatus.CONFIRMED, result.getStatus());
        verify(consultationRepository).findById(consultationId);
        verify(consultationRepository).save(consultation);
    }

    @Test
    void update_WhenConsultationDoesNotExist_ShouldThrowException() {
        // Arrange
        Consultation updatedConsultation = new Consultation();
        when(consultationRepository.findById(consultationId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            consultationService.update(consultationId, updatedConsultation);
        });

        assertEquals("Consultation not found", exception.getMessage());
        verify(consultationRepository).findById(consultationId);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    void delete_ShouldDeleteConsultation() {
        // Act
        Boolean result = consultationService.delete(consultationId);

        // Assert
        assertTrue(result);
        verify(consultationRepository).deleteById(consultationId);
    }

    @Test
    void findByPatientId_ShouldReturnPageOfConsultations() {
        // Arrange
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        List<Consultation> consultations = Arrays.asList(consultation);
        Page<Consultation> expectedPage = new PageImpl<>(consultations, pageable, consultations.size());

        when(consultationRepository.findByPatientId(patientId, pageable)).thenReturn(expectedPage);

        // Act
        Page<Consultation> result = consultationService.findByPatientId(patientId, page, size);

        // Assert
        assertEquals(expectedPage, result);
        verify(consultationRepository).findByPatientId(patientId, pageable);
    }

    @Test
    void findByDoctorId_ShouldReturnPageOfConsultations() {
        // Arrange
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        List<Consultation> consultations = Arrays.asList(consultation);
        Page<Consultation> expectedPage = new PageImpl<>(consultations, pageable, consultations.size());

        when(consultationRepository.findByDoctorId(doctorId, pageable)).thenReturn(expectedPage);

        // Act
        Page<Consultation> result = consultationService.findByDoctorId(doctorId, page, size);

        // Assert
        assertEquals(expectedPage, result);
        verify(consultationRepository).findByDoctorId(doctorId, pageable);
    }

    @Test
    void cancelConsultation_WhenConsultationIsPending_ShouldCancelConsultation() {
        // Arrange
        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(consultation));
        when(availabilityService.findByDoctorAndStartDate(doctorId, consultationDateTime))
                .thenReturn(Optional.of(availability));
        when(consultationRepository.save(any(Consultation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Consultation result = consultationService.cancelConsultation(consultationId);

        // Assert
        assertEquals(ConsultationStatus.CANCELLED, result.getStatus());
        assertFalse(availability.getIsBooked());
        verify(consultationRepository).findById(consultationId);
        verify(availabilityService).findByDoctorAndStartDate(doctorId, consultationDateTime);
        verify(availabilityService).save(availability);
        verify(consultationRepository).save(consultation);
    }

    @Test
    void cancelConsultation_WhenConsultationIsNotPending_ShouldThrowException() {
        // Arrange
        consultation.setStatus(ConsultationStatus.CONFIRMED);
        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(consultation));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            consultationService.cancelConsultation(consultationId);
        });

        assertEquals("Seules les consultations en attente peuvent être annuléés.", exception.getMessage());
        verify(consultationRepository).findById(consultationId);
        verify(availabilityService, never()).findByDoctorAndStartDate(any(UUID.class), any(LocalDateTime.class));
        verify(availabilityService, never()).save(any(Availability.class));
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    void cancelConsultation_WhenConsultationNotFound_ShouldThrowException() {
        // Arrange
        when(consultationRepository.findById(consultationId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            consultationService.cancelConsultation(consultationId);
        });

        assertEquals("Consultation not found with ID: " + consultationId, exception.getMessage());
        verify(consultationRepository).findById(consultationId);
        verify(availabilityService, never()).findByDoctorAndStartDate(any(UUID.class), any(LocalDateTime.class));
        verify(availabilityService, never()).save(any(Availability.class));
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    void cancelConsultation_WhenAvailabilityNotFound_ShouldStillCancelConsultation() {
        // Arrange
        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(consultation));
        when(availabilityService.findByDoctorAndStartDate(doctorId, consultationDateTime))
                .thenReturn(Optional.empty());
        when(consultationRepository.save(any(Consultation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Consultation result = consultationService.cancelConsultation(consultationId);

        // Assert
        assertEquals(ConsultationStatus.CANCELLED, result.getStatus());
        verify(consultationRepository).findById(consultationId);
        verify(availabilityService).findByDoctorAndStartDate(doctorId, consultationDateTime);
        verify(availabilityService, never()).save(any(Availability.class));
        verify(consultationRepository).save(consultation);
    }

    @Test
    void confirmConsultation_WhenConsultationIsPending_ShouldConfirmConsultation() {
        // Arrange
        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(consultation));
        when(consultationRepository.save(any(Consultation.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(emailService).sendConsultationConfirmationEmail(any(Consultation.class));

        // Act
        Consultation result = consultationService.confirmConsultation(consultationId);

        // Assert
        assertEquals(ConsultationStatus.CONFIRMED, result.getStatus());
        verify(consultationRepository).findById(consultationId);
        verify(consultationRepository).save(consultation);
        verify(emailService).sendConsultationConfirmationEmail(consultation);
    }

    @Test
    void confirmConsultation_WhenConsultationIsNotPending_ShouldThrowException() {
        // Arrange
        consultation.setStatus(ConsultationStatus.CANCELLED);
        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(consultation));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            consultationService.confirmConsultation(consultationId);
        });

        assertEquals("Seules les consultations en attente peuvent être confirmées.", exception.getMessage());
        verify(consultationRepository).findById(consultationId);
        verify(consultationRepository, never()).save(any(Consultation.class));
        verify(emailService, never()).sendConsultationConfirmationEmail(any(Consultation.class));
    }

    @Test
    void confirmConsultation_WhenConsultationNotFound_ShouldThrowException() {
        // Arrange
        when(consultationRepository.findById(consultationId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            consultationService.confirmConsultation(consultationId);
        });

        assertEquals("Consultation not found with ID: " + consultationId, exception.getMessage());
        verify(consultationRepository).findById(consultationId);
        verify(consultationRepository, never()).save(any(Consultation.class));
        verify(emailService, never()).sendConsultationConfirmationEmail(any(Consultation.class));
    }
}