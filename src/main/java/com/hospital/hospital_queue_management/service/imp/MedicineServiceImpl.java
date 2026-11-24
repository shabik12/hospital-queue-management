package com.hospital.hospital_queue_management.service.imp;

import com.hospital.hospital_queue_management.dto.request.MedicineRequest;
import com.hospital.hospital_queue_management.dto.request.MedicineUpdateRequest;
import com.hospital.hospital_queue_management.dto.response.MedicineResponse;
import com.hospital.hospital_queue_management.entity.Medicine;
import com.hospital.hospital_queue_management.exception.ResourceAlreadyExistsException;
import com.hospital.hospital_queue_management.exception.ResourceNotFoundException;
import com.hospital.hospital_queue_management.mapper.MedicineMapper;
import com.hospital.hospital_queue_management.repository.MedicineRepository;
import com.hospital.hospital_queue_management.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Override
    public MedicineResponse createMedicine(MedicineRequest request) {

        String medicineName = request.getName().trim();

        if (medicineRepository.existsByNameIgnoreCase(medicineName)) {
            throw new ResourceAlreadyExistsException(
                    "Medicine already exists: " + medicineName
            );
        }

        Medicine medicine = medicineMapper.toEntity(request);

        Medicine savedMedicine = medicineRepository.save(medicine);

        return medicineMapper.toResponse(savedMedicine);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponse> getAllMedicines() {

        return medicineRepository.findAll()
                .stream()
                .map(medicineMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineResponse getMedicineById(Long id) {

        Medicine medicine = findMedicineById(id);

        return medicineMapper.toResponse(medicine);
    }

    @Override
    public MedicineResponse updateMedicine(
            Long id,
            MedicineUpdateRequest request
    ) {

        Medicine medicine = findMedicineById(id);

        String newName = request.getName().trim();

        if (!medicine.getName().equalsIgnoreCase(newName)
                && medicineRepository.existsByNameIgnoreCase(newName)) {

            throw new ResourceAlreadyExistsException(
                    "Medicine already exists: " + newName
            );
        }

        medicineMapper.updateEntity(medicine, request);

        return medicineMapper.toResponse(medicine);
    }

    @Override
    public MedicineResponse activateMedicine(Long id) {

        Medicine medicine = findMedicineById(id);

        medicine.setActive(true);

        return medicineMapper.toResponse(medicine);
    }

    @Override
    public MedicineResponse deactivateMedicine(Long id) {

        Medicine medicine = findMedicineById(id);

        medicine.setActive(false);

        return medicineMapper.toResponse(medicine);
    }

    private Medicine findMedicineById(Long id) {

        return medicineRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medicine not found with id: " + id
                        )
                );
    }
}