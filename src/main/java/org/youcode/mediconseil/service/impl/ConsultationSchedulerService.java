package org.youcode.mediconseil.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.youcode.mediconseil.domain.Consultation;
import org.youcode.mediconseil.domain.enums.ConsultationStatus;
import org.youcode.mediconseil.repository.ConsultationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationSchedulerService {

    private final ConsultationRepository consultationRepository;

    @Scheduled(fixedRate = 900000) // 15 minutes in milliseconds
    @Transactional
    public void updateConsultationStatuses() {
        log.info("Running scheduled task to update consultation statuses...");

        // Get current time
        LocalDateTime now = LocalDateTime.now();

        // 1. Cancel pending consultations that are past their time
        List<Consultation> pendingPastConsultations = consultationRepository
                .findByStatusAndDateConsultationLessThan(ConsultationStatus.PENDING, now);

        if (!pendingPastConsultations.isEmpty()) {
            pendingPastConsultations.forEach(consultation -> {
                consultation.setStatus(ConsultationStatus.CANCELLED);
            });

            consultationRepository.saveAll(pendingPastConsultations);
        }

        // 2. Mark confirmed consultations as completed if their time + 30 minutes is passed
        LocalDateTime thirtyMinutesAgo = now.minusMinutes(30);
        List<Consultation> confirmedPastConsultations = consultationRepository
                .findByStatusAndDateConsultationLessThan(ConsultationStatus.CONFIRMED, thirtyMinutesAgo);

        if (!confirmedPastConsultations.isEmpty()) {
            confirmedPastConsultations.forEach(consultation -> {
                consultation.setStatus(ConsultationStatus.COMPLETED);
            });

            consultationRepository.saveAll(confirmedPastConsultations);
        }
    }
}