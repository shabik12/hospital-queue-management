package com.hospital.hospital_queue_management.service.imp;

import com.hospital.hospital_queue_management.dto.request.MedicineBatchRequest;
import com.hospital.hospital_queue_management.dto.response.MedicineBatchResponse;
import com.hospital.hospital_queue_management.entity.Medicine;
import com.hospital.hospital_queue_management.entity.MedicineBatch;
import com.hospital.hospital_queue_management.exception.ResourceAlreadyExistsException;
import com.hospital.hospital_queue_management.exception.ResourceNotFoundException;
import com.hospital.hospital_queue_management.mapper.MedicineBatchMapper;
import com.hospital.hospital_queue_management.repository.MedicineBatchRepository;
import com.hospital.hospital_queue_management.repository.MedicineRepository;
import com.hospital.hospital_queue_management.service.MedicineBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicineBatchServiceImpl implements MedicineBatchService {

    private final MedicineBatchRepository medicineBatchRepository;
    private final MedicineRepository medicineRepository;
    private final MedicineBatchMapper medicineBatchMapper;

    @Override
    public MedicineBatchResponse addBatch(MedicineBatchRequest request) {

        String batchNumber = request.getBatchNumber().trim();

        if (medicineBatchRepository.existsByBatchNumberIgnoreCase(batchNumber)) {
            throw new ResourceAlreadyExistsException(
                    "Batch already exists: " + batchNumber
            );
        }

        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medicine not found with id: "
                                        + request.getMedicineId()
                        ));

        if (!medicine.getActive()) {
            throw new IllegalArgumentException(
                    "Cannot add batch for inactive medicine"
            );
        }

        MedicineBatch batch =
                medicineBatchMapper.toEntity(request, medicine);

        MedicineBatch savedBatch =
                medicineBatchRepository.save(batch);

        return medicineBatchMapper.toResponse(savedBatch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineBatchResponse> getAllBatches() {

        return medicineBatchRepository.findAll()
                .stream()
                .map(medicineBatchMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineBatchResponse getBatchById(Long id) {

        MedicineBatch batch = findBatchById(id);

        return medicineBatchMapper.toResponse(batch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineBatchResponse> getBatchesByMedicine(
            Long medicineId) {

        if (!medicineRepository.existsById(medicineId)) {
            throw new ResourceNotFoundException(
                    "Medicine not found with id: " + medicineId
            );
        }

        return medicineBatchRepository
                .findByMedicineIdOrderByExpiryDateAsc(medicineId)
                .stream()
                .map(medicineBatchMapper::toResponse)
                .toList();
    }

    private MedicineBatch findBatchById(Long id) {

        return medicineBatchRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medicine batch not found with id: " + id
                        ));
    }
}